import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { firstValueFrom, Observable } from 'rxjs';
import { RegraFinanceira } from 'src/app/models/regra-financeira';
import { LoadingService } from 'src/app/services/loading.service';
import { RegraFinanceiraService } from 'src/app/services/regra-financeira.service';
import { ToastService } from 'src/app/services/toast.service';
import { Acao, Enumeradores, ItemEnum } from 'src/app/utils/helper';
import { Utils } from 'src/app/utils/util';

@Component({
  selector: 'app-regra-form',
  templateUrl: './regra-form.component.html',
  styleUrls: ['./regra-form.component.css']
})
export class RegraFormComponent implements OnInit {

  protected isLoaded: boolean = Utils.FALSO;
  protected isLoading$: Observable<boolean>;
  protected loadingMessage$: Observable<string>;
  protected acao!: Enumeradores;
  protected regra: RegraFinanceira = {} as RegraFinanceira;
  protected listaTpCobranca: ItemEnum[] = Object.values(Enumeradores.TipoCobranca);
  protected listaTpMov: ItemEnum[] = Object.values(Enumeradores.TipoMovVeiculo);
  protected listaSituacao: ItemEnum[] = Object.values(Enumeradores.Situacao);
  protected formRegraFinanc = this.fb.group({
    id: new FormControl<number | null>(null),
    descricao: new FormControl<string>('TESTE', Validators.required),
    valor: new FormControl<number>(10.0, Validators.required),
    tipoCobranca: new FormControl(1, Validators.required),
    tipoMovimento: new FormControl(1, Validators.required),
    dtInicioValidade: new FormControl<string>('2026-01-29', [Validators.required, Validators.pattern(/^\d{4}-\d{2}-\d{2}$/)]),
    dtFimValidade: new FormControl<string>('',[Validators.pattern(/^\d{4}-\d{2}-\d{2}$/)]),
    situacao: new FormControl<number | null>(null),
    versao: new FormControl<number | null>(null)
  });

  constructor(private fb: FormBuilder, private rota: ActivatedRoute,
    private rs:RegraFinanceiraService, private ts: ToastService, private ls: LoadingService){
    this.isLoading$ = this.ls.isLoading$;
    this.loadingMessage$ = this.ls.loadingMessage$;
  }

  ngOnInit(): void {

    this.formRegraFinanc.get("tipoCobranca")?.setValue(this.listaTpCobranca[0].id, { emitEvent: Utils.FALSO });
    this.formRegraFinanc.get("tipoMovimento")?.setValue(this.listaTpMov[0].id, { emitEvent: Utils.FALSO });
    this.formRegraFinanc.get("situacao")?.setValue(Enumeradores.Situacao["ATIVO"].id, { emitEvent: Utils.FALSO });
    this.acao = this.rota.snapshot.url[0].path == 'cadastrar' ? Acao.CADASTRAR : Acao.EDITAR;

    if(this.acao == Acao.EDITAR){
      let idRegra = Number(this.rota.snapshot.url[1].path);
      this.rs.getRegraById(idRegra).subscribe({
        next:(resp) => {
          this.regra = resp.registros[0];
          this.formRegraFinanc.patchValue(resp.registros[0]);
        }
        , error:(err) => {
          console.error('Erro ao obter a regra: '+err.erro.mensagem);
          this.ts.gerarToast(`Ocorreu um erro ao carregar a regra: ${err.erro.mensagem}`, Utils.FALSO);
        }
      })
    }

    this.formRegraFinanc.get("situacao")?.valueChanges.subscribe(value => {
      if(value != Enumeradores.Situacao['CADASTRADO'].id){
        this.formRegraFinanc.get("dtFimValidade")?.setValidators(Validators.required)
      } else {
        this.formRegraFinanc.controls.dtFimValidade.removeValidators(Validators.required)
      }
      this.formRegraFinanc.controls.dtFimValidade.updateValueAndValidity();
    });

    this.isLoaded = Utils.VERDADEIRO;
  }

  acaoRegra(): void {
    this.isLoaded = Utils.FALSO;

    if(this.acao == Acao.EDITAR){
      this.updateRegra();
    } else {
      this.createRegra();
    }
  }

  private async createRegra(): Promise<void> {
    let regra = this.criarRegraForm();
    console.log(regra)
    await firstValueFrom(this.rs.createRegra(regra))
      .then(resp => {
        this.regra = resp.registros[0];
        this.ts.gerarToast(`Regra '${this.regra.descricao}' criada com sucesso!`, Utils.VERDADEIRO);
      })
      .catch(err => {
        this.ts.gerarToast(`Erro ao criar a regra: ${err.error.mensagem}`, Utils.FALSO);
        console.error('Erro ao criar a regra: '+err.error.mensagem);
      })
      .finally(() => this.isLoaded = Utils.VERDADEIRO)
  }

  private async updateRegra(): Promise<void> {
    let regraForm = this.criarRegraForm();

    let foiAlterado =
      regraForm.descricao !== this.regra.descricao ||
      regraForm.dtFimValidade !== this.regra.dtFimValidade ||
      regraForm.situacao !== this.regra.situacao;

    let naoPodemSerAlterados =
      regraForm.valor !== this.regra.valor ||
      regraForm.tipoCobranca !== this.regra.tipoCobranca ||
      regraForm.tipoMovimento !== this.regra.tipoMovimento ||
      regraForm.dtInicioValidade !== this.regra.dtInicioValidade;

    if(!foiAlterado){
      this.ts.gerarToast('Nenhuma alteração foi realizada.', false);
    } else if(naoPodemSerAlterados){
      this.ts.gerarToast('Os campos a seguir não podem ser alterados: valor, cobrança, movimento e validade início!', false);
    } else {
      await firstValueFrom(this.rs.updateRegra(regraForm))
        .then(resp => {
          this.regra = resp.registros[0];
          this.ts.gerarToast(`Regra '${this.regra.descricao}' atualizada com sucesso!`, true);
        })
        .catch(err => {
          console.error('Erro ao atualizar regra: '+err.error.mensagem);
          this.ts.gerarToast('Erro ao atualizar a regra, tente novamente mais tarde.', false);
        })
        .finally(() => this.isLoaded = Utils.VERDADEIRO)
    }
  }

  private criarRegraForm(): RegraFinanceira {

    let regra: RegraFinanceira = {
      id: this.formRegraFinanc.value.id!,
      descricao: this.formRegraFinanc.value.descricao!,
      valor: this.formRegraFinanc.value.valor!,
      tipoCobranca: this.formRegraFinanc.value.tipoCobranca!,
      tipoMovimento: this.formRegraFinanc.value.tipoMovimento!,
      dtInicioValidade: this.formRegraFinanc.value.dtInicioValidade!,
      dtFimValidade: this.formRegraFinanc.value.dtFimValidade!,
      situacao: this.formRegraFinanc.value.situacao!,
      versao: this.formRegraFinanc.value.versao!
    }
    return regra;
  }

  private validarCampoObrigatorio(): void {
    // return (control: AbstractControl): ValidationErrors | null => {
    //   console.log('situacao: ', (control.root as FormGroup).parent?.get("situacao")?.value)
    //   const situacao  = control.parent?.get("situacao")?.value;
    //   const dtFim = control.value;

    //   return Enumeradores.Situacao['INATIVO'].id === situacao && !dtFim ? { dtFimValidade: true } : { dtFimValidade: false };
    // }


  }
}

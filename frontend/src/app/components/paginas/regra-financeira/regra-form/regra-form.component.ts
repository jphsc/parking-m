import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { RegraFinanceira } from 'src/app/models/regra-financeira';
import { LoadingService } from 'src/app/services/loading.service';
import { RegraFinanceiraService } from 'src/app/services/regra-financeira.service';
import { ToastService } from 'src/app/services/toast.service';
import { Acao, Enumeradores, ItemEnum } from 'src/app/utils/helper';

@Component({
  selector: 'app-regra-form',
  templateUrl: './regra-form.component.html',
  styleUrls: ['./regra-form.component.css']
})
export class RegraFormComponent implements OnInit {

  protected isLoaded: boolean = false;
  protected isLoading$: Observable<boolean>;
  protected loadingMessage$: Observable<string>;
  protected listaTpCobranca:ItemEnum[] = [];
  protected listaTpMov:ItemEnum[] = [];
  protected listaSituacao:ItemEnum[] = [];
  protected acao!: Enumeradores;
  protected regra: RegraFinanceira = {} as RegraFinanceira;
  protected formRegraFinanc = this.fb.group({
    id: new FormControl<number | null>(null),
    descricao: new FormControl<string>('TESTE', [Validators.required]),
    valor: new FormControl<number>(10.0, [Validators.required, Validators.min(0.0)]),
    tipoCobranca: new FormControl<any>(1, Validators.required),
    tipoMovimento: new FormControl<any>(1, Validators.required),
    dtInicioValidade: new FormControl<string>('2026-01-29', Validators.required),
    dtFimValidade: new FormControl<string>('2026-01-29', Validators.required),
    situacao: new FormControl<any>(''),
    versao: new FormControl<any | null>(null)
  });

  constructor(private fb: FormBuilder, private rota: ActivatedRoute,
    private rs:RegraFinanceiraService, private ts: ToastService, private ls: LoadingService){
    this.isLoading$ = this.ls.isLoading$;
    this.loadingMessage$ = this.ls.loadingMessage$;
  }

  ngOnInit(): void {
    this.listaSituacao = Object.values(Enumeradores.Situacao);
    this.listaTpCobranca = Object.values(Enumeradores.TipoCobranca);
    this.listaTpMov = Object.values(Enumeradores.TipoMovVeiculo);

    this.acao = this.rota.snapshot.url[0].path == 'cadastrar' ? Acao.CADASTRAR : Acao.EDITAR;

    if(this.acao == Acao.EDITAR){
      let idRegra = Number(this.rota.snapshot.url[1].path);
      this.rs.getRegraById(idRegra)
        .subscribe({
          next:(resp) => {
            this.regra = resp.registros[0];
            this.formRegraFinanc.patchValue(resp.registros[0]);
          },
          error:(err) => {
            console.error('Erro ao obter a regra: '+err.erro.mensagem);
            this.ts.gerarToast('Ocorreu um erro ao carregar a regra, favor, tente novamente mais tarde.', false);
          },
          complete: () => this.isLoaded = true
        })
    } else {
      this.isLoaded = true;
    }
  }

  acaoRegra():void {
    if(this.acao == Acao.EDITAR){
      this.updateRegra();
    } else {
      this.createRegra();
    }
  }

  private createRegra() {
    let regra = this.criarRegraForm();

    this.rs.createRegra(regra)
      .subscribe({
        next:(resp) => {
          this.regra = resp.registros[0];
          this.ts.gerarToast(`Regra '${this.regra.descricao}' criada com sucesso!`, true);
        },
        error:(err) => {
          this.ts.gerarToast('Erro ao criar a regra, tente novamente mais tarde.', false);
          console.error('Erro ao criar a regra: '+err.error.mensagem);
        }
      })
  }

  private updateRegra() {

    let regraForm = this.criarRegraForm();

    // regraForm.situacao = this.listaSituacao.find(sit => sit.descricao === this.formRegraFinanc.get('situacao')?.value)?.id;
    // regraForm.tipoCobranca = this.listaTpCobranca.find(tpCob => tpCob.descricao === this.formRegraFinanc.get('tipoCobranca')?.value)?.id;
    // regraForm.tipoMovimento = this.listaTpMov.find(tpMov => tpMov.descricao === this.formRegraFinanc.get('tipoMovimento')?.value)?.id;

    console.log("regra", this.regra);
    console.log("regraForm", regraForm);

    console.log(regraForm.situacao, regraForm.tipoCobranca, regraForm.tipoMovimento)
    let foiAlterado =
      regraForm.descricao !== this.regra.descricao &&
      regraForm.dtFimValidade !== this.regra.dtFimValidade &&
      regraForm.situacao !== this.regra.situacao;
    let naoPodemSerAlterados =
      regraForm.valor !== this.regra.valor ||
      regraForm.tipoCobranca !== this.regra.tipoCobranca ||
      regraForm.tipoMovimento !== this.regra.tipoMovimento ||
      regraForm.dtInicioValidade !== this.regra.dtInicioValidade;

    if(foiAlterado){
      this.ts.gerarToast('Nenhuma alteração foi realizada.', false);
      return;
    } else if(naoPodemSerAlterados){
      this.ts.gerarToast('Os campos a seguir não podem ser alterados: valor, cobrança, movimento e validade início!', false);
      return;
    } else {
      this.rs.updateRegra(regraForm)
        .subscribe({
          next:(resp) => {
            this.regra = resp.registros[0];
            this.ts.gerarToast(`Regra '${this.regra.descricao}' atualizada com sucesso!`, true);
          },
          error:(err) => {
            console.error('Erro ao atualizar regra: '+err.error.mensagem);
            this.ts.gerarToast('Erro ao atualizar a regra, tente novamente mais tarde.', false);
          }
        })
    }
  }

  private criarRegraForm():RegraFinanceira {
    let regra:RegraFinanceira = {
      id: this.formRegraFinanc.value.id!,
      descricao: this.formRegraFinanc.value.descricao!,
      dtInicioValidade: this.formRegraFinanc.value.dtInicioValidade!,
      dtFimValidade: this.formRegraFinanc.value.dtFimValidade!,
      situacao: this.formRegraFinanc.value.situacao!,
      tipoCobranca: this.formRegraFinanc.value.tipoCobranca!,
      tipoMovimento: this.formRegraFinanc.value.tipoMovimento!,
      valor: this.formRegraFinanc.value.valor!,
      versao: this.formRegraFinanc.value.versao!,
    }

    return regra;
  }
}

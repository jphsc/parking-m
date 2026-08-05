import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router, UrlSegment } from '@angular/router';
import { Observable } from 'rxjs';
import { Erro } from 'src/app/models/erro.model';
import { RespostaReqBackend } from 'src/app/models/resposta.model';
import { Veiculo } from 'src/app/models/veiculo.model';
import { LoadingService } from 'src/app/services/loading.service';
import { ToastService } from 'src/app/services/toast.service';
import { VeiculoService } from 'src/app/services/veiculo.service';
import { Acao, Enumeradores } from 'src/app/utils/helper';
import { Utils } from 'src/app/utils/util';

@Component({
  selector: 'app-veiculo-form',
  templateUrl: './veiculo-form.component.html',
  styleUrls: ['./veiculo-form.component.css']
})
export class VeiculoFormComponent implements OnInit {

  protected acao!: Enumeradores;
  protected veiculo: Veiculo = {} as Veiculo;
  protected isLoading$: Observable<boolean>;
  protected loadingMessage$: Observable<string>;
  protected isLoaded: boolean = Utils.FALSO;

  protected formVeiculo = this.fb.group({
    id: new FormControl<number | null>(null),
    montadora: new FormControl<string | null>('GM', [Validators.required, Validators.minLength(2)]),
    modelo: new FormControl<string>('Onix', [Validators.required, Validators.minLength(3)]),
    placa: new FormControl<string>('ECR5B01', [Validators.required, Validators.minLength(7)]),
    dtRegistro: new FormControl<string | null>(null),
    versao: new FormControl<string | null>(null)
  });

  constructor(private fb: FormBuilder, private vs: VeiculoService, private ts: ToastService
    , private rotaAct: ActivatedRoute, private ls: LoadingService, private rota: Router
  ) {
    this.isLoading$ = this.ls.isLoading$;
    this.loadingMessage$ = this.ls.loadingMessage$;
  }

  ngOnInit(): void {

    let url: UrlSegment[] = this.rotaAct.snapshot.url;
    this.acao = url[0].path.toLowerCase() === 'cadastrar' ? Acao.CADASTRAR : Acao.EDITAR;

    if(this.acao === Acao.EDITAR) {
      let idVeiculo = Number(url[1].path);
      this.getVeiculoById(idVeiculo);
    } else {
      this.isLoaded = Utils.VERDADEIRO;
    }

    this.formVeiculo.get("placa")?.valueChanges.subscribe(placa => {
      if(!placa) return;

      this.formVeiculo.get("placa")?.setValue(Utils.formatarPlaca(placa), { emitEvent: Utils.FALSO });
    });

    this.formVeiculo.get("modelo")?.valueChanges.subscribe(modelo => {
      if(!modelo) return;

      this.formVeiculo.get("modelo")?.setValue(Utils.uppercaseOnly(modelo), {emitEvent: Utils.FALSO})
    });

    this.formVeiculo.get("montadora")?.valueChanges.subscribe(montadora => {
      if(!montadora) return;

      this.formVeiculo.get("montadora")?.setValue(Utils.uppercaseOnly(montadora), {emitEvent: Utils.FALSO})
    });
  }

  acaoVeiculo(): void {
    this.isLoaded = Utils.FALSO;
    if(this.acao === Acao.CADASTRAR) {
      this.criarVeiculo();
    } else {
      this.editarVeiculo();
    }
  }

  criarVeiculo(): void {
    let veiculo: Veiculo = {
      placa: this.formVeiculo.value.placa!,
      modelo: this.formVeiculo.value.modelo!,
      montadora: this.formVeiculo.value.montadora!,
      dtRegistro: new Date().toISOString(),
      versao: new Date().toISOString()
    };

    this.vs.createVeiculo(veiculo).subscribe({
      next: (resp: RespostaReqBackend<Veiculo>) => {
        this.ts.gerarToast(Utils.SUCESSO_CRIAR_VEICULO + resp.registros[0].placa, Utils.VERDADEIRO);
        this.formVeiculo.reset();
        this.isLoaded = Utils.VERDADEIRO;
      }
      , error: (err: any) => {
        let mensagem = Utils.getMensagemErro(err, Utils.ERRO_GERAL_PADRAO);
        console.error(err.error.mensagem);
        this.ts.gerarToast(mensagem, Utils.FALSO);
        this.isLoaded = Utils.VERDADEIRO;
      }
    });
  }

  editarVeiculo(): void {
    let veiculoForm: Veiculo = {
      id: this.formVeiculo.value.id!,
      placa: this.formVeiculo.value.placa!,
      modelo: this.formVeiculo.value.modelo!,
      montadora: this.formVeiculo.value.montadora!,
      dtRegistro: this.veiculo.dtRegistro,
      versao: this.veiculo.versao
    }

    const alterado: boolean =
      this.veiculo.placa !== veiculoForm.placa ||
      this.veiculo.modelo !== veiculoForm.modelo ||
      this.veiculo.montadora !== veiculoForm.montadora;

    if(!alterado) {
      this.ts.gerarToast('Nenhum dado foi alterado.', Utils.FALSO);
    } else {
      this.vs.updateVeiculo(veiculoForm).subscribe({
        next: (resp: RespostaReqBackend<Veiculo>) => {
          this.veiculo = resp.registros[0];
          this.ts.gerarToast(`Veículo de placa ${resp.registros[0].placa} alterado com sucesso!`, Utils.VERDADEIRO);
          this.rota.navigate(['/veiculo/listar']);
          this.isLoaded = Utils.VERDADEIRO;
        }
        , error: (err) => {
          let mensagem = Utils.getMensagemErro(err, Utils.ERRO_ATUALIZAR_REGISTRO);
          console.error(mensagem);
          this.ts.gerarToast(mensagem, Utils.FALSO);
          this.isLoaded = Utils.VERDADEIRO;
        }
      })
    }
  }

  private getVeiculoById(id: number): void {
     this.vs.getVeiculoById(id).subscribe({
        next: (resp: RespostaReqBackend<Veiculo>) => {
          this.veiculo = resp.registros[0];
          this.formVeiculo.patchValue(resp.registros[0]);
          this.isLoaded = Utils.VERDADEIRO;
        }
        , error: (err) => {
          console.error(err.error.mensagem);
          this.ts.gerarToast(err.error.mensagem, Utils.FALSO);
          this.isLoaded = Utils.VERDADEIRO;
        }
      })
  }
}

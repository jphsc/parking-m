import { AfterContentInit, Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { firstValueFrom, Observable } from 'rxjs';
import { MovimentoVeiculo } from 'src/app/models/movimento-veiculo.model';
import { RegraFinanceira } from 'src/app/models/regra-financeira';
import { RespostaReqBackend } from 'src/app/models/resposta.model';
import { Veiculo } from 'src/app/models/veiculo.model';
import { LoadingService } from 'src/app/services/loading.service';
import { MovimentoVeiculoService } from 'src/app/services/movimento-veiculo.service';
import { RegraFinanceiraService } from 'src/app/services/regra-financeira.service';
import { ToastService } from 'src/app/services/toast.service';
import { VeiculoService } from 'src/app/services/veiculo.service';
import { Enumeradores, ItemEnum } from 'src/app/utils/helper';
import { Utils } from 'src/app/utils/util';

@Component({
  selector: 'app-movveiculo-form',
  templateUrl: './movveiculo-form.component.html',
  styleUrls: ['./movveiculo-form.component.css']
})
export class MovveiculoFormComponent implements OnInit, AfterContentInit {

  protected loadingMessage$: Observable<string>;
  protected isLoading$: Observable<boolean>;
  protected isLoaded: boolean = Utils.FALSO;
  protected informarVeiculo!: number;
  protected veiculos!: Veiculo[];
  protected regras!: RegraFinanceira[];
  protected tiposMovimento!: ItemEnum[];
  protected formVeiculo = this.form.group({
    tipoMovimento: new FormControl<number | null>(null, Validators.required),
    regra: new FormControl<number | null>(null, Validators.required),
    placa: new FormControl<string | null>('TPV1E33', [Validators.required, Validators.minLength(7)]),
    modelo: new FormControl<string | null>('EX2 MAX', [Validators.required, Validators.minLength(2)]),
    montadora: new FormControl<string | null>('GEELY', [Validators.required, Validators.minLength(2)])
  })

  constructor(private form: FormBuilder, private ls: LoadingService, private ts:ToastService,
    private vs: VeiculoService, private rfs: RegraFinanceiraService, private mvs: MovimentoVeiculoService) {
      this.isLoading$ = this.ls.isLoading$;
      this.loadingMessage$ = this.ls.loadingMessage$;
    }

  ngAfterContentInit(): void {

  }

  async ngOnInit(): Promise<void> {
    this.regras = [];
    this.tiposMovimento = Object.values(Enumeradores.TipoMovVeiculo);
    this.informarVeiculo = Utils.CRIAR_VEICULO;
    this.validarCamposObrigatorios();

    await this.getRegras();

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

    this.formVeiculo.get("placa")?.valueChanges.subscribe(value => {
      if(!value) return;
      this.formatarPlaca(value.toString());
    })

    this.isLoaded = Utils.VERDADEIRO;
  }

  protected informarVeiculoChange(ev: Event): void {
    const target = ev.target as HTMLInputElement;
    this.informarVeiculo = Number(target.value);
    this.validarCamposObrigatorios();

    if(this.informarVeiculo == Utils.INFORMAR_VEICULO) {
      this.formVeiculo.reset()
    }
    this.isLoaded = Utils.VERDADEIRO;
  }

  private async getRegras(): Promise<void> {
    const resposta = await firstValueFrom(this.rfs.getAllRegras(Utils.PAGINA_UM, Utils.REGISTROS_POR_PAGINA));

    this.regras = resposta.registros
      .filter(regra => regra.situacao == Enumeradores.Situacao['ATIVO'].id)
      .map(regra => {
        const tipoCobranca = Enumeradores.factory("TipoCobranca").getDescricao(regra.tipoCobranca);
        const tipoMovimento = Enumeradores.factory("TipoMovVeiculo").getDescricao(regra.tipoMovimento);
        regra.tipoCobranca = tipoCobranca;
        regra.tipoMovimento = tipoMovimento;
        return regra;
      });
  }

  private async cadastrarVeiculo(): Promise<Veiculo | any> {
    const veiculoForm: Veiculo = this.formVeiculo.value as Veiculo;
    return (await firstValueFrom(this.vs.createVeiculo(veiculoForm))
      .then((resp) => resp.registros[0])
      .catch((err) => {
        console.error('Erro ao cadastrar o veículo');
        return err;
      })
    );
  }

  protected async incluirMovimento(): Promise<void> {
    this.isLoaded = Utils.FALSO;

    let atualFormaInformarVeiculo = this.informarVeiculo;
    let movimento: MovimentoVeiculo = {
      idRegra: this.formVeiculo.get('regra')?.value || undefined,
      dtHrEntrada: new Date().toISOString().substring(0, 16),
      tipoMovimento: this.formVeiculo.get('tipoMovimento')?.value || undefined
    };

    if(this.informarVeiculo == Utils.INFORMAR_VEICULO){

      const veiculo: any = await firstValueFrom(this.vs.getVeiculoByPlaca(this.formVeiculo.get('placa')?.value || null))
        .then((resp) =>  resp.registros[0])
        .catch((resp) => {
            console.error('Erro ao obter o veículo pela placa: '+resp.error.mensagem);
            return resp;
          });

      movimento.idVeiculo = 'id' in veiculo ? veiculo.id : 0
    } else {
      const resp = await this.cadastrarVeiculo();
      if('id' in resp) {
        movimento.idVeiculo = resp.id;
      } else {
        console.error('erro: '+resp.error.mensagem)
        this.isLoaded = Utils.VERDADEIRO;
        this.ts.gerarToast(resp.error.mensagem, Utils.FALSO);
        return;
      }
    }

    await firstValueFrom(this.mvs.criarMovimento(movimento))
      .then((resp) => {
        this.ts.gerarToast(`Movimento incluído com sucesso a placa ${resp.registros[0].placa}`, Utils.VERDADEIRO);
      })
      .catch((e) => {
        console.log(e.error)
        console.error("Erro ao criar o movimento: ", e.error.mensagem)
        this.ts.gerarToast("Não foi possível incluir o movimento, tente novamente mais tarde", Utils.FALSO);
      })
      .finally(() => {
        this.informarVeiculo = atualFormaInformarVeiculo;
        this.isLoaded = Utils.VERDADEIRO;
      })

  }

  private formatarPlaca(value: string): void {
    const placaFormatada = Utils.formatarPlaca(value);
    this.formVeiculo.get("placa")?.setValue(placaFormatada, { emitEvent: Utils.FALSO });
  }

  private validarCamposObrigatorios(): void {

    const placa = this.formVeiculo.get('placa')!;
    const modelo = this.formVeiculo.get('modelo')!;
    const montadora = this.formVeiculo.get('montadora')!;

    if(this.informarVeiculo == Utils.INFORMAR_VEICULO){
      modelo.clearValidators();
      montadora.clearValidators();
    } else {
      modelo.setValidators([Validators.required,Validators.minLength(2)]);
      montadora.setValidators([Validators.required,Validators.minLength(2)]);

    }
    placa.updateValueAndValidity();
    modelo.updateValueAndValidity();
    montadora.updateValueAndValidity();
  }
}

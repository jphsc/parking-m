import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
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
  protected isLoaded:boolean = false;

  protected formVeiculo = this.fb.group({
    id: new FormControl<number | null>(null),
    montadora: new FormControl<string | null>('GM', [Validators.required, Validators.minLength(3)]),
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

    this.acao = this.rotaAct.snapshot.url[0].path === 'cadastrar' ? Acao.CADASTRAR : Acao.EDITAR;

    if(this.acao === Acao.EDITAR) {
      let idVeiculo = Number(this.rotaAct.snapshot.url[1].path);
      this.getVeiculoById(idVeiculo);
    } else {
      this.isLoaded = true;
    }

    this.formVeiculo.get("placa")?.valueChanges
    .subscribe(placa => {
      if(!placa) return;

      this.formVeiculo.get("placa")?.setValue(Utils.formatarPlaca(placa), { emitEvent: false });
    });

    this.formVeiculo.get("modelo")?.valueChanges.subscribe(modelo => {
      if(!modelo) return;

      this.formVeiculo.get("modelo")?.setValue(Utils.uppercaseOnly(modelo), {emitEvent: false})
    });

    this.formVeiculo.get("montadora")?.valueChanges.subscribe(montadora => {
      if(!montadora) return;

      this.formVeiculo.get("montadora")?.setValue(Utils.uppercaseOnly(montadora), {emitEvent: false})
    });
  }

  acaoVeiculo(): void {
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
      next: (resp) => {
        this.ts.gerarToast(`Veículo criado com a placa: ${resp.registros[0].placa}`, true);
        this.formVeiculo.reset();
      },
      error: (err) => {
        console.error(err.error.mensagem);
        this.ts.gerarToast(err.error.mensagem, false);
      }, complete: () => {
        this.isLoaded = true;
      }
    });
  }

  editarVeiculo():void {
    let veiculoForm:Veiculo = {
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
      this.ts.gerarToast('Nenhum dado foi alterado.', false);
    } else {
      this.vs.updateVeiculo(veiculoForm).subscribe({
        next: (resp) => {
          this.veiculo = resp.registros[0];
          this.ts.gerarToast(`Veículo de placa ${resp.registros[0].placa} alterado com sucesso!`, true);
          this.rota.navigate(['/veiculo/listar']);
        },
        error: (err) => {
            console.log('Erro ao atualizar o veículo: '+ err.error.mensagem);
            this.ts.gerarToast('Erro ao atualizar o veículo: '+ err.error.mensagem, false)
        },
      })
    }
  }

  private getVeiculoById(id: number):void {
     this.vs.getVeiculoById(id)
      .subscribe({
        next: (resp) => {
          this.veiculo = resp.registros[0];
          this.formVeiculo.patchValue(resp.registros[0]);
        },
        error: (err) => {
          console.error(err.error.mensagem);
          this.ts.gerarToast(err.error.mensagem, false);
        }, complete: () => {
          this.isLoaded = true;
        }
      })
  }
}

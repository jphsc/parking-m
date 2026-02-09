import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { MovimentoVeiculo } from 'src/app/models/movimento-veiculo.model';
import { RespostaReqBackend } from 'src/app/models/resposta.model';
import { LoadingService } from 'src/app/services/loading.service';
import { MovimentoVeiculoService } from 'src/app/services/movimento-veiculo.service';
import { ToastService } from 'src/app/services/toast.service';
import { Enumeradores } from 'src/app/utils/helper';
import { Utils } from 'src/app/utils/util';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  placa: string = '';
  movAbertos: MovimentoVeiculo[] = [];
  isLoading$: Observable<boolean>;
  loadingMessage$: Observable<string>;
  isLoaded: boolean = false;
  placaForm = new FormGroup({ placaInput: new FormControl('', [Validators.required, Validators.minLength(7)]) });

  constructor(private mvs: MovimentoVeiculoService, private loadingService: LoadingService,
    private rota: Router, private ts:ToastService) {
    this.isLoading$ = this.loadingService.isLoading$;
    this.loadingMessage$ = this.loadingService.loadingMessage$;
  }

  ngOnInit(): void {
    this.mvs.getMovimentosAbertos().subscribe({
      next: (resp: RespostaReqBackend<MovimentoVeiculo>) => {
        resp.registros.forEach(mv => {
          const tipoMov:string = Enumeradores.factory('TipoMovVeiculo').getDescricao(mv.tipoMovimento);
          const situacaoMov:string = Enumeradores.factory('SituacaoMovimento').getDescricao(mv.situacao);

          mv.tipoMovimento = tipoMov;
          mv.situacao = situacaoMov;

          this.movAbertos.push(mv);
          });
        this.isLoaded = true;
      },
      error: (err) => {
        console.error('Erro ao carregar movimentos abertos:', err.error.mensagem);
        this.ts.gerarToast("Não foi possível carregar os movimentos, tente novamente mais tarde", false);
        this.isLoaded = true;
      }
    });

    this.placaForm.get("placaInput")?.valueChanges.subscribe(value => {
      if(!value) return;

      this.formatarPlaca(value);
    })
  }

  protected gerarMovimentoAvulso(): void {
    console.log(this.placaForm.value);
  }

  protected detalharMovimento(movId: any): void {
    this.rota.navigate([`/movimento/detalhe/${movId}`]);
  }

  protected finalizarMovimento(movId: any): void {
    this.rota.navigate([`/movimento/finalizar/${movId}`]);
  }

  public formatarPlaca(value: string): void {
    const placaFormatada = Utils.formatarPlaca(value);
    this.placaForm.get("placaInput")?.setValue(placaFormatada, { emitEvent: false });
  }
}

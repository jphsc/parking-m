import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { catchError, finalize, map, Observable, of, tap } from 'rxjs';
import { MovimentoEncerrar } from 'src/app/models/movimento-encerrar-model';
import { MovimentoVeiculo } from 'src/app/models/movimento-veiculo.model';
import { RespostaReqBackend } from 'src/app/models/resposta.model';
import { LoadingService } from 'src/app/services/loading.service';
import { MovimentoVeiculoService } from 'src/app/services/movimento-veiculo.service';
import { ToastService } from 'src/app/services/toast.service';
import { Enumeradores } from 'src/app/utils/helper';
import { Utils } from 'src/app/utils/util';

@Component({
  selector: 'app-movveiculo-detalhe',
  templateUrl: './movveiculo-detalhe.component.html',
  styleUrls: ['./movveiculo-detalhe.component.css']
})
export class MovveiculoDetalheComponent implements OnInit {

  protected movimento!: MovimentoVeiculo;
  protected isTrueFalse: boolean = Utils.FALSO;
  protected isLoading$: Observable<boolean>;
  protected loadingMessage$: Observable<string>;
  protected isLoaded: boolean = Utils.FALSO;
  protected idMovimento: number = 0;
  private   idSituacao: number = 12;

  constructor(private mvs:MovimentoVeiculoService, private rota: ActivatedRoute
    , private ls:LoadingService, private ts:ToastService
  ){
    this.isLoading$ = this.ls.isLoading$;
    this.loadingMessage$ = this.ls.loadingMessage$;
  }

  ngOnInit(): void {
    this.idMovimento = Number(this.rota.snapshot.paramMap.get("id"));
    this.getMovimento();
  }

  protected finalizarMovimento(): void {

    this.getMovimento();

    let movimentoEncerrar: MovimentoEncerrar = {
      idMovimento: this.movimento.idMovimento!,
      idRegra: this.movimento.idRegra!,
      dtHrSaida: new Date().toISOString().substring(0, 16)
    }

    this.mvs.fecharMovimento(movimentoEncerrar).subscribe({
      next: (resp: RespostaReqBackend<MovimentoVeiculo>) => {
        this.ts.gerarToast(`Movimento de placa ${resp.registros[0].placa} finalizado com sucesso!`, Utils.VERDADEIRO);
        this.isTrueFalse = Utils.VERDADEIRO;
      }
      , error: err => {
        console.error('Erro ao finalizar o movimento: ', err);
        this.ts.gerarToast("Não foi possível finalizar o movimento, tente novamente mais tarde", Utils.FALSO);
      }
    })
  }

  private getMovimento(): void {
    this.mvs.getMovimentoPorId(this.idMovimento)
    .pipe(
      tap((resp: RespostaReqBackend<MovimentoVeiculo>) => {
        this.isTrueFalse = resp.registros[0].situacao == this.idSituacao ? Utils.FALSO : Utils.VERDADEIRO;
      })
      , map((resp: RespostaReqBackend<MovimentoVeiculo>) => {
        resp.registros.forEach(mv => {
          this.idSituacao = mv.situacao;
          let situacao = Enumeradores.factory('SituacaoMovimento').getDescricao(mv.situacao);
          let tipoMovimento = Enumeradores.factory('TipoMovVeiculo').getDescricao(mv.tipoMovimento);

          mv.situacao = situacao;
          mv.tipoMovimento = tipoMovimento;
          this.movimento = mv;
        })
      })
        , catchError(err => {
          console.log('Erro ao obter os movimentos de veículos: ', err.error.mensagem);
          this.ts.gerarToast(err.error.mensagem, Utils.FALSO);
          this.isLoaded = Utils.FALSO;
          return of(null);
        })
    )
    .subscribe((mv)=> {
      mv ? this.movimento = mv : null;
      this.isLoaded = Utils.VERDADEIRO;
    })
  }
}

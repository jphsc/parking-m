import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { MovimentoVeiculo } from 'src/app/models/movimento-veiculo.model';
import { RespostaReqBackend } from 'src/app/models/resposta.model';
import { LoadingService } from 'src/app/services/loading.service';
import { MovimentoVeiculoService } from 'src/app/services/movimento-veiculo.service';
import { Enumeradores } from 'src/app/utils/helper';

@Component({
  selector: 'app-movveiculo-detalhe',
  templateUrl: './movveiculo-detalhe.component.html',
  styleUrls: ['./movveiculo-detalhe.component.css']
})
export class MovveiculoDetalheComponent implements OnInit {

  protected movimento:MovimentoVeiculo = {};
  protected isTrueFalse:boolean = false;
  protected isLoading$: Observable<boolean>;
  protected loadingMessage$: Observable<string>;
  protected isLoaded: boolean = false;

  constructor(private mvs:MovimentoVeiculoService, private rota: ActivatedRoute
    , private ls:LoadingService
  ){
    this.isLoading$ = this.ls.isLoading$;
    this.loadingMessage$ = this.ls.loadingMessage$;
  }

  ngOnInit(): void {
    let idMovVeiculo = Number(this.rota.snapshot.paramMap.get("id"));
    let paginaAcao = this.rota.snapshot.url[0].path;
    this.isTrueFalse = (paginaAcao === 'detalhe') ? true : false;

    this.mvs.getMovimentoById(idMovVeiculo)
      .subscribe({
        next: (resp) => {
          resp.registros.forEach((mv) => {
            let situacao = Enumeradores.factory('SituacaoMovimento').getDescricao(mv.situacao);
            let tipoMovimento = Enumeradores.factory('TipoMovVeiculo').getDescricao(mv.tipoMovimento);

            mv.situacao = situacao;
            mv.tipoMovimento = tipoMovimento;
            this.movimento = mv;
          })
        },
        error: (err) => {
          console.log('Erro ao obter os movimentos de veículos: ', err.error.mensagem);
        },
        complete: ()=>{
          this.isLoaded = true;
        }
      }
    );
  }

}

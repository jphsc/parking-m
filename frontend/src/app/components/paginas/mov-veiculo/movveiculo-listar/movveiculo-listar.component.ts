import { Component, OnInit } from '@angular/core';
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
  selector: 'app-movveiculo-listar',
  templateUrl: './movveiculo-listar.component.html',
  styleUrls: ['./movveiculo-listar.component.css']
})
export class MovveiculoListarComponent implements OnInit {

  paginaAtual: number = Utils.PAGINA_UM;
  movimentos:MovimentoVeiculo[] = [];
  isLoading$: Observable<boolean>;
  loadingMessage$: Observable<string>;
  isLoaded: boolean = Utils.FALSO;
  paginar: boolean = Utils.FALSO;
  qtdPaginas: number[] = [];

  constructor(private mvs: MovimentoVeiculoService, private rota: Router
    , private ls: LoadingService, private ts: ToastService){
      this.isLoading$ = this.ls.isLoading$;
      this.loadingMessage$ = this.ls.loadingMessage$;
    }

  ngOnInit(): void {
    this.getAllMovimentos(this.paginaAtual);
  }

  private getAllMovimentos(pagina: number):void {

    this.movimentos = [];
    this.paginaAtual = pagina;
    this.isLoaded = Utils.FALSO;

    this.mvs.getAllMovimentos(pagina).subscribe({
        next: (resp: RespostaReqBackend<MovimentoVeiculo>) => {
          resp.registros.forEach(mv => {
            let tipoMovimento = Enumeradores.factory('TipoMovVeiculo').getDescricao(mv.tipoMovimento);
            let situacao = Enumeradores.factory('SituacaoMovimento').getDescricao(mv.situacao);

            mv.tipoMovimento = tipoMovimento;
            mv.situacao = situacao;

            this.movimentos.push(mv);
          });

          this.paginar = Utils.paginarRegistros(resp.quantidade, Utils.REGISTROS_POR_PAGINA);
          this.qtdPaginas = Utils.obterQtdPaginas(resp.quantidade, Utils.REGISTROS_POR_PAGINA);
          this.isLoaded = Utils.VERDADEIRO;
        }
        , error: (err) => {
          console.error('Erro ao carregar movimentos: ', err);
          this.ts.gerarToast("Não foi possível carregar os movimentos, tente novamente mais tarde", false);
          this.isLoaded = Utils.VERDADEIRO;
        }
      }
    )
  }

  detalharMovimento(id: any) {
    this.rota.navigate([`/movimento/detalhe/${id}`]);
  }

  finalizarMovimento(id: any) {
    this.rota.navigate([`/movimento/finalizar/${id}`]);
  }

  public paginarRegistros(pagina: number): void{
    this.getAllMovimentos(pagina);
  }

}

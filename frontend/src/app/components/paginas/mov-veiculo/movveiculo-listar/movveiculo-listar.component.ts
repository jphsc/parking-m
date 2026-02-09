import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { MovimentoVeiculo } from 'src/app/models/movimento-veiculo.model';
import { RespostaReqBackend } from 'src/app/models/resposta.model';
import { LoadingService } from 'src/app/services/loading.service';
import { MovimentoVeiculoService } from 'src/app/services/movimento-veiculo.service';
import { ToastService } from 'src/app/services/toast.service';
import { Enumeradores } from 'src/app/utils/helper';

@Component({
  selector: 'app-movveiculo-listar',
  templateUrl: './movveiculo-listar.component.html',
  styleUrls: ['./movveiculo-listar.component.css']
})
export class MovveiculoListarComponent implements OnInit {

  movimentos:MovimentoVeiculo[] = [];
  isLoading$: Observable<boolean>;
  loadingMessage$: Observable<string>;
  isLoaded: boolean = false;

  constructor(private mvs: MovimentoVeiculoService, private rota: Router
    , private ls: LoadingService, private ts: ToastService){
      this.isLoading$ = this.ls.isLoading$;
      this.loadingMessage$ = this.ls.loadingMessage$;
    }

  ngOnInit(): void {
    this.getAllMovimentos();
  }

  private async getAllMovimentos():Promise<void> {
    this.mvs.getAllMovimentos().subscribe({
        next: (resp: RespostaReqBackend<MovimentoVeiculo>) => {
          resp.registros.forEach(mv => {
            let tipoMovimento = Enumeradores.factory('TipoMovVeiculo').getDescricao(mv.tipoMovimento);
            let situacao = Enumeradores.factory('SituacaoMovimento').getDescricao(mv.situacao);

            mv.tipoMovimento = tipoMovimento;
            mv.situacao = situacao;

            this.movimentos.push(mv);
          });
        this.isLoaded = true;
        },
        error: (err) => {
          console.error('Erro ao carregar movimentos:', err.error.mensagem);
          this.ts.gerarToast("Não foi possível carregar os veículos, tente novamente mais tarde", false);
          this.isLoaded = true;
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

}

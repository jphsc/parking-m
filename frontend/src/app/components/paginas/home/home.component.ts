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

  private readonly qtdRegistrosPorPagina: number = 4;
  private loadingMessage$: Observable<string>;
  paginar = Utils.FALSO;
  paginaAtual: number = Utils.PAGINA_UM;
  qtdPaginas: number[] = [];
  movAbertos: MovimentoVeiculo[] = [];
  isLoading$: Observable<boolean>;
  isLoaded: boolean = Utils.FALSO;
  placaForm = new FormGroup({ placaInput: new FormControl('', [Validators.required, Validators.minLength(7)]) });

  constructor(private mvs: MovimentoVeiculoService, private loadingService: LoadingService,
    private rota: Router, private ts:ToastService) {
    this.isLoading$ = this.loadingService.isLoading$;
    this.loadingMessage$ = this.loadingService.loadingMessage$;
  }

  ngOnInit(): void {
    this.getMovimentosAbertos(Utils.PAGINA_UM);

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
    this.placaForm.get("placaInput")?.setValue(placaFormatada, { emitEvent: Utils.FALSO });
  }

  private getMovimentosAbertos(pagina: number): void {
    this.isLoaded = Utils.FALSO;
    this.movAbertos = [];
    this.paginaAtual = pagina;

    this.mvs.getMovimentosAbertos(pagina, this.qtdRegistrosPorPagina).subscribe({
      next: (resp: RespostaReqBackend<MovimentoVeiculo>) => {
        resp.registros.forEach(mv => {
          const tipoMov:string = Enumeradores.factory('TipoMovVeiculo').getDescricao(mv.tipoMovimento);
          const situacaoMov:string = Enumeradores.factory('SituacaoMovimento').getDescricao(mv.situacao);

          mv.tipoMovimento = tipoMov;
          mv.situacao = situacaoMov;

          this.movAbertos.push(mv);
        });

        this.qtdPaginas = Utils.obterQtdPaginas(resp.quantidade, this.qtdRegistrosPorPagina);
        this.paginar = Utils.paginarRegistros(resp.quantidade, this.qtdRegistrosPorPagina);
        this.isLoaded = Utils.VERDADEIRO;
      }
      , error: (err) => {
        console.error('Erro ao carregar movimentos abertos:', err.error.mensagem);
        this.ts.gerarToast("Não foi possível carregar os movimentos, tente novamente mais tarde", Utils.FALSO);
        this.paginar = Utils.FALSO;
        this.isLoaded = Utils.VERDADEIRO;
      }
    });
  }

  protected paginarRegistros(pagina: number): void {
    this.getMovimentosAbertos(pagina);
  }
}

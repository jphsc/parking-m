import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { RegraFinanceira } from 'src/app/models/regra-financeira';
import { RespostaReqBackend } from 'src/app/models/resposta.model';
import { LoadingService } from 'src/app/services/loading.service';
import { RegraFinanceiraService } from 'src/app/services/regra-financeira.service';
import { ToastService } from 'src/app/services/toast.service';
import { Enumeradores } from 'src/app/utils/helper';
import { Utils } from 'src/app/utils/util';

@Component({
  selector: 'app-regra-listar',
  templateUrl: './regra-listar.component.html',
  styleUrls: ['./regra-listar.component.css']
})
export class RegraListarComponent implements OnInit {

  regras: RegraFinanceira[] = [];
  isLoading$: Observable<boolean>;
  loadingMessage$: Observable<string>;
  isLoaded: boolean = Utils.FALSO;
  paginaAtual: number = Utils.PAGINA_UM;
  paginar: boolean = Utils.FALSO;
  qtdPaginas: number[] = [];
  private readonly REGISTROS_POR_PAGINA = 5;

  constructor(private regraFinService: RegraFinanceiraService, private ls: LoadingService
    , private rota: Router, private ts:ToastService){
    this.isLoading$ = this.ls.isLoading$;
    this.loadingMessage$ = this.ls.loadingMessage$;
  }

  ngOnInit(): void {
    this.getAllRegras(this.paginaAtual);
  }

  private getAllRegras(pagina: number): void {

    this.isLoaded = Utils.FALSO;
    this.regras = [];
    this.paginaAtual = pagina;

    this.regraFinService.getAllRegras(pagina, this.REGISTROS_POR_PAGINA).subscribe({
      next: (resp: RespostaReqBackend<RegraFinanceira>) => {

        resp.registros.forEach(rf => {
          rf.tipoCobranca = Enumeradores.factory('TipoCobranca').getDescricao(rf.tipoCobranca);
          rf.tipoMovimento = Enumeradores.factory('TipoMovVeiculo').getDescricao(rf.tipoMovimento);
          rf.situacao = Enumeradores.factory('Situacao').getDescricao(rf.situacao);

          this.regras.push(rf);
        });

        this.paginar = Utils.paginarRegistros(resp.quantidade, this.REGISTROS_POR_PAGINA);
        this.qtdPaginas = Utils.obterQtdPaginas(resp.quantidade, this.REGISTROS_POR_PAGINA);
        this.isLoaded = Utils.VERDADEIRO;
      }
      , error: (err) => {
        console.error('Erro ao carregar regras financeiras:', err);
        this.ts.gerarToast("Não foi possível carregar as regras financeiras, tente novamente mais tarde", Utils.FALSO);
        this.isLoaded = Utils.VERDADEIRO;
      }
    });
  }

  navEditarRegra(id: any){
    this.rota.navigate([`/regra/editar/${id}`]);
  }

  protected paginarRegistros(pagina: number): void {
    this.getAllRegras(pagina);
  }
}

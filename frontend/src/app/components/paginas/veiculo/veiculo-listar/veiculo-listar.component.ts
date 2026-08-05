import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { RespostaReqBackend } from 'src/app/models/resposta.model';
import { Veiculo } from 'src/app/models/veiculo.model';
import { LoadingService } from 'src/app/services/loading.service';
import { ToastService } from 'src/app/services/toast.service';
import { VeiculoService } from 'src/app/services/veiculo.service';
import { Utils } from 'src/app/utils/util';

@Component({
  selector: 'app-veiculo-listar',
  templateUrl: './veiculo-listar.component.html',
  styleUrls: ['./veiculo-listar.component.css']
})
export class VeiculoListarComponent implements OnInit {

  protected veiculos:Veiculo[] = [];
  protected isLoading$: Observable<boolean>;
  protected loadingMessage$: Observable<string>;
  protected isLoaded: boolean = Utils.FALSO;
  paginar: boolean = Utils.FALSO;
  qtdPaginas: number[] = [];
  paginaAtual: number = Utils.PAGINA_UM;

  constructor(private vs: VeiculoService, private rota:Router, private ls: LoadingService,
    private ts:ToastService){
    this.isLoading$ = this.ls.isLoading$;
    this.loadingMessage$ = this.ls.loadingMessage$;
  }

  ngOnInit():void {
    this.getVeiculos(this.paginaAtual);
  }

  navEditarVeiculo(id:any):void {
    this.rota.navigate([`/veiculo/editar/${id}`])
  }

  private getVeiculos(pagina: number):void {
    this.isLoaded = Utils.FALSO;
    this.veiculos = [];
    this.paginaAtual = pagina;

    this.vs.getVeiculos(pagina).subscribe({
      next: (resp: RespostaReqBackend<Veiculo>) => {
        this.veiculos = resp.registros;
        this.qtdPaginas = Utils.obterQtdPaginas(resp.quantidade, Utils.REGISTROS_POR_PAGINA);
        this.paginar = Utils.paginarRegistros(resp.quantidade, Utils.REGISTROS_POR_PAGINA);
        this.isLoaded = Utils.VERDADEIRO;
      }
      , error: (err) => {
        console.error('Erro ao carregar veículos, código: ', err.error.codigo);
        this.ts.gerarToast(Utils.ERRO_PADRAO_CARREGAR_DADOS, false);
        this.paginar = Utils.FALSO;
        this.isLoaded = Utils.VERDADEIRO;
      }
    });
  }

  public paginarRegistros(pagina: number): void {
    this.getVeiculos(pagina);
  }
}

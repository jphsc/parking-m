import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { RegraFinanceira } from 'src/app/models/regra-financeira';
import { LoadingService } from 'src/app/services/loading.service';
import { RegraFinanceiraService } from 'src/app/services/regra-financeira.service';
import { ToastService } from 'src/app/services/toast.service';
import { Enumeradores } from 'src/app/utils/helper';

@Component({
  selector: 'app-regra-listar',
  templateUrl: './regra-listar.component.html',
  styleUrls: ['./regra-listar.component.css']
})
export class RegraListarComponent implements OnInit {

  regras: RegraFinanceira[] = [];
  isLoading$: Observable<boolean>;
  loadingMessage$: Observable<string>;
  isLoaded: boolean = false;

  constructor(private regraFinService: RegraFinanceiraService, private ls: LoadingService
    , private rota: Router, private ts:ToastService){
    this.isLoading$ = this.ls.isLoading$;
    this.loadingMessage$ = this.ls.loadingMessage$;
  }

  ngOnInit(): void {
    this.getAllRegras();
  }

  getAllRegras(): void {
    this.regraFinService.getAllRegras().subscribe({
      next: (resp) => {
        resp.registros.forEach(rf => {
          rf.tipoCobranca = Enumeradores.factory('TipoCobranca').getDescricao(rf.tipoCobranca);
          rf.tipoMovimento = Enumeradores.factory('TipoMovVeiculo').getDescricao(rf.tipoMovimento);
          rf.situacao = Enumeradores.factory('Situacao').getDescricao(rf.situacao);

          this.regras.push(rf);
        });
        this.isLoaded = true;
      },
      error: (err) => {
        console.error('Erro ao carregar regras financeiras:', err.error.mensagem);
        console.error(err);
        this.ts.gerarToast("Não foi possível carregar as regras financeiras, tente novamente mais tarde", false);
        this.isLoaded = true;
      }
    });
  }

  navEditarRegra(id: any){
    this.rota.navigate([`/regra/editar/${id}`]);
  }
}

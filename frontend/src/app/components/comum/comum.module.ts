import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CardLateralComponent } from './card-lateral/card-lateral.component';
import { CardCabecalhoComponent } from './card-cabecalho/card-cabecalho.component';
import { LoadingComponent } from './loading/loading.component';
import { RouterModule } from '@angular/router';
import { PaginacaoComponent } from './paginacao/paginacao.component';


@NgModule({
  declarations: [
    CardLateralComponent,
    CardCabecalhoComponent,
    LoadingComponent,
    PaginacaoComponent
  ],
  imports: [
    CommonModule,
    RouterModule
  ],
  exports: [
    CardCabecalhoComponent,
    CardLateralComponent,
    LoadingComponent,
    PaginacaoComponent
  ]
})
export class ComumModule { }

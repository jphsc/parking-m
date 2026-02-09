import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CardLateralComponent } from './card-lateral/card-lateral.component';
import { CardCabecalhoComponent } from './card-cabecalho/card-cabecalho.component';
import { LoadingComponent } from './loading/loading.component';
import { RouterModule } from '@angular/router';


@NgModule({
  declarations: [
    CardLateralComponent,
    CardCabecalhoComponent,
    LoadingComponent
  ],
  imports: [
    CommonModule,
    RouterModule
  ],
  exports: [
    CardCabecalhoComponent,
    CardLateralComponent,
    LoadingComponent
  ]
})
export class ComumModule { }

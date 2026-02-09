import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RegraFinanceiraRoutingModule } from './regra-financeira-routing.module';
import { RegraListarComponent } from './regra-listar/regra-listar.component';
import { RegraFormComponent } from './regra-form/regra-form.component';
import { RegraDetalheComponent } from './regra-detalhe/regra-detalhe.component';
import { ComumModule } from '../../comum/comum.module';
import { ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    RegraListarComponent,
    RegraFormComponent,
    RegraDetalheComponent
  ],
  imports: [
    CommonModule,
    RegraFinanceiraRoutingModule,
    ReactiveFormsModule,
    ComumModule
  ]
})
export class RegrafFinanceiraModule { }

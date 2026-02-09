import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MovVeiculoRoutingModule } from './mov-veiculo-routing.module';
import { MovveiculoListarComponent } from './movveiculo-listar/movveiculo-listar.component';
import { MovveiculoFormComponent } from './movveiculo-form/movveiculo-form.component';
import { MovveiculoDetalheComponent } from './movveiculo-detalhe/movveiculo-detalhe.component';
import { ComumModule } from '../../comum/comum.module';


@NgModule({
  declarations: [
    MovveiculoListarComponent,
    MovveiculoFormComponent,
    MovveiculoDetalheComponent
  ],
  imports: [
    CommonModule,
    MovVeiculoRoutingModule,
    ComumModule
  ]
})
export class MovVeiculoModule { }

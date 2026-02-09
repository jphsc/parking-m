import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { VeiculoRoutingModule } from './veiculo-routing.module';
import { VeiculoListarComponent } from './veiculo-listar/veiculo-listar.component';
import { VeiculoFormComponent } from './veiculo-form/veiculo-form.component';
import { VeiculoDetalheComponent } from './veiculo-detalhe/veiculo-detalhe.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ComumModule } from '../../comum/comum.module';


@NgModule({
  declarations: [
    VeiculoListarComponent,
    VeiculoFormComponent,
    VeiculoDetalheComponent
  ],
  imports: [
    CommonModule,
    VeiculoRoutingModule,
    ReactiveFormsModule,
    ComumModule
  ]
})
export class VeiculoModule { }

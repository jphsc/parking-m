import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MovveiculoListarComponent } from './movveiculo-listar/movveiculo-listar.component';
import { MovveiculoFormComponent } from './movveiculo-form/movveiculo-form.component';
import { MovveiculoDetalheComponent } from './movveiculo-detalhe/movveiculo-detalhe.component';
import { NaoEncontradaComponent } from '../nao-encontrada/nao-encontrada.component';

const routes: Routes = [
  {path: '', redirectTo: 'listar', pathMatch: 'full'},
  {path: 'listar', component: MovveiculoListarComponent},
  {path: 'cadastrar', component: MovveiculoFormComponent},
  {path: 'editar/:id', component: MovveiculoFormComponent},
  {path: 'detalhe/:id', component: MovveiculoDetalheComponent},
  {path: 'finalizar/:id', component: MovveiculoDetalheComponent},
  {path: '**', redirectTo: '/pagina-nao-encontrada'},
  {path: 'pagina-nao-encontrada', component: NaoEncontradaComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MovVeiculoRoutingModule { }

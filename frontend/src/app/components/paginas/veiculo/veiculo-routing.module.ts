import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { VeiculoListarComponent } from './veiculo-listar/veiculo-listar.component';
import { VeiculoFormComponent } from './veiculo-form/veiculo-form.component';
import { VeiculoDetalheComponent } from './veiculo-detalhe/veiculo-detalhe.component';
import { NaoEncontradaComponent } from '../nao-encontrada/nao-encontrada.component';

const routes: Routes = [
  {path: '', redirectTo: 'listar', pathMatch: 'full'},
  {path: 'listar', component: VeiculoListarComponent},
  {path: 'cadastrar', component: VeiculoFormComponent},
  {path: 'editar/:id', component: VeiculoFormComponent},
  {path: 'detalhe/:id', component: VeiculoDetalheComponent},
  {path: '**', redirectTo: '/pagina-nao-encontrada'},
  {path: 'pagina-nao-encontrada', component: NaoEncontradaComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class VeiculoRoutingModule { }

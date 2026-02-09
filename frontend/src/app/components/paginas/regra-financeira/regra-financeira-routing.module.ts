import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegraListarComponent } from './regra-listar/regra-listar.component';
import { RegraFormComponent } from './regra-form/regra-form.component';
import { RegraDetalheComponent } from './regra-detalhe/regra-detalhe.component';
import { NaoEncontradaComponent } from '../nao-encontrada/nao-encontrada.component';

const routes: Routes = [
  {path: '', redirectTo: 'listar', pathMatch: 'full'},
  {path: 'listar', component: RegraListarComponent},
  {path: 'cadastrar', component: RegraFormComponent},
  {path: 'editar/:id', component: RegraFormComponent},
  {path: 'detalhe/:id', component: RegraDetalheComponent},
  {path: '**', redirectTo: '/pagina-nao-encontrada'},
  {path: 'pagina-nao-encontrada', component: NaoEncontradaComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RegraFinanceiraRoutingModule { }

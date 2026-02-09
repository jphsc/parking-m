import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/paginas/home/home.component';
import { NaoEncontradaComponent } from './components/paginas/nao-encontrada/nao-encontrada.component';

const routes: Routes = [
  {path: '', redirectTo: 'home', pathMatch: 'full'},
  {path: 'home', component: HomeComponent},
  {path: 'veiculo', loadChildren: () => import('./components/paginas/veiculo/veiculo.module').then(m => m.VeiculoModule)},
  {path: 'movimento', loadChildren: () => import('./components/paginas/mov-veiculo/mov-veiculo.module').then(m => m.MovVeiculoModule)},
  {path: 'regra', loadChildren: () => import('./components/paginas/regra-financeira/regra-financeira.module').then(m => m.RegrafFinanceiraModule)},
  {path: '**', redirectTo: 'pagina-nao-encontrada'},
  {path: 'pagina-nao-encontrada', component: NaoEncontradaComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

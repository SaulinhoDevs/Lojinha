import { Inicio } from './pages/inicio/inicio';
import { Clientes } from './pages/clientes/clientes';
import { Produtos } from './pages/produtos/produtos';
import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'inicio', pathMatch: 'full' },
  { path: 'inicio', component: Inicio },
  { path: 'produtos', component: Produtos },
  { path: 'clientes', component: Clientes },
];

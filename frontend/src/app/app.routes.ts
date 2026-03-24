import { Vendas } from './pages/vendas/vendas';
import { Inicio } from './pages/inicio/inicio';
import { Clientes } from './pages/clientes/clientes';
import { Produtos } from './pages/produtos/produtos';
import { Routes } from '@angular/router';
import { ClienteForm } from './pages/cliente-form/cliente-form';

export const routes: Routes = [
  { path: '', redirectTo: 'inicio', pathMatch: 'full' },
  { path: 'inicio', component: Inicio },
  { path: 'produtos', component: Produtos },
  { path: 'clientes', component: Clientes },
  {path: 'vendas', component: Vendas },
  {path: 'chat-bot', loadComponent: () => import('./ia/chat-bot/chat-bot').then(c => c.ChatBot) },
  {path: 'cliente-form', component: ClienteForm},
];

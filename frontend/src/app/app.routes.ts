import { Vendas } from './pages/vendas/vendas';
import { Inicio } from './pages/inicio/inicio';
import { Clientes } from './pages/clientes/clientes';
import { Produtos } from './pages/produtos/produtos';
import { Routes } from '@angular/router';
import { ClienteForm } from './pages/cliente-form/cliente-form';
import { Categorias } from './pages/categorias/categorias';
import { CategoriaForm } from './pages/categoria-form/categoria-form';
import { ProdutoForm } from './pages/produto-form/produto-form';
import { Login } from './pages/login/login';
import { Cadastro } from './pages/cadastro/cadastro';
import { AuthLayout } from './componentes/layouts/auth-layout/auth-layout';
import { MainLayout } from './componentes/layouts/main-layout/main-layout';
import { ClienteDetails } from './pages/cliente-details/cliente-details';
import { ProdutoDetails } from './pages/produto-details/produto-details';
import { CategoriaDetails } from './pages/categoria-details/categoria-details';
import { VendaForm } from './pages/venda-form/venda-form';
import { VendaDetails } from './pages/venda-details/venda-details';
import { authGuard } from './auth/auth-guard';
import { loginGuard } from './auth/login-guard';

export const routes: Routes = [
  // Redireciona a raiz para o login
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  // Rotas SEM header e sidebar (login, cadastro)
  {
    path: '',
    component: AuthLayout,
    children: [
      { path: 'login', component: Login, canActivate: [loginGuard] },
      { path: 'cadastro', component: Cadastro },
    ],
  },

  // Rotas COM header e sidebar
  {
    path: '',
    component: MainLayout,
    canActivate: [authGuard],
    children: [
      { path: 'inicio', component: Inicio },
      { path: 'produtos', component: Produtos },
      { path: 'clientes', component: Clientes },
      { path: 'vendas', component: Vendas },
      { path: 'categorias', component: Categorias },
      {
        path: 'chat-bot',
        loadComponent: () => import('./ia/chat-bot/chat-bot').then((c) => c.ChatBot),
      },
      { path: 'cliente-form', component: ClienteForm },
      { path: 'categoria-form', component: CategoriaForm },
      { path: 'venda-form', component: VendaForm },
      { path: 'produto-form', component: ProdutoForm },
      { path: 'clientes/:id', component: ClienteDetails },
      { path: 'produtos/:id', component: ProdutoDetails },
      { path: 'categorias/:id', component: CategoriaDetails },
      { path: 'vendas/:id', component: VendaDetails },
      { path: 'produtos/:id/editar', component: ProdutoForm },
      { path: 'categorias/:id/editar', component: CategoriaForm },
      { path: 'clientes/:id/editar', component: ClienteForm },
      { path: 'vendas/:id/editar', component: VendaForm },
    ],
  },
];

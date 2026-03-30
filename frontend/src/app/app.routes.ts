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

export const routes: Routes = [
  // Redireciona a raiz para o login
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  // Rotas SEM header e sidebar (login, cadastro)
  {
    path: '',
    component: AuthLayout,
    children: [
      { path: 'login', component: Login },
      { path: 'cadastro', component: Cadastro },
    ],
  },

  // Rotas COM header e sidebar
  {
    path: '',
    component: MainLayout,
    children: [
      { path: 'inicio', component: Inicio },
      { path: 'produtos', component: Produtos },
      { path: 'clientes', component: Clientes },
      { path: 'vendas', component: Vendas },
      {
        path: 'chat-bot',
        loadComponent: () => import('./ia/chat-bot/chat-bot').then((c) => c.ChatBot),
      },
      { path: 'cliente-form', component: ClienteForm },
      { path: 'categorias', component: Categorias },
      { path: 'categoria-form', component: CategoriaForm },
      { path: 'produto-form', component: ProdutoForm },
      { path: 'clientes/:id', component: ClienteDetails },
      { path: 'produtos/:id', component: ProdutoDetails },
      { path: 'categorias/:id', component: CategoriaDetails },
      { path: 'produtos/:id/editar', component: ProdutoForm },
    ],
  },
];

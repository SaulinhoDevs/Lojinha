import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sidebar',
  imports: [RouterModule, CommonModule],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar {
  constructor(private router: Router) {}

  menuItems = [
  { label: 'Início', route: '/inicio' },
  { label: 'Produtos', route: '/produtos' },
  { label: 'Categorias', route: '/categorias' },
  { label: 'Vendas', route: '/vendas' },
  { label: 'Clientes', route: '/clientes' },
  ];

  logout() {
    const confirmado = confirm('Deseja realmente sair?');
    if (confirmado) {
      localStorage.removeItem('token');
      this.router.navigate(['/login']);
    }
  }
}
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
    { label: 'Clientes', route: '/clientes' },
    { label: 'Vendas', route: '/vendas' },
  ];

  logout() {
    const confirmado = confirm('Deseja realmente sair?');
    if (confirmado) {
      localStorage.removeItem('token');
      this.router.navigate(['/login']);
    }
  }
}

import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar {
  menuItems = [
    { label: 'Início', route: '/inicio' },
    { label: 'Produtos', route: '/produtos' },
    { label: 'Categorias', route: '/categorias' },
    { label: 'Vendas', route: '/vendas' },
    { label: 'Clientes', route: '/clientes' },
  ];
}

import { Component } from '@angular/core';

@Component({
  selector: 'app-sidebar',
  imports: [],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar {
  menuItems = [
    { label: 'Início' },
    { label: 'Produtos' },
    { label: 'Vendas' },
    { label: 'Clientes' },
  ];
}

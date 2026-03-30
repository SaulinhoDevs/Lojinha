import { Component, signal, HostListener, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header } from '../../shared/header/header';
import { Sidebar } from '../../shared/sidebar/sidebar';

@Component({
  selector: 'app-main-layout',
  imports: [Header, RouterOutlet, Sidebar],
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.css',
})
export class MainLayout implements OnInit {
  isSidebarOpen = signal(true);

  ngOnInit() {
    // Ao carregar, verifica o tamanho da tela
    this.verificarTela();
  }

  // Escuta o redimensionamento da janela em tempo real
  @HostListener('window:resize')
  verificarTela() {
    if (window.innerWidth <= 768) {
      this.isSidebarOpen.set(false); // fecha no mobile
    } else {
      this.isSidebarOpen.set(true);  // abre no desktop
    }
  }

  toggleSidebar() {
    this.isSidebarOpen.set(!this.isSidebarOpen());
  }
}
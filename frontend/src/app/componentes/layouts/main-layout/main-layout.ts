import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header } from '../../shared/header/header';
import { Sidebar } from '../../shared/sidebar/sidebar';

@Component({
  selector: 'app-main-layout',
  imports: [
    Header, RouterOutlet, Sidebar
  ],
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.css',
})
export class MainLayout {
  isSidebarOpen = signal(true);

    toggleSidebar() {
    this.isSidebarOpen.set(!this.isSidebarOpen());
    }
}

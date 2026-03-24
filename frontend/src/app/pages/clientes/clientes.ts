import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { ClientsService } from '../../services/clients-service';
import { CommonModule } from '@angular/common';
import { RouterLink } from "@angular/router";

@Component({
  selector: 'app-clientes',
  imports: [CommonModule, RouterLink],
  templateUrl: './clientes.html',
  styleUrl: './clientes.css',
})
export class Clientes {

  private clientsService = inject(ClientsService);

  public clientes: any[] = [];

  ngOnInit() {
    this.clientsService.getClients().subscribe((response: any) => {
      console.log(response);
      this.clientes = response;
    });
  }

}

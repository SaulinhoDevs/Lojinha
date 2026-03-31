import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { ClientsService } from '../../services/clients-service';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

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

  excluir(id: number, nomeCliente: string): void {
    const confirmacao = window.confirm(
      `Tem certeza que deseja excluir o cliente "${nomeCliente}"?`,
    );

    if (confirmacao) {
      this.clientsService.deleteCliente(id).subscribe({
        next: () => {
          alert('Cliente excluído com sucesso!');
          this.clientes = this.clientes.filter((cliente) => cliente.id !== id);
        },
        error: (erro: HttpErrorResponse) => {
          console.error('Erro ao excluir:', erro);
          alert('Não foi possível excluir o cliente. Ele pode estar vinculado a uma venda.');
        },
      });
    }
  }
}

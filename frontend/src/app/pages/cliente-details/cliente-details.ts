import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Cliente } from '../../services/interfaces/cliente.model';
import { ClientsService } from '../../services/clients-service';

@Component({
  selector: 'app-cliente-details',
  imports: [CommonModule, RouterLink],
  templateUrl: './cliente-details.html',
  styleUrl: './cliente-details.css',
})
export class ClienteDetails {
  private route = inject(ActivatedRoute);
  private clientsService = inject(ClientsService);

  cliente?: Cliente;
  carregando = true;
  erro = '';

  ngOnInit(): void {
    this.route.paramMap.subscribe((paramMap) => {
      const id = Number(paramMap.get('id'));
      this.carregarCliente(id);
    });
  }

  private carregarCliente(id: number): void {
    if (!id) {
      this.erro = 'ID do cliente inválido.';
      this.carregando = false;
      return;
    } else {
      this.carregando = true;
      this.erro = '';

      this.clientsService.buscarPorId(id).subscribe({
        next: (resposta) => {
          this.cliente = resposta;
          this.carregando = false;
        },
        error: () => {
          this.erro = 'Não foi possível carregar os dados do cliente.';
          this.carregando = false;
        },
      });
    }
  }

}

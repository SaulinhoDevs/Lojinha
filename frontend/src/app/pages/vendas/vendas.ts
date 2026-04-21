import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

import { VendasService } from '../../services/vendas-service';
import { Venda } from '../../services/interfaces/venda.model';

@Component({
  selector: 'app-vendas',
  imports: [CommonModule, RouterLink],
  templateUrl: './vendas.html',
  styleUrl: './vendas.css',
})
export class Vendas {
  private vendasService = inject(VendasService);

  public vendas: Venda[] = [];

  ngOnInit() {
    this.vendasService.getVendas().subscribe({
      next: (response) => {
        this.vendas = response;
      },
      error: (erro) => {
        console.error('Erro ao buscar vendas:', erro);
      },
    });
  }

  statusTradutor: { [key: number]: string } = {
    1: 'Pago',
    2: 'Aguardando Pagamento',
    3: 'Parcelado',
  };

  excluir(id: number, nomeCliente: string): void {
    const confirmacao = window.confirm(
      `Tem certeza que deseja excluir a venda do cliente "${nomeCliente}"?`,
    );

    if (confirmacao) {
      this.vendasService.deleteVenda(id).subscribe({
        next: () => {
          alert('Venda excluída com sucesso!');
          this.vendas = this.vendas.filter((venda) => venda.id !== id);
        },
        error: (erro: HttpErrorResponse) => {
          console.error('Erro ao excluir venda:', erro);

          const mensagemBackend =
            erro.error?.message || erro.error?.error || 'Não foi possível excluir a venda.';

          alert(mensagemBackend);
        },
      });
    }
  }
}

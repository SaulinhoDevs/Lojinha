import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';

import { VendasService } from '../../services/vendas-service';
import { Venda } from '../../services/interfaces/venda.model';

@Component({
  selector: 'app-venda-details',
  imports: [CommonModule, RouterLink],
  templateUrl: './venda-details.html',
  styleUrl: './venda-details.css',
})
export class VendaDetails {
  private route = inject(ActivatedRoute);
  private vendasService = inject(VendasService);

  venda?: Venda;
  carregando = true;
  erro = '';

  statusTradutor: { [key: number]: string } = {
    1: 'Pago',
    2: 'Aguardando Pagamento',
    3: 'Parcelado',
  };

  ngOnInit(): void {
    this.route.paramMap.subscribe((paramMap) => {
      const id = Number(paramMap.get('id'));
      this.carregarVenda(id);
    });
  }

  private carregarVenda(id: number): void {
    if (!id) {
      this.erro = 'ID da venda inválido.';
      this.carregando = false;
      return;
    }

    this.carregando = true;
    this.erro = '';

    this.vendasService.buscarPorId(id).subscribe({
      next: (resposta: Venda) => {
        this.venda = resposta;
        this.carregando = false;
      },
      error: () => {
        this.erro = 'Não foi possível carregar os dados da venda.';
        this.carregando = false;
      },
    });
  }
}

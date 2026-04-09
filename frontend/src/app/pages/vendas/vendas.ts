import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { VendasService } from '../../services/vendas-service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-vendas',
  imports: [CommonModule, RouterLink],
  templateUrl: './vendas.html',
  styleUrl: './vendas.css',
})
export class Vendas {
  private vendasService = inject(VendasService);

  public vendas: any[] = [];

  ngOnInit() {
    this.vendasService.getVendas().subscribe((response: any) => {
      console.log(response);
      this.vendas = response;
    });
  }

  statusTradutor: { [key: number]: string } = {
    1: 'Pago',
    2: 'Aguardando Pagamento',
    3: 'Parcelado',
  };
}

import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { VendasService } from '../../services/vendas-service';

@Component({
  selector: 'app-vendas',
  imports: [CommonModule],
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

}

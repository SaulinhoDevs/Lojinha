import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ProductsService } from '../../services/products-service';
import { RouterLink } from "@angular/router";

@Component({
  selector: 'app-produtos',
  imports: [CommonModule, RouterLink],
  templateUrl: './produtos.html',
  styleUrl: './produtos.css',
})

export class Produtos {

  private productsService = inject(ProductsService);

  public produtos: any[] = [];

  ngOnInit() {
    this.productsService.getProducts().subscribe((response: any) => {
      console.log(response);
      this.produtos = response;
    });
  }
}

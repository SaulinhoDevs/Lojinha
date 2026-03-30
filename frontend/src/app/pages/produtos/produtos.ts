import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ProductsService } from '../../services/products-service';
import { RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

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

  excluir(id: number, nomeProduto: string): void {
    const confirmacao = window.confirm(
      `Tem certeza que deseja excluir o produto "${nomeProduto}"?`,
    );

    if (confirmacao) {
      this.productsService.deleteProduto(id).subscribe({
        next: () => {
          alert('Produto excluído com sucesso!');
          this.produtos = this.produtos.filter((produto) => produto.id !== id);
        },
        error: (erro: HttpErrorResponse) => {
          console.error('Erro ao excluir:', erro);
          alert('Não foi possível excluir o produto. Ele pode estar vinculado a uma venda.');
        },
      });
    }
  }
}

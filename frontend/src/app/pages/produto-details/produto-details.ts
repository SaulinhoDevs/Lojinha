import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Produto } from '../../services/interfaces/produto.model';
import { ProductsService } from '../../services/products-service';

@Component({
  selector: 'app-produto-details',
  imports: [CommonModule, RouterLink],
  templateUrl: './produto-details.html',
  styleUrl: './produto-details.css',
})
export class ProdutoDetails {
  private route = inject(ActivatedRoute);
  private productsService = inject(ProductsService);

  produto?: Produto;
  carregando = true;
  erro = '';

  ngOnInit(): void {
    this.route.paramMap.subscribe((paramMap) => {
      const id = Number(paramMap.get('id'));
      this.carregarProduto(id);
    });
  }

  private carregarProduto(id: number): void {
    if (!id) {
      this.erro = 'ID do produto inválido.';
      this.carregando = false;
      return;
    } else {
      this.carregando = true;
      this.erro = '';

      this.productsService.buscarPorId(id).subscribe({
        next: (resposta) => {
          this.produto = resposta;
          this.carregando = false;
        },
        error: () => {
          this.erro = 'Não foi possível carregar os dados do produto.';
          this.carregando = false;
        },
      });
    }
  }
}

import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Categoria } from '../../services/interfaces/categoria.model';
import { CategoriasService } from '../../services/categorias-service';

@Component({
  selector: 'app-categoria-details',
  imports: [CommonModule, RouterLink],
  templateUrl: './categoria-details.html',
  styleUrl: './categoria-details.css',
})
export class CategoriaDetails {
  private route = inject(ActivatedRoute);
  private categoriasService = inject(CategoriasService);

  categoria?: Categoria;
  carregando = true;
  erro = '';

  ngOnInit(): void {
    this.route.paramMap.subscribe((paramMap) => {
      const id = Number(paramMap.get('id'));
      this.carregarCategoria(id);
    });
  }

  private carregarCategoria(id: number): void {
    if (!id) {
      this.erro = 'ID da categoria inválido.';
      this.carregando = false;
      return;
    } else {
      this.carregando = true;
      this.erro = '';

      this.categoriasService.buscarPorId(id).subscribe({
        next: (resposta) => {
          this.categoria = resposta;
          this.carregando = false;
        },
        error: () => {
          this.erro = 'Não foi possível carregar os dados da categoria.';
          this.carregando = false;
        },
      });
    }
  }
}

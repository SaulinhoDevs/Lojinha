import { Component, inject } from '@angular/core';
import { CategoriasService } from '../../services/categorias-service';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-categorias',
  imports: [CommonModule, RouterLink],
  templateUrl: './categorias.html',
  styleUrl: './categorias.css',
})
export class Categorias {
  private categoriasService = inject(CategoriasService);

  public categorias: any[] = [];

  ngOnInit() {
    this.categoriasService.getCategorias().subscribe((response: any) => {
      console.log(response);
      this.categorias = response;
    });
  }

  excluir(id: number, nomeCategoria: string): void {
    const confirmacao = window.confirm(
      `Tem certeza que deseja excluir a categoria "${nomeCategoria}"?`,
    );

    if (confirmacao) {
      this.categoriasService.deleteCategoria(id).subscribe({
        next: () => {
          alert('Categoria excluída com sucesso!');
          this.categorias = this.categorias.filter((categoria) => categoria.id !== id);
        },
        error: (erro: HttpErrorResponse) => {
          console.error('Erro ao excluir:', erro);
          alert('Não foi possível excluir a categoria. Ela pode estar vinculada a um produto.');
        },
      });
    }
  }
}

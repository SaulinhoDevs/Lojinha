import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CategoriasService } from '../../services/categorias-service';
import { Categoria } from '../../services/interfaces/categoria.model';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-categoria-form',
  imports: [ReactiveFormsModule],
  templateUrl: './categoria-form.html',
  styleUrl: './categoria-form.css',
})
export class CategoriaForm implements OnInit {
  private fb = inject(FormBuilder);
  private categoriasService = inject(CategoriasService);

  private route = inject(ActivatedRoute);
  private router = inject(Router);

  formulario!: FormGroup;

  modoEdicao = false;
  categoriaIdParaEdicao?: number;

  ngOnInit(): void {
    this.formulario = this.fb.group({
      nome: ['', [Validators.required]],
      descricao: [''],
    });

    const idDaUrl = this.route.snapshot.paramMap.get('id');
    if (idDaUrl) {
      this.modoEdicao = true;
      this.categoriaIdParaEdicao = Number(idDaUrl);
      this.carregarCategoriaParaEdicao(this.categoriaIdParaEdicao);
    }
  }

  isCampoInvalido(nomeDoCampo: string): boolean {
    const campo = this.formulario.get(nomeDoCampo);
    return !!(campo && campo.invalid && (campo.touched || campo.dirty));
  }

  salvar(): void {
    if (this.formulario.invalid) {
      alert('Por favor, preencha o nome da categoria.');
      this.formulario.markAllAsTouched();
      return;
    }

    const dadosBrutos = this.formulario.getRawValue();

    if (this.modoEdicao && this.categoriaIdParaEdicao) {
      this.categoriasService.updateCategoria(this.categoriaIdParaEdicao, dadosBrutos).subscribe({
        next: () => {
          alert('Categoria atualizada com sucesso!');
          this.router.navigate(['/categorias']);
        },
        error: (erro: HttpErrorResponse) => {
          console.error('Erro ao atualizar', erro);
          alert('Erro ao atualizar categoria.');
        },
      });
    } else {
      this.categoriasService.saveCategoria(dadosBrutos).subscribe({
        next: (categoriaSalva: Categoria) => {
          alert(`Sucesso! Categoria '${categoriaSalva.nome}' cadastrada!`);
          this.formulario.reset();
        },
        error: (erro: HttpErrorResponse) => {
          console.error('Erro ao salvar categoria:', erro.message);
          alert('Ops! Falha ao comunicar com o servidor.');
        },
      });
    }
  }

  private carregarCategoriaParaEdicao(id: number): void {
    this.categoriasService.buscarPorId(id).subscribe({
      next: (categoria) => {
        this.formulario.patchValue({
          nome: categoria.nome,
          descricao: categoria.descricao,
        });
      },
      error: (erro) => {
        console.error('Erro ao carregar categoria', erro);
        alert('Categoria não encontrada!');
        this.router.navigate(['/categorias']);
      },
    });
  }
}

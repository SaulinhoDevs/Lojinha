import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { ProductsService } from '../../services/products-service';
import { CategoriasService } from '../../services/categorias-service';
import { Categoria } from '../../services/interfaces/categoria.model';

@Component({
  selector: 'app-produto-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './produto-form.html',
  styleUrl: './produto-form.css',
})
export class ProdutoForm implements OnInit {
  private fb = inject(FormBuilder);
  private produtosService = inject(ProductsService);
  private categoriasService = inject(CategoriasService);

  formulario!: FormGroup;

  listaDeCategorias: Categoria[] = [];

  ngOnInit(): void {
    this.iniciarFormulario();
    this.carregarCategorias();
  }

  private iniciarFormulario(): void {
    this.formulario = this.fb.group({
      nome: ['', [Validators.required]],
      estoque: [null, [Validators.required, Validators.min(0)]],

      precoCompra: [null, [Validators.required, Validators.min(0.01)]],
      precoVenda: [null, [Validators.required, Validators.min(0.01)]],

      categoriasIds: [[], [Validators.required]],
    });
  }

  private carregarCategorias(): void {
    this.categoriasService.getCategorias().subscribe({
      next: (categorias) => (this.listaDeCategorias = categorias),
      error: (erro) => console.error('Erro ao buscar categorias', erro),
    });
  }

  isCampoInvalido(nomeDoCampo: string): boolean {
    const campo = this.formulario.get(nomeDoCampo);
    return !!(campo && campo.invalid && (campo.touched || campo.dirty));
  }

  salvar(): void {
    if (this.formulario.invalid) {
      alert('Por favor, preencha todos os campos obrigatórios corretamente.');
      this.formulario.markAllAsTouched();
      return;
    }

    const dadosBrutos = this.formulario.getRawValue();

    this.produtosService.saveProduto(dadosBrutos).subscribe({
      next: () => {
        alert('Produto cadastrado com sucesso!');
        this.formulario.reset({ estoque: null, categoriasIds: [] });
      },
      error: (erro: HttpErrorResponse) => {
        console.error('Erro ao salvar produto:', erro.message);
        alert('Falha ao comunicar com o servidor.');
      },
    });
  }

  onCategoriaChange(event: any, categoriaId: number | undefined): void {
    if (!categoriaId) return;

    const categoriasIdsControl = this.formulario.get('categoriasIds');
    let listaAtual: number[] = categoriasIdsControl?.value || [];

    if (event.target.checked) {
      listaAtual.push(categoriaId);
    } else {
      listaAtual = listaAtual.filter((id) => id !== categoriaId);
    }

    categoriasIdsControl?.setValue(listaAtual);
    categoriasIdsControl?.markAsDirty();
  }
}

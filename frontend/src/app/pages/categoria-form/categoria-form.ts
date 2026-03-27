import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CategoriasService } from '../../services/categorias-service';
import { Categoria } from '../../services/interfaces/categoria.model';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-categoria-form',
  imports: [ReactiveFormsModule],
  templateUrl: './categoria-form.html',
  styleUrl: './categoria-form.css',
})
export class CategoriaForm implements OnInit {
  private fb = inject(FormBuilder);
  private categoriasService = inject(CategoriasService);

  formulario!: FormGroup;

  ngOnInit(): void {
    this.formulario = this.fb.group({
      nome: ['', [Validators.required]],
      descricao: [''],
    });
  }

  isCampoInvalido(nomeDoCampo: string): boolean {
    const campo = this.formulario.get(nomeDoCampo);
    return !!(campo && campo.invalid && (campo.touched || campo.dirty));
  }

  salvar(): void {
    if (this.formulario.invalid) {
      alert('Por favor, preencha o nome da categoria.');
      return;
    }

    const dadosBrutos = this.formulario.getRawValue();

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

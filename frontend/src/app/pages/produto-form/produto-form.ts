import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { ProductsService } from '../../services/products-service';
import { CategoriasService } from '../../services/categorias-service';
import { Categoria } from '../../services/interfaces/categoria.model';
import { ActivatedRoute, Router } from '@angular/router'; // Importados!

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
  private route = inject(ActivatedRoute); // Para ler o ID da URL
  private router = inject(Router); // Para navegar de volta

  formulario!: FormGroup;
  listaDeCategorias: Categoria[] = [];

  modoEdicao = false;
  produtoIdParaEdicao?: number; // Guarda o ID para usar no PUT

  ngOnInit(): void {
    this.iniciarFormulario();
    this.carregarCategorias();

    // Verifica se existe um ID na URL
    const idDaUrl = this.route.snapshot.paramMap.get('id');
    if (idDaUrl) {
      this.modoEdicao = true;
      this.produtoIdParaEdicao = Number(idDaUrl);
      this.carregarProdutoParaEdicao(this.produtoIdParaEdicao);
    }
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

  // Busca no backend e preenche os campos automaticamente!
  private carregarProdutoParaEdicao(id: number): void {
    this.produtosService.buscarPorId(id).subscribe({
      next: (produto) => {
        // O backend devolve um array de objetos, mas o form espera um array de números (IDs)
        const idsDasCategoriasDoBanco = produto.categorias?.map((c) => c.id) || [];

        // O patchValue espalha os dados pelos inputs da tela
        this.formulario.patchValue({
          nome: produto.nome,
          estoque: produto.estoque,
          precoCompra: produto.precoCompra,
          precoVenda: produto.precoVenda,
          categoriasIds: idsDasCategoriasDoBanco,
        });
      },
      error: (erro) => {
        console.error('Erro ao carregar produto', erro);
        alert('Produto não encontrado!');
        this.router.navigate(['/produtos']);
      },
    });
  }

  isCampoInvalido(nomeDoCampo: string): boolean {
    const campo = this.formulario.get(nomeDoCampo);
    return !!(campo && campo.invalid && (campo.touched || campo.dirty));
  }

  // Novo método útil para o HTML marcar o checkbox sozinho
  isCategoriaSelecionada(id: number | undefined): boolean {
    if (!id) return false;
    const listaAtual: number[] = this.formulario.get('categoriasIds')?.value || [];
    return listaAtual.includes(id);
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

  salvar(): void {
    if (this.formulario.invalid) {
      alert('Por favor, preencha todos os campos obrigatórios corretamente.');
      this.formulario.markAllAsTouched();
      return;
    }

    const dadosBrutos = this.formulario.getRawValue();

    // Se estiver em modo edição, chama o Update (PUT)
    if (this.modoEdicao && this.produtoIdParaEdicao) {
      this.produtosService.updateProduto(this.produtoIdParaEdicao, dadosBrutos).subscribe({
        next: () => {
          alert('Produto atualizado com sucesso!');
          this.router.navigate(['/produtos']); // Volta pra tabela
        },
        error: (erro: HttpErrorResponse) => {
          console.error('Erro ao atualizar produto:', erro.message);
          alert('Falha ao atualizar no servidor.');
        },
      });
    }
    // Se não, é criação normal (POST)
    else {
      this.produtosService.saveProduto(dadosBrutos).subscribe({
        next: () => {
          alert('Produto cadastrado com sucesso!');
          this.formulario.reset({ estoque: null, categoriasIds: [] });
          this.router.navigate(['/produtos']); // Volta pra tabela (opcional)
        },
        error: (erro: HttpErrorResponse) => {
          console.error('Erro ao salvar produto:', erro.message);
          alert('Falha ao comunicar com o servidor.');
        },
      });
    }
  }
}

import { Component, inject, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

import { VendasService } from '../../services/vendas-service';
import { ProductsService } from '../../services/products-service';
import { ClientsService } from '../../services/clients-service';

import { Cliente } from '../../services/interfaces/cliente.model';
import { Produto } from '../../services/interfaces/produto.model';

@Component({
  selector: 'app-venda-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './venda-form.html',
  styleUrl: './venda-form.css',
})
export class VendaForm implements OnInit {
  private fb = inject(FormBuilder);
  private vendasService = inject(VendasService);
  private clientesService = inject(ClientsService);
  private produtosService = inject(ProductsService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  formulario!: FormGroup;
  listaDeClientes: Cliente[] = [];
  listaDeProdutos: Produto[] = [];

  modoEdicao = false;
  vendaIdParaEdicao?: number;
  carregandoVenda = false;

  ngOnInit(): void {
    this.iniciarFormulario();
    this.carregarListas();

    const idDaUrl = this.route.snapshot.paramMap.get('id');
    if (idDaUrl) {
      this.modoEdicao = true;
      this.vendaIdParaEdicao = Number(idDaUrl);
      this.carregarVendaParaEdicao(this.vendaIdParaEdicao);
    } else {
      this.adicionarItem();
    }
  }

  private iniciarFormulario(): void {
    this.formulario = this.fb.group({
      clienteId: ['', [Validators.required]],
      status: [1, [Validators.required]],
      frete: [0, [Validators.min(0)]],
      desconto: [0, [Validators.min(0)]],
      itens: this.fb.array([], [Validators.required]),
    });
  }

  private carregarListas(): void {
    this.clientesService.getClients().subscribe({
      next: (clientes) => (this.listaDeClientes = clientes),
      error: (erro) => console.error('Erro ao buscar clientes', erro),
    });

    this.produtosService.getProducts().subscribe({
      next: (produtos) => (this.listaDeProdutos = produtos),
      error: (erro) => console.error('Erro ao buscar produtos', erro),
    });
  }

  private carregarVendaParaEdicao(id: number): void {
    this.carregandoVenda = true;

    this.vendasService.buscarPorId(id).subscribe({
      next: (venda) => {
        this.formulario.patchValue({
          clienteId: venda.cliente?.id ?? '',
          status: venda.status,
          frete: venda.frete ?? 0,
          desconto: venda.desconto ?? 0,
        });

        const itensFormArray = this.itensFormArray;
        itensFormArray.clear();

        if (venda.itens?.length) {
          venda.itens.forEach((item: any) => {
            itensFormArray.push(
              this.criarItemFormGroup(item.produto?.id ?? '', item.quantidade ?? 1),
            );
          });
        } else {
          this.adicionarItem();
        }

        this.carregandoVenda = false;
      },
      error: (erro) => {
        console.error('Erro ao carregar venda', erro);
        alert('Venda não encontrada!');
        this.carregandoVenda = false;
        this.router.navigate(['/vendas']);
      },
    });
  }

  get itensFormArray(): FormArray {
    return this.formulario.get('itens') as FormArray;
  }

  private criarItemFormGroup(produtoId: number | string = '', quantidade: number = 1): FormGroup {
    return this.fb.group({
      produtoId: [produtoId, [Validators.required]],
      quantidade: [quantidade, [Validators.required, Validators.min(1)]],
    });
  }

  adicionarItem(): void {
    this.itensFormArray.push(this.criarItemFormGroup());
  }

  removerItem(index: number): void {
    if (this.itensFormArray.length > 1) {
      this.itensFormArray.removeAt(index);
    } else {
      alert('A venda precisa de pelo menos 1 produto!');
    }
  }

  isCampoInvalido(nomeDoCampo: string): boolean {
    const campo = this.formulario.get(nomeDoCampo);
    return !!(campo && campo.invalid && (campo.touched || campo.dirty));
  }

  isItemCampoInvalido(index: number, nomeDoCampo: string): boolean {
    const campo = this.itensFormArray.at(index).get(nomeDoCampo);
    return !!(campo && campo.invalid && (campo.touched || campo.dirty));
  }

  isProdutoJaSelecionado(produtoId: number | undefined, indexAtual: number): boolean {
    if (!produtoId) return false;

    return this.itensFormArray.controls.some((control, index) => {
      if (index === indexAtual) return false;

      const produtoSelecionado = Number(control.get('produtoId')?.value);
      return produtoSelecionado === produtoId;
    });
  }

  temProdutosDuplicados(): boolean {
    const ids = this.itensFormArray.controls
      .map((control) => Number(control.get('produtoId')?.value))
      .filter((id) => !!id);

    return new Set(ids).size !== ids.length;
  }

  salvar(): void {
    if (this.formulario.invalid) {
      alert('Por favor, preencha todos os campos obrigatórios corretamente.');
      this.formulario.markAllAsTouched();
      this.itensFormArray.controls.forEach((item) => item.markAllAsTouched());
      return;
    }

    if (this.temProdutosDuplicados()) {
      alert('Não é permitido repetir o mesmo produto na venda.');
      return;
    }

    const dadosBrutos = this.formulario.getRawValue();

    if (this.modoEdicao && this.vendaIdParaEdicao) {
      this.vendasService.updateVenda(this.vendaIdParaEdicao, dadosBrutos).subscribe({
        next: () => {
          alert('Venda atualizada com sucesso!');
          this.router.navigate(['/vendas']);
        },
        error: (erro: HttpErrorResponse) => {
          console.error('Erro ao atualizar venda:', erro);

          const mensagemBackend =
            erro.error?.message || erro.error?.error || 'Falha ao atualizar no servidor.';

          alert(mensagemBackend);
        },
      });
    } else {
      this.vendasService.saveVenda(dadosBrutos).subscribe({
        next: () => {
          alert('Venda cadastrada com sucesso!');
          this.formulario.reset({
            clienteId: '',
            status: 1,
            frete: 0,
            desconto: 0,
          });
          this.itensFormArray.clear();
          this.adicionarItem();
          this.router.navigate(['/vendas']);
        },
        error: (erro: HttpErrorResponse) => {
          console.error('Erro ao salvar venda:', erro);

          const mensagemBackend =
            erro.error?.message || erro.error?.error || 'Falha ao comunicar com o servidor.';

          alert(mensagemBackend);
        },
      });
    }
  }
}

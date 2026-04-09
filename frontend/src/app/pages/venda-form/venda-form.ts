import { Component, inject, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { VendasService } from '../../services/vendas-service';
import { ProductsService } from '../../services/products-service';
import { ClientsService } from '../../services/clients-service';
import { ActivatedRoute, Router } from '@angular/router';
import { Cliente } from '../../services/interfaces/cliente.model';
import { Produto } from '../../services/interfaces/produto.model';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-venda-form',
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

  ngOnInit(): void {
    this.iniciarFormulario();
    this.carregarListas();

    const idDaUrl = this.route.snapshot.paramMap.get('id');
    if (idDaUrl) {
      this.modoEdicao = true;
      this.vendaIdParaEdicao = Number(idDaUrl);
      this.carregarVendaParaEdicao(this.vendaIdParaEdicao);
    } else {
      // Se for criação nova, já coloca 1 produto vazio no carrinho para a mãe preencher
      this.adicionarItem();
    }
  }

  private iniciarFormulario(): void {
    this.formulario = this.fb.group({
      clienteId: ['', [Validators.required]],
      status: [1, [Validators.required]], // 1 = PAGO, 2 = AGUARDANDO_PAGAMENTO...
      frete: [0, [Validators.min(0)]],
      desconto: [0, [Validators.min(0)]],
      // O FormArray é a mágica para a lista de itens!
      itens: this.fb.array([], [Validators.required]),
    });
  }

  private carregarListas(): void {
    this.clientesService.getClients().subscribe({
      next: (clientes) => (this.listaDeClientes = clientes),
      error: (erro) => console.error('Erro ao buscar clientes', erro),
    });

    this.produtosService.getProducts().subscribe({
      // Use o nome do seu método do service
      next: (produtos) => (this.listaDeProdutos = produtos),
      error: (erro) => console.error('Erro ao buscar produtos', erro),
    });
  }

  private carregarVendaParaEdicao(id: number): void {
    this.vendasService.buscarPorId(id).subscribe({
      next: (venda) => {
        // Preenche os dados básicos
        this.formulario.patchValue({
          clienteId: venda.cliente.id,
          status: venda.status,
          frete: venda.frete,
          desconto: venda.desconto,
        });

        // Preenche o carrinho (FormArray) com os itens que vieram do banco
        const itensFormArray = this.itensFormArray;
        itensFormArray.clear(); // Limpa qualquer item vazio

        if (venda.itens) {
          venda.itens.forEach((item: any) => {
            itensFormArray.push(
              this.fb.group({
                produtoId: [item.produto.id, [Validators.required]],
                quantidade: [item.quantidade, [Validators.required, Validators.min(1)]],
              }),
            );
          });
        }
      },
      error: (erro) => {
        console.error('Erro ao carregar venda', erro);
        alert('Venda não encontrada!');
        this.router.navigate(['/vendas']);
      },
    });
  }

  // --- GERENCIAMENTO DO CARRINHO (FormArray) ---

  get itensFormArray(): FormArray {
    return this.formulario.get('itens') as FormArray;
  }

  adicionarItem(): void {
    this.itensFormArray.push(
      this.fb.group({
        produtoId: ['', [Validators.required]],
        quantidade: [1, [Validators.required, Validators.min(1)]],
      }),
    );
  }

  removerItem(index: number): void {
    if (this.itensFormArray.length > 1) {
      this.itensFormArray.removeAt(index);
    } else {
      alert('A venda precisa de pelo menos 1 produto!');
    }
  }

  // ---------------------------------------------

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

    if (this.modoEdicao && this.vendaIdParaEdicao) {
      this.vendasService.updateVenda(this.vendaIdParaEdicao, dadosBrutos).subscribe({
        next: () => {
          alert('Venda atualizada com sucesso!');
          this.router.navigate(['/vendas']);
        },
        error: (erro: HttpErrorResponse) => {
          console.error('Erro ao atualizar venda:', erro.message);
          alert('Falha ao atualizar no servidor.');
        },
      });
    } else {
      this.vendasService.saveVenda(dadosBrutos).subscribe({
        next: () => {
          alert('Venda cadastrada com sucesso! Estoque e dívidas atualizados.');
          this.formulario.reset({ frete: 0, desconto: 0, status: 1 });
          this.itensFormArray.clear();
          this.adicionarItem(); // Adiciona 1 campo em branco para a próxima venda
          this.router.navigate(['/vendas']);
        },
        error: (erro: HttpErrorResponse) => {
          console.error('Erro ao salvar venda:', erro.message);
          alert('Falha ao comunicar com o servidor.');
        },
      });
    }
  }
}

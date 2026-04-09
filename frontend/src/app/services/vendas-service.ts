import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Venda } from './interfaces/venda.model';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class VendasService {
  static readonly BASE_PATH = 'http://localhost:8080';

  private http = inject(HttpClient);

  getVendas() {
    return this.http.get(`${VendasService.BASE_PATH}/vendas`);
  }

  buscarPorId(id: number): Observable<any> {
    return this.http.get<any>(`${VendasService.BASE_PATH}/vendas/${id}`);
  }

  saveVenda(dadosBrutos: any): Observable<any> {
    // 1. Mapeia a lista de itens (o carrinho de compras)
    const itensMapeados = dadosBrutos.itens.map((item: any) => {
      return {
        produto: { id: Number(item.produtoId) },
        quantidade: Number(item.quantidade),
      };
    });

    // 2. Monta o objeto da venda exatamente como o Spring Boot espera
    const vendaParaSalvar = {
      frete: dadosBrutos.frete,
      desconto: dadosBrutos.desconto,
      status: Number(dadosBrutos.status),
      cliente: { id: Number(dadosBrutos.clienteId) },
      itens: itensMapeados,
    };

    return this.http.post<any>(`${VendasService.BASE_PATH}/vendas`, vendaParaSalvar);
  }

  updateVenda(id: number, dadosBrutos: any): Observable<any> {
    // 1. Mapeia a lista de itens
    const itensMapeados = dadosBrutos.itens.map((item: any) => {
      return {
        produto: { id: Number(item.produtoId) },
        quantidade: Number(item.quantidade),
      };
    });

    // 2. Monta o objeto da venda com o ID para atualizar
    const vendaParaAtualizar = {
      id: id,
      frete: dadosBrutos.frete,
      desconto: dadosBrutos.desconto,
      status: Number(dadosBrutos.status),
      cliente: { id: Number(dadosBrutos.clienteId) },
      itens: itensMapeados,
    };

    return this.http.put<any>(`${VendasService.BASE_PATH}/vendas/${id}`, vendaParaAtualizar);
  }

  deleteVenda(id: number): Observable<void> {
    return this.http.delete<void>(`${VendasService.BASE_PATH}/vendas/${id}`);
  }
}

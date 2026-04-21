import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Venda } from './interfaces/venda.model';

@Injectable({ providedIn: 'root' })
export class VendasService {
  static readonly BASE_PATH = 'http://localhost:8080';

  private http = inject(HttpClient);

  getVendas(): Observable<Venda[]> {
    return this.http.get<Venda[]>(`${VendasService.BASE_PATH}/vendas`);
  }

  buscarPorId(id: number): Observable<Venda> {
    return this.http.get<Venda>(`${VendasService.BASE_PATH}/vendas/${id}`);
  }

  saveVenda(dadosBrutos: any): Observable<Venda> {
    const itensMapeados = dadosBrutos.itens.map((item: any) => ({
      produtoId: Number(item.produtoId),
      quantidade: Number(item.quantidade),
    }));

    const vendaParaSalvar = {
      frete: Number(dadosBrutos.frete ?? 0),
      desconto: Number(dadosBrutos.desconto ?? 0),
      status: Number(dadosBrutos.status),
      clienteId: Number(dadosBrutos.clienteId),
      itens: itensMapeados,
    };

    return this.http.post<Venda>(`${VendasService.BASE_PATH}/vendas`, vendaParaSalvar);
  }

  updateVenda(id: number, dadosBrutos: any): Observable<Venda> {
    const itensMapeados = dadosBrutos.itens.map((item: any) => ({
      produtoId: Number(item.produtoId),
      quantidade: Number(item.quantidade),
    }));

    const vendaParaAtualizar = {
      frete: Number(dadosBrutos.frete ?? 0),
      desconto: Number(dadosBrutos.desconto ?? 0),
      status: Number(dadosBrutos.status),
      clienteId: Number(dadosBrutos.clienteId),
      itens: itensMapeados,
    };

    return this.http.put<Venda>(`${VendasService.BASE_PATH}/vendas/${id}`, vendaParaAtualizar);
  }

  deleteVenda(id: number): Observable<void> {
    return this.http.delete<void>(`${VendasService.BASE_PATH}/vendas/${id}`);
  }
}

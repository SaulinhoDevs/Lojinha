import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class VendasService {
  static readonly BASE_PATH = 'http://localhost:8080';

  private http = inject(HttpClient);

  getVendas(): Observable<any[]> {
    return this.http.get<any[]>(`${VendasService.BASE_PATH}/vendas`);
  }

  buscarPorId(id: number): Observable<any> {
    return this.http.get<any>(`${VendasService.BASE_PATH}/vendas/${id}`);
  }

  saveVenda(dadosBrutos: any): Observable<any> {
    const itensMapeados = dadosBrutos.itens.map((item: any) => ({
      produto: { id: Number(item.produtoId) },
      quantidade: Number(item.quantidade),
    }));

    const vendaParaSalvar = {
      frete: Number(dadosBrutos.frete ?? 0),
      desconto: Number(dadosBrutos.desconto ?? 0),
      status: Number(dadosBrutos.status),
      cliente: { id: Number(dadosBrutos.clienteId) },
      itens: itensMapeados,
    };

    return this.http.post<any>(`${VendasService.BASE_PATH}/vendas`, vendaParaSalvar);
  }

  updateVenda(id: number, dadosBrutos: any): Observable<any> {
    const itensMapeados = dadosBrutos.itens.map((item: any) => ({
      produto: { id: Number(item.produtoId) },
      quantidade: Number(item.quantidade),
    }));

    const vendaParaAtualizar = {
      id,
      frete: Number(dadosBrutos.frete ?? 0),
      desconto: Number(dadosBrutos.desconto ?? 0),
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

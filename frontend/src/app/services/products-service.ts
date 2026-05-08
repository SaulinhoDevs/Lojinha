import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Produto, ProdutoRequest } from './interfaces/produto.model';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ProductsService {
  static readonly BASE_PATH = 'http://localhost:8080';

  private http = inject(HttpClient);

  getProducts(): Observable<Produto[]> {
    return this.http.get<Produto[]>(`${ProductsService.BASE_PATH}/produtos`);
  }

  buscarPorId(id: number): Observable<Produto> {
    return this.http.get<Produto>(`${ProductsService.BASE_PATH}/produtos/${id}`);
  }

  saveProduto(dadosBrutos: any): Observable<Produto> {
    const produtoParaSalvar: ProdutoRequest = {
      nome: dadosBrutos.nome,
      estoque: Number(dadosBrutos.estoque),
      precoVenda: Number(dadosBrutos.precoVenda),
      precoCompra: Number(dadosBrutos.precoCompra),
      categoriasIds: dadosBrutos.categoriasIds.map((idDaTela: number) => Number(idDaTela)),
    };

    return this.http.post<Produto>(`${ProductsService.BASE_PATH}/produtos`, produtoParaSalvar);
  }

  updateProduto(id: number, dadosBrutos: any): Observable<Produto> {
    const produtoParaAtualizar: ProdutoRequest = {
      nome: dadosBrutos.nome,
      estoque: Number(dadosBrutos.estoque),
      precoVenda: Number(dadosBrutos.precoVenda),
      precoCompra: Number(dadosBrutos.precoCompra),
      categoriasIds: dadosBrutos.categoriasIds.map((idDaTela: number) => Number(idDaTela)),
    };

    return this.http.put<Produto>(
      `${ProductsService.BASE_PATH}/produtos/${id}`,
      produtoParaAtualizar,
    );
  }

  deleteProduto(id: number): Observable<void> {
    return this.http.delete<void>(`${ProductsService.BASE_PATH}/produtos/${id}`);
  }
}

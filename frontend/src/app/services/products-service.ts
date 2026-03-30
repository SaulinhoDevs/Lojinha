import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Produto } from './interfaces/produto.model';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ProductsService {
  static readonly BASE_PATH = 'http://localhost:8080';

  private http = inject(HttpClient);

  getProducts() {
    return this.http.get(`${ProductsService.BASE_PATH}/produtos`);
  }

  buscarPorId(id: number): Observable<Produto> {
    return this.http.get<Produto>(`${ProductsService.BASE_PATH}/produtos/${id}`);
  }

  saveProduto(dadosBrutos: any): Observable<Produto> {
    const categoriasMapeadas = dadosBrutos.categoriasIds.map((idDaTela: number) => {
      return { id: Number(idDaTela) };
    });

    const produtoParaSalvar: Produto = {
      nome: dadosBrutos.nome,
      estoque: dadosBrutos.estoque,
      precoVenda: dadosBrutos.precoVenda,
      precoCompra: dadosBrutos.precoCompra,
      categorias: categoriasMapeadas,
    };

    return this.http.post<Produto>(`${ProductsService.BASE_PATH}/produtos`, produtoParaSalvar);
  }

  updateProduto(id: number, dadosBrutos: any): Observable<Produto> {
    const categoriasMapeadas = dadosBrutos.categoriasIds.map((idDaTela: number) => {
      return { id: Number(idDaTela) };
    });

    const produtoParaAtualizar: Produto = {
      id: id,
      nome: dadosBrutos.nome,
      estoque: dadosBrutos.estoque,
      precoVenda: dadosBrutos.precoVenda,
      precoCompra: dadosBrutos.precoCompra,
      categorias: categoriasMapeadas,
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

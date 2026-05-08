import { Categoria } from './categoria.model';

export interface Produto {
  id?: number;
  nome: string;
  estoque: number;
  precoVenda: number;
  precoCompra: number;
  categorias: Categoria[];
}

export interface ProdutoRequest {
  nome: string;
  estoque: number;
  precoVenda: number;
  precoCompra: number;
  categoriasIds: number[];
}

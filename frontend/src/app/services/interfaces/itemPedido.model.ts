import { Produto } from './produto.model';

export interface ItemPedido {
  produtoId: number;
  produtoNome: string;
  quantidade: number;
  precoUnitario: number;
  subTotal: number;
}

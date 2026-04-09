import { Cliente } from './cliente.model';
import { ItemPedido } from './itemPedido.model';

export interface Venda {
  id?: number;
  frete: number;
  desconto: number;
  total: number;
  status: number;
  cliente: Cliente;
  itens: ItemPedido[];
}

import { Cliente } from './cliente.model';
import { ItemPedido } from './itemPedido.model';

export interface Venda {
  id: number;
  dataVenda?: string;
  frete: number;
  desconto: number;
  total: number;
  status: number;
  clienteId: number;
  clienteNome: string;
  itens: ItemPedido[];
}

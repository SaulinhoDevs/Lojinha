package com.lojinhasystem.system.services;

import com.lojinhasystem.system.entities.Cliente;
import com.lojinhasystem.system.entities.ItemPedido;
import com.lojinhasystem.system.entities.Produto;
import com.lojinhasystem.system.entities.Venda;
import com.lojinhasystem.system.entities.enums.StatusVenda;
import com.lojinhasystem.system.repositories.ClienteRepository;
import com.lojinhasystem.system.repositories.ItemPedidoRepository;
import com.lojinhasystem.system.repositories.ProdutoRepository;
import com.lojinhasystem.system.repositories.VendaRepository;
import com.lojinhasystem.system.services.exceptions.DatabaseException;
import com.lojinhasystem.system.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Venda> findAll() {
        return vendaRepository.findAll();
    }

    public Venda findById(Long id) {
        Optional<Venda> venda = vendaRepository.findById(id);
        return venda.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Transactional
    public Venda insert(Venda obj) {

        if (obj.getCliente() == null || obj.getCliente().getId() == null) {
            throw new RuntimeException("Cliente é obrigatório");
        }

        if (obj.getItens() == null || obj.getItens().isEmpty()) {
            throw new RuntimeException("A venda deve ter ao menos um item");
        }

        Set<Long> produtosInformados = new HashSet<>();

        for (ItemPedido item : obj.getItens()) {
            if (item.getProduto() == null || item.getProduto().getId() == null) {
                throw new RuntimeException("Todos os itens devem ter um produto");
            }

            if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
                throw new RuntimeException("A quantidade do item deve ser maior que zero");
            }

            if (!produtosInformados.add(item.getProduto().getId())) {
                throw new RuntimeException("Não é permitido repetir o mesmo produto na venda.");
            }
        }

        obj.setDataVenda(LocalDate.now());

        Cliente cliente = clienteRepository.findById(obj.getCliente().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        obj.setCliente(cliente);

        vendaRepository.save(obj);

        double totalItens = 0.0;

        for (ItemPedido item : obj.getItens()) {

            Produto produto = produtoRepository.findById(item.getProduto().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

            if (produto.getEstoque() < item.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            produto.setEstoque(produto.getEstoque() - item.getQuantidade());
            produtoRepository.save(produto);

            item.setProduto(produto);
            item.setVenda(obj);
            item.setPrecoUnitario(produto.getPrecoVenda());

            totalItens += item.getSubTotal();
            itemPedidoRepository.save(item);
        }

        if (obj.getStatus() == StatusVenda.AGUARDANDO_PAGAMENTO) {
            double frete = obj.getFrete() != null ? obj.getFrete() : 0.0;
            double desconto = obj.getDesconto() != null ? obj.getDesconto() : 0.0;
            double totalVenda = totalItens + frete - desconto;

            double dividaAtual = cliente.getDivida() != null ? cliente.getDivida() : 0.0;
            cliente.setDivida(dividaAtual + totalVenda);

            clienteRepository.save(cliente);
        }

        return obj;
    }

    @Transactional
    public void delete(Long id) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        Cliente cliente = venda.getCliente();

        double frete = venda.getFrete() != null ? venda.getFrete() : 0.0;
        double desconto = venda.getDesconto() != null ? venda.getDesconto() : 0.0;

        double totalItens = 0.0;

        // 1) Devolve estoque de todos os produtos da venda
        for (ItemPedido item : venda.getItens()) {
            Produto produto = produtoRepository.findById(item.getProduto().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

            produto.setEstoque(produto.getEstoque() + item.getQuantidade());
            produtoRepository.save(produto);

            totalItens += item.getSubTotal();
        }

        double totalVenda = totalItens + frete - desconto;

        // 2) Remove impacto da dívida, se essa venda gerava dívida
        if (cliente != null && venda.getStatus() == StatusVenda.AGUARDANDO_PAGAMENTO) {
            double dividaAtual = cliente.getDivida() != null ? cliente.getDivida() : 0.0;
            double novaDivida = dividaAtual - totalVenda;
            cliente.setDivida(Math.max(novaDivida, 0.0));
            clienteRepository.save(cliente);
        }

        // 3) Exclui os itens da venda
        for (ItemPedido item : venda.getItens()) {
            itemPedidoRepository.delete(item);
        }

        // 4) Exclui a venda
        vendaRepository.delete(venda);
    }

    @Transactional
    public Venda update(Long id, Venda obj) {
        if (obj.getCliente() == null || obj.getCliente().getId() == null) {
            throw new RuntimeException("Cliente é obrigatório");
        }

        if (obj.getItens() == null || obj.getItens().isEmpty()) {
            throw new RuntimeException("A venda deve ter ao menos um item");
        }

        Set<Long> produtosInformados = new HashSet<>();

        for (ItemPedido item : obj.getItens()) {
            if (item.getProduto() == null || item.getProduto().getId() == null) {
                throw new RuntimeException("Todos os itens devem ter um produto");
            }

            if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
                throw new RuntimeException("A quantidade do item deve ser maior que zero");
            }

            if (!produtosInformados.add(item.getProduto().getId())) {
                throw new RuntimeException("Não é permitido repetir o mesmo produto na venda.");
            }
        }

        Venda vendaAtual = vendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        Cliente clienteAntigo = vendaAtual.getCliente();

        Cliente clienteNovo = clienteRepository.findById(obj.getCliente().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        StatusVenda statusAntigo = vendaAtual.getStatus();
        StatusVenda statusNovo = obj.getStatus();

        double freteNovo = obj.getFrete() != null ? obj.getFrete() : 0.0;
        double descontoNovo = obj.getDesconto() != null ? obj.getDesconto() : 0.0;

        double freteAntigo = vendaAtual.getFrete() != null ? vendaAtual.getFrete() : 0.0;
        double descontoAntigo = vendaAtual.getDesconto() != null ? vendaAtual.getDesconto() : 0.0;

        Map<Long, ItemPedido> itensAntigosMap = new HashMap<>();
        for (ItemPedido item : vendaAtual.getItens()) {
            itensAntigosMap.put(item.getProduto().getId(), item);
        }

        Map<Long, ItemPedido> itensNovosMap = new HashMap<>();
        for (ItemPedido item : obj.getItens()) {
            itensNovosMap.put(item.getProduto().getId(), item);
        }

        Set<Long> todosProdutosIds = new HashSet<>();
        todosProdutosIds.addAll(itensAntigosMap.keySet());
        todosProdutosIds.addAll(itensNovosMap.keySet());

        // Ajuste de estoque pela diferença
        for (Long produtoId : todosProdutosIds) {
            ItemPedido itemAntigo = itensAntigosMap.get(produtoId);
            ItemPedido itemNovo = itensNovosMap.get(produtoId);

            int qtdAntiga = itemAntigo != null ? itemAntigo.getQuantidade() : 0;
            int qtdNova = itemNovo != null ? itemNovo.getQuantidade() : 0;

            int diferenca = qtdNova - qtdAntiga;

            if (diferenca != 0) {
                Produto produto = produtoRepository.findById(produtoId)
                        .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

                if (diferenca > 0) {
                    if (produto.getEstoque() < diferenca) {
                        throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
                    }
                    produto.setEstoque(produto.getEstoque() - diferenca);
                } else {
                    produto.setEstoque(produto.getEstoque() + Math.abs(diferenca));
                }

                produtoRepository.save(produto);
            }
        }

        // Total antigo
        double totalItensAntigos = 0.0;
        for (ItemPedido item : vendaAtual.getItens()) {
            totalItensAntigos += item.getSubTotal();
        }
        double totalVendaAntiga = totalItensAntigos + freteAntigo - descontoAntigo;

        // Remove itens antigos
        for (ItemPedido item : vendaAtual.getItens()) {
            itemPedidoRepository.delete(item);
        }
        vendaAtual.getItens().clear();

        // Atualiza dados básicos
        vendaAtual.setCliente(clienteNovo);
        vendaAtual.setFrete(obj.getFrete());
        vendaAtual.setDesconto(obj.getDesconto());
        vendaAtual.setStatus(statusNovo);

        vendaRepository.save(vendaAtual);

        // Cria itens novos
        double totalItensNovos = 0.0;

        for (ItemPedido item : obj.getItens()) {
            Produto produto = produtoRepository.findById(item.getProduto().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

            ItemPedido novoItem = new ItemPedido();
            novoItem.setVenda(vendaAtual);
            novoItem.setProduto(produto);
            novoItem.setQuantidade(item.getQuantidade());
            novoItem.setPrecoUnitario(produto.getPrecoVenda());

            totalItensNovos += novoItem.getSubTotal();

            itemPedidoRepository.save(novoItem);
            vendaAtual.getItens().add(novoItem);
        }

        double totalVendaNova = totalItensNovos + freteNovo - descontoNovo;

        // Impacto na dívida
        double impactoAntigo = (statusAntigo == StatusVenda.AGUARDANDO_PAGAMENTO) ? totalVendaAntiga : 0.0;
        double impactoNovo = (statusNovo == StatusVenda.AGUARDANDO_PAGAMENTO) ? totalVendaNova : 0.0;

        boolean mesmoCliente = clienteAntigo != null
                && clienteNovo != null
                && clienteAntigo.getId().equals(clienteNovo.getId());

        if (mesmoCliente) {
            double dividaAtual = clienteNovo.getDivida() != null ? clienteNovo.getDivida() : 0.0;
            double diferencaDivida = impactoNovo - impactoAntigo;
            clienteNovo.setDivida(dividaAtual + diferencaDivida);
            clienteRepository.save(clienteNovo);
        } else {
            if (clienteAntigo != null && impactoAntigo > 0) {
                double dividaClienteAntigo = clienteAntigo.getDivida() != null ? clienteAntigo.getDivida() : 0.0;
                clienteAntigo.setDivida(dividaClienteAntigo - impactoAntigo);
                clienteRepository.save(clienteAntigo);
            }

            if (clienteNovo != null && impactoNovo > 0) {
                double dividaClienteNovo = clienteNovo.getDivida() != null ? clienteNovo.getDivida() : 0.0;
                clienteNovo.setDivida(dividaClienteNovo + impactoNovo);
                clienteRepository.save(clienteNovo);
            }
        }

        return vendaRepository.findById(vendaAtual.getId())
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

}
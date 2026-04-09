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
import java.util.List;
import java.util.Optional;

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

    public void delete(Long id) {
        try {
            if (vendaRepository.existsById(id)) {
                vendaRepository.deleteById(id);
            } else {
                throw new ResourceNotFoundException(id);
            }
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

}
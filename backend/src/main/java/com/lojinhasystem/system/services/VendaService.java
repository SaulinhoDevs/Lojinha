package com.lojinhasystem.system.services;

import com.lojinhasystem.system.entities.Cliente;
import com.lojinhasystem.system.entities.ItemPedido;
import com.lojinhasystem.system.entities.Produto;
import com.lojinhasystem.system.entities.Usuario;
import com.lojinhasystem.system.entities.Venda;
import com.lojinhasystem.system.entities.enums.StatusVenda;
import com.lojinhasystem.system.repositories.ClienteRepository;
import com.lojinhasystem.system.repositories.ItemPedidoRepository;
import com.lojinhasystem.system.repositories.ProdutoRepository;
import com.lojinhasystem.system.repositories.VendaRepository;
import com.lojinhasystem.system.resources.dto.ItemPedidoRequestDTO;
import com.lojinhasystem.system.resources.dto.ItemPedidoResponseDTO;
import com.lojinhasystem.system.resources.dto.VendaRequestDTO;
import com.lojinhasystem.system.resources.dto.VendaResponseDTO;
import com.lojinhasystem.system.services.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    public List<VendaResponseDTO> findAll() {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();

        return vendaRepository.findByUsuarioId(usuarioLogado.getId())
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public VendaResponseDTO findById(Long id) {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();

        Venda venda = vendaRepository.findByIdAndUsuarioId(id, usuarioLogado.getId())
                .orElseThrow(() -> new ResourceNotFoundException(id));

        return toResponseDTO(venda);
    }

    @Transactional
    public VendaResponseDTO insert(VendaRequestDTO dto) {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();
        Long usuarioId = usuarioLogado.getId();

        if (dto.getClienteId() == null) {
            throw new RuntimeException("Cliente é obrigatório");
        }

        if (dto.getItens() == null || dto.getItens().isEmpty()) {
            throw new RuntimeException("A venda deve ter ao menos um item");
        }

        Set<Long> produtosInformados = new HashSet<>();

        for (ItemPedidoRequestDTO item : dto.getItens()) {
            if (item.getProdutoId() == null) {
                throw new RuntimeException("Todos os itens devem ter um produto");
            }

            if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
                throw new RuntimeException("A quantidade do item deve ser maior que zero");
            }

            if (!produtosInformados.add(item.getProdutoId())) {
                throw new RuntimeException("Não é permitido repetir o mesmo produto na venda.");
            }
        }

        Cliente cliente = clienteRepository.findByIdAndUsuarioId(dto.getClienteId(), usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Venda venda = new Venda();
        venda.setDataVenda(LocalDate.now());
        venda.setFrete(dto.getFrete() != null ? dto.getFrete() : 0.0);
        venda.setDesconto(dto.getDesconto() != null ? dto.getDesconto() : 0.0);
        venda.setStatus(StatusVenda.valueOf(dto.getStatus()));
        venda.setCliente(cliente);
        venda.setUsuario(usuarioLogado);

        vendaRepository.save(venda);

        double totalItens = 0.0;

        for (ItemPedidoRequestDTO itemDto : dto.getItens()) {
            Produto produto = produtoRepository.findByIdAndUsuarioId(itemDto.getProdutoId(), usuarioId)
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

            if (produto.getEstoque() < itemDto.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            produto.setEstoque(produto.getEstoque() - itemDto.getQuantidade());
            produtoRepository.save(produto);

            ItemPedido item = new ItemPedido();
            item.setVenda(venda);
            item.setProduto(produto);
            item.setQuantidade(itemDto.getQuantidade());
            item.setPrecoUnitario(produto.getPrecoVenda());

            itemPedidoRepository.save(item);
            venda.getItens().add(item);

            totalItens += item.getSubTotal();
        }

        if (venda.getStatus() == StatusVenda.AGUARDANDO_PAGAMENTO) {
            double totalVenda = totalItens + venda.getFrete() - venda.getDesconto();
            double dividaAtual = cliente.getDivida() != null ? cliente.getDivida() : 0.0;
            cliente.setDivida(dividaAtual + totalVenda);
            clienteRepository.save(cliente);
        }

        return toResponseDTO(venda);
    }

    @Transactional
    public VendaResponseDTO update(Long id, VendaRequestDTO dto) {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();
        Long usuarioId = usuarioLogado.getId();

        if (dto.getClienteId() == null) {
            throw new RuntimeException("Cliente é obrigatório");
        }

        if (dto.getItens() == null || dto.getItens().isEmpty()) {
            throw new RuntimeException("A venda deve ter ao menos um item");
        }

        Set<Long> produtosInformados = new HashSet<>();

        for (ItemPedidoRequestDTO item : dto.getItens()) {
            if (item.getProdutoId() == null) {
                throw new RuntimeException("Todos os itens devem ter um produto");
            }

            if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
                throw new RuntimeException("A quantidade do item deve ser maior que zero");
            }

            if (!produtosInformados.add(item.getProdutoId())) {
                throw new RuntimeException("Não é permitido repetir o mesmo produto na venda.");
            }
        }

        Venda vendaAtual = vendaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        Cliente clienteAntigo = vendaAtual.getCliente();

        Cliente clienteNovo = clienteRepository.findByIdAndUsuarioId(dto.getClienteId(), usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        StatusVenda statusAntigo = vendaAtual.getStatus();
        StatusVenda statusNovo = StatusVenda.valueOf(dto.getStatus());

        double freteNovo = dto.getFrete() != null ? dto.getFrete() : 0.0;
        double descontoNovo = dto.getDesconto() != null ? dto.getDesconto() : 0.0;

        double freteAntigo = vendaAtual.getFrete() != null ? vendaAtual.getFrete() : 0.0;
        double descontoAntigo = vendaAtual.getDesconto() != null ? vendaAtual.getDesconto() : 0.0;

        Map<Long, ItemPedido> itensAntigosMap = new HashMap<>();
        for (ItemPedido item : vendaAtual.getItens()) {
            itensAntigosMap.put(item.getProduto().getId(), item);
        }

        Map<Long, ItemPedidoRequestDTO> itensNovosMap = new HashMap<>();
        for (ItemPedidoRequestDTO item : dto.getItens()) {
            itensNovosMap.put(item.getProdutoId(), item);
        }

        Set<Long> todosProdutosIds = new HashSet<>();
        todosProdutosIds.addAll(itensAntigosMap.keySet());
        todosProdutosIds.addAll(itensNovosMap.keySet());

        for (Long produtoId : todosProdutosIds) {
            ItemPedido itemAntigo = itensAntigosMap.get(produtoId);
            ItemPedidoRequestDTO itemNovo = itensNovosMap.get(produtoId);

            int qtdAntiga = itemAntigo != null ? itemAntigo.getQuantidade() : 0;
            int qtdNova = itemNovo != null ? itemNovo.getQuantidade() : 0;

            int diferenca = qtdNova - qtdAntiga;

            if (diferenca != 0) {
                Produto produto = produtoRepository.findByIdAndUsuarioId(produtoId, usuarioId)
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

        double totalItensAntigos = 0.0;
        for (ItemPedido item : vendaAtual.getItens()) {
            totalItensAntigos += item.getSubTotal();
        }
        double totalVendaAntiga = totalItensAntigos + freteAntigo - descontoAntigo;

        for (ItemPedido item : vendaAtual.getItens()) {
            itemPedidoRepository.delete(item);
        }
        vendaAtual.getItens().clear();

        vendaAtual.setUsuario(usuarioLogado);
        vendaAtual.setCliente(clienteNovo);
        vendaAtual.setFrete(freteNovo);
        vendaAtual.setDesconto(descontoNovo);
        vendaAtual.setStatus(statusNovo);

        vendaRepository.save(vendaAtual);

        double totalItensNovos = 0.0;

        for (ItemPedidoRequestDTO itemDto : dto.getItens()) {
            Produto produto = produtoRepository.findByIdAndUsuarioId(itemDto.getProdutoId(), usuarioId)
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

            ItemPedido novoItem = new ItemPedido();
            novoItem.setVenda(vendaAtual);
            novoItem.setProduto(produto);
            novoItem.setQuantidade(itemDto.getQuantidade());
            novoItem.setPrecoUnitario(produto.getPrecoVenda());

            itemPedidoRepository.save(novoItem);
            vendaAtual.getItens().add(novoItem);

            totalItensNovos += novoItem.getSubTotal();
        }

        double totalVendaNova = totalItensNovos + freteNovo - descontoNovo;

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
                clienteAntigo.setDivida(Math.max(dividaClienteAntigo - impactoAntigo, 0.0));
                clienteRepository.save(clienteAntigo);
            }

            if (clienteNovo != null && impactoNovo > 0) {
                double dividaClienteNovo = clienteNovo.getDivida() != null ? clienteNovo.getDivida() : 0.0;
                clienteNovo.setDivida(dividaClienteNovo + impactoNovo);
                clienteRepository.save(clienteNovo);
            }
        }

        return toResponseDTO(vendaAtual);
    }

    @Transactional
    public void delete(Long id) {
        Venda venda = findEntityById(id);

        Cliente cliente = venda.getCliente();

        double frete = venda.getFrete() != null ? venda.getFrete() : 0.0;
        double desconto = venda.getDesconto() != null ? venda.getDesconto() : 0.0;

        double totalItens = 0.0;

        for (ItemPedido item : venda.getItens()) {
            Produto produto = item.getProduto();
            produto.setEstoque(produto.getEstoque() + item.getQuantidade());
            produtoRepository.save(produto);

            totalItens += item.getSubTotal();
        }

        double totalVenda = totalItens + frete - desconto;

        if (cliente != null && venda.getStatus() == StatusVenda.AGUARDANDO_PAGAMENTO) {
            double dividaAtual = cliente.getDivida() != null ? cliente.getDivida() : 0.0;
            double novaDivida = dividaAtual - totalVenda;
            cliente.setDivida(Math.max(novaDivida, 0.0));
            clienteRepository.save(cliente);
        }

        for (ItemPedido item : venda.getItens()) {
            itemPedidoRepository.delete(item);
        }

        vendaRepository.delete(venda);
    }

    private Venda findEntityById(Long id) {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();

        return vendaRepository.findByIdAndUsuarioId(id, usuarioLogado.getId())
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    private VendaResponseDTO toResponseDTO(Venda venda) {
        List<ItemPedidoResponseDTO> itens = venda.getItens().stream()
                .map(item -> new ItemPedidoResponseDTO(
                        item.getProduto().getId(),
                        item.getProduto().getNome(),
                        item.getQuantidade(),
                        item.getPrecoUnitario(),
                        item.getSubTotal()
                ))
                .toList();

        return new VendaResponseDTO(
                venda.getId(),
                venda.getDataVenda(),
                venda.getFrete(),
                venda.getDesconto(),
                venda.getStatus().getCodigo(),
                venda.getCliente().getId(),
                venda.getCliente().getNome(),
                venda.getTotal(),
                itens
        );
    }
}
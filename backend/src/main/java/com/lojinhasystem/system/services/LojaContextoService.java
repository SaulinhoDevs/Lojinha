package com.lojinhasystem.system.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lojinhasystem.system.entities.Usuario;
import com.lojinhasystem.system.repositories.*;
import com.lojinhasystem.system.resources.dto.ResumoLojaDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LojaContextoService {

    private static final int LIMITE_ESTOQUE_BAIXO = 5;
    private static final int LIMITE_TOP_PRODUTOS = 5;

    private final UsuarioRepository usuarioRepository;
    private final VendaRepository vendaRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;
    private final ObjectMapper objectMapper;

    public LojaContextoService(UsuarioRepository usuarioRepository,
                               VendaRepository vendaRepository,
                               ItemPedidoRepository itemPedidoRepository,
                               ProdutoRepository produtoRepository,
                               ClienteRepository clienteRepository,
                               ObjectMapper objectMapper) {
        this.usuarioRepository = usuarioRepository;
        this.vendaRepository = vendaRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
        this.objectMapper = objectMapper;
    }

    public Usuario buscarLoja(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário/loja não encontrado"));
    }

    public String resumoEmJson(Long usuarioId) {
        Usuario loja = buscarLoja(usuarioId);

        LocalDate hoje = LocalDate.now();
        LocalDate inicioMes = hoje.withDayOfMonth(1);

        Double totalItens = itemPedidoRepository.somaSubtotalItensPeriodo(usuarioId, inicioMes, hoje);
        Double freteDesconto = vendaRepository.somaFreteDescontoPeriodo(usuarioId, inicioMes, hoje);
        Double totalVendas = totalItens + freteDesconto;

        Long qtdVendas = vendaRepository.countByUsuarioIdAndDataVendaBetween(usuarioId, inicioMes, hoje);
        Double totalDividas = clienteRepository.somaDividasClientes(usuarioId);

        List<ResumoLojaDTO.ProdutoEstoqueBaixoDTO> estoqueBaixo = produtoRepository
                .findByUsuarioIdAndEstoqueLessThanEqual(usuarioId, LIMITE_ESTOQUE_BAIXO)
                .stream()
                .map(p -> new ResumoLojaDTO.ProdutoEstoqueBaixoDTO(p.getNome(), p.getEstoque()))
                .toList();

        List<ResumoLojaDTO.ProdutoMaisVendidoDTO> topProdutos = itemPedidoRepository
                .topProdutosVendidos(usuarioId, PageRequest.of(0, LIMITE_TOP_PRODUTOS))
                .stream()
                .map(proj -> new ResumoLojaDTO.ProdutoMaisVendidoDTO(proj.getNome(), proj.getTotalVendido()))
                .toList();

        ResumoLojaDTO resumo = new ResumoLojaDTO(
                loja.getNome(),
                totalVendas,
                qtdVendas,
                totalDividas,
                estoqueBaixo,
                topProdutos
        );

        try {
            return objectMapper.writeValueAsString(resumo);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao montar contexto da loja", e);
        }
    }
}
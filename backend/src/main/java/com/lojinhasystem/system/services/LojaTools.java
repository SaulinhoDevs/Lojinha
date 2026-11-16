package com.lojinhasystem.system.services;

import com.lojinhasystem.system.repositories.ItemPedidoRepository;
import com.lojinhasystem.system.repositories.VendaRepository;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@Component
public class LojaTools {

    private static final Logger LOGGER = Logger.getLogger(LojaTools.class.getName());

    private final ItemPedidoRepository itemPedidoRepository;
    private final VendaRepository vendaRepository;

    public LojaTools(ItemPedidoRepository itemPedidoRepository, VendaRepository vendaRepository) {
        this.itemPedidoRepository = itemPedidoRepository;
        this.vendaRepository = vendaRepository;
    }

    @Tool(description = """
            Retorna os produtos mais vendidos da loja dentro de um período específico de datas,
            ordenados do mais vendido para o menos vendido. Use esta ferramenta sempre que a pergunta
            mencionar um mês, um ano ou um intervalo de datas específico (ex: "em outubro",
            "no mês passado", "em 2025"). Não use para perguntas sobre o histórico geral da loja.
            """)
    public List<ProdutoVendidoResultado> produtosMaisVendidosNoPeriodo(
            @ToolParam(description = "Data inicial do período, no formato yyyy-MM-dd") LocalDate inicio,
            @ToolParam(description = "Data final do período, no formato yyyy-MM-dd") LocalDate fim,
            ToolContext toolContext) {

        LOGGER.info("TOOL produtosMaisVendidosNoPeriodo chamada com inicio=" + inicio + " fim=" + fim);

        Long usuarioId = (Long) toolContext.getContext().get("usuarioId");

        var resultado = itemPedidoRepository
                .topProdutosVendidosPeriodo(usuarioId, inicio, fim, PageRequest.of(0, 5))
                .stream()
                .map(p -> new ProdutoVendidoResultado(p.getNome(), p.getTotalVendido()))
                .toList();

        LOGGER.info("TOOL produtosMaisVendidosNoPeriodo resultado=" + resultado);
        return resultado;
    }

    @Tool(description = """
            Retorna o valor total vendido (faturamento) pela loja dentro de um período específico
            de datas. Use quando a pergunta mencionar um mês, ano ou intervalo de datas específico.
            """)
    public Double totalVendidoNoPeriodo(
            @ToolParam(description = "Data inicial do período, no formato yyyy-MM-dd") LocalDate inicio,
            @ToolParam(description = "Data final do período, no formato yyyy-MM-dd") LocalDate fim,
            ToolContext toolContext) {

        LOGGER.info("TOOL totalVendidoNoPeriodo chamada com inicio=" + inicio + " fim=" + fim);

        Long usuarioId = (Long) toolContext.getContext().get("usuarioId");
        Double totalItens = itemPedidoRepository.somaSubtotalItensPeriodo(usuarioId, inicio, fim);
        Double freteDesconto = vendaRepository.somaFreteDescontoPeriodo(usuarioId, inicio, fim);
        Double total = totalItens + freteDesconto;

        LOGGER.info("TOOL totalVendidoNoPeriodo resultado=" + total);
        return total;
    }

    public record ProdutoVendidoResultado(String nome, Long quantidadeVendida) {
    }
}
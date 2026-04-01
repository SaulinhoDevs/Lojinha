package com.lojinhasystem.system.tools;

import com.lojinhasystem.system.repositories.ItemPedidoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.function.Function;

@Configuration
public class ChatBotToolsConfig {

    private final ItemPedidoRepository itemPedidoRepository;

    public ChatBotToolsConfig(ItemPedidoRepository itemPedidoRepository) {
        this.itemPedidoRepository = itemPedidoRepository;
    }

    @Bean
    @Description("Use esta função estritamente para descobrir qual é o produto mais vendido do estoque e sua quantidade.")
    public Function<ProdutoMaisVendidoRequest, ProdutoMaisVendidoResponse> buscarProdutoMaisVendido() {
        return request -> {
            List<Object[]> resultado = itemPedidoRepository.findProdutoMaisVendido(PageRequest.of(0, 1));

            if (resultado != null && !resultado.isEmpty()) {
                Object[] linha = resultado.get(0);
                String nome = (String) linha[0];
                Long qtd = ((Number) linha[1]).longValue(); // Pega a soma

                return new ProdutoMaisVendidoResponse(nome, qtd);
            }

            return new ProdutoMaisVendidoResponse("Nenhum produto vendido", 0L);
        };
    }
}
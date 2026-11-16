package com.lojinhasystem.system.repositories;

import com.lojinhasystem.system.entities.ItemPedido;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Object> {

    @Query("""
            SELECT COALESCE(SUM(i.quantidade * i.precoUnitario), 0.0)
            FROM ItemPedido i
            WHERE i.id.venda.usuario.id = :usuarioId
            AND i.id.venda.dataVenda BETWEEN :inicio AND :fim
            """)
    Double somaSubtotalItensPeriodo(@Param("usuarioId") Long usuarioId,
                                    @Param("inicio") LocalDate inicio,
                                    @Param("fim") LocalDate fim);

    @Query("""
            SELECT i.id.produto.nome AS nome, SUM(i.quantidade) AS totalVendido
            FROM ItemPedido i
            WHERE i.id.venda.usuario.id = :usuarioId
            GROUP BY i.id.produto.nome
            ORDER BY SUM(i.quantidade) DESC
            """)
    List<ProdutoVendidoProjection> topProdutosVendidos(@Param("usuarioId") Long usuarioId, Pageable pageable);

    @Query("""
            SELECT i.id.produto.nome AS nome, SUM(i.quantidade) AS totalVendido
            FROM ItemPedido i
            WHERE i.id.venda.usuario.id = :usuarioId
            AND i.id.venda.dataVenda BETWEEN :inicio AND :fim
            GROUP BY i.id.produto.nome
            ORDER BY SUM(i.quantidade) DESC
            """)
    List<ProdutoVendidoProjection> topProdutosVendidosPeriodo(@Param("usuarioId") Long usuarioId,
                                                              @Param("inicio") LocalDate inicio,
                                                              @Param("fim") LocalDate fim,
                                                              Pageable pageable);
}
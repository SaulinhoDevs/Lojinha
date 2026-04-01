package com.lojinhasystem.system.repositories;

import com.lojinhasystem.system.entities.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    @Query("SELECT ip.id.produto.nome, SUM(ip.quantidade) FROM ItemPedido ip GROUP BY ip.id.produto.nome ORDER BY SUM(ip.quantidade) DESC")
    List<Object[]> findProdutoMaisVendido(Pageable pageable);
}
package com.lojinhasystem.system.repositories;

import com.lojinhasystem.system.entities.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findByUsuarioId(Long usuarioId);

    Optional<Venda> findByIdAndUsuarioId(Long id, Long usuarioId);

    @Query("""
            SELECT COALESCE(SUM(v.frete - v.desconto), 0.0)
            FROM Venda v
            WHERE v.usuario.id = :usuarioId
            AND v.dataVenda BETWEEN :inicio AND :fim
            """)
    Double somaFreteDescontoPeriodo(@Param("usuarioId") Long usuarioId,
                                    @Param("inicio") LocalDate inicio,
                                    @Param("fim") LocalDate fim);

    long countByUsuarioIdAndDataVendaBetween(Long usuarioId, LocalDate inicio, LocalDate fim);
}
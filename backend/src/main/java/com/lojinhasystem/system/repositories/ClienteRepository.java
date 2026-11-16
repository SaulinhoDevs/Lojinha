package com.lojinhasystem.system.repositories;

import com.lojinhasystem.system.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByUsuarioId(Long usuarioId);

    Optional<Cliente> findByIdAndUsuarioId(Long id, Long usuarioId);

    @Query("SELECT COALESCE(SUM(c.divida), 0.0) FROM Cliente c WHERE c.usuario.id = :usuarioId")
    Double somaDividasClientes(@Param("usuarioId") Long usuarioId);
}
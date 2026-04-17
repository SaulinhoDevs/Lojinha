package com.lojinhasystem.system.repositories;

import com.lojinhasystem.system.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByUsuarioId(Long usuarioId);

    Optional<Cliente> findByIdAndUsuarioId(Long id, Long usuarioId);
}
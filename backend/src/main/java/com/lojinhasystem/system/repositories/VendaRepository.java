package com.lojinhasystem.system.repositories;

import com.lojinhasystem.system.entities.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findByUsuarioId(Long usuarioId);

    Optional<Venda> findByIdAndUsuarioId(Long id, Long usuarioId);
}
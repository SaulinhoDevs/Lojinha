package com.lojinhasystem.system.repositories;

import com.lojinhasystem.system.entities.UsuarioPF;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioPFRepository extends JpaRepository<UsuarioPF, Long> {
    Optional<UsuarioPF> findByEmail(String email);

}
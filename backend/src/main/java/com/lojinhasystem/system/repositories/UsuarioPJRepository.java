package com.lojinhasystem.system.repositories;

import com.lojinhasystem.system.entities.UsuarioPJ;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioPJRepository extends JpaRepository<UsuarioPJ, Long> {
    Optional<UsuarioPJ> findByEmail(String email);

}
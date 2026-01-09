package com.lojinhasystem.system.repositories;

import com.lojinhasystem.system.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
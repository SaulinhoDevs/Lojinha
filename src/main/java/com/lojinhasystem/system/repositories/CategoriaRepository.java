package com.lojinhasystem.system.repositories;

import com.lojinhasystem.system.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
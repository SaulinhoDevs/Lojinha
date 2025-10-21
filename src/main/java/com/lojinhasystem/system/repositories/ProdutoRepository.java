package com.lojinhasystem.system.repositories;

import com.lojinhasystem.system.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}
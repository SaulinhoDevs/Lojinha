package com.lojinhasystem.system.repositories;

import com.lojinhasystem.system.entities.InteracaoChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InteracaoChatRepository extends JpaRepository<InteracaoChat, Long> {

}

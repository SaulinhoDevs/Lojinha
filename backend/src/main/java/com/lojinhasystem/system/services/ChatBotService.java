package com.lojinhasystem.system.services;

import com.lojinhasystem.system.entities.InteracaoChat;
import com.lojinhasystem.system.repositories.InteracaoChatRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatBotService {

    private final ChatClient chatClient;
    private final InteracaoChatRepository repository;

    public ChatBotService(ChatClient.Builder chatClientBuilder, InteracaoChatRepository repository) {
        this.chatClient = chatClientBuilder.build();
        this.repository = repository;
    }

    public String processarMensagem(String mensagemUsuario) {
        String respostaIA = this.chatClient.prompt()
                .user(mensagemUsuario)
                .call()
                .content();

        InteracaoChat memoria = new InteracaoChat(
                mensagemUsuario,
                respostaIA,
                LocalDateTime.now()
        );

        repository.save(memoria);

        return respostaIA;
    }

}

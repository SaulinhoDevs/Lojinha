package com.lojinhasystem.system.services;

import com.lojinhasystem.system.entities.InteracaoChat;
import com.lojinhasystem.system.repositories.InteracaoChatRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class ChatBotService {

    private final ChatClient chatClient;
    private final InteracaoChatRepository repository;

    public ChatBotService(ChatClient.Builder chatClientBuilder, InteracaoChatRepository repository) {
        this.chatClient = chatClientBuilder.build();
        this.repository = repository;
    }

    public String processarMensagem(String mensagemUsuario) {
        List<InteracaoChat> historico = repository.findTop5ByOrderByIdDesc();
        Collections.reverse(historico);
        StringBuilder contextoMemoria = new StringBuilder();
        contextoMemoria.append("Você é um assistente virtual. Aqui está o histórico recente da nossa conversa. Use essas informações para responder à nova pergunta:\n\n");

        for (InteracaoChat chat : historico) {
            contextoMemoria.append("Usuário: ").append(chat.getPergunta()).append("\n");
            contextoMemoria.append("IA: ").append(chat.getResposta()).append("\n");
        }

        String respostaIA = this.chatClient.prompt()
                .system(contextoMemoria.toString())
                .user(mensagemUsuario)
                .call()
                .content();

        InteracaoChat memoria = new InteracaoChat(mensagemUsuario, respostaIA, LocalDateTime.now());
        repository.save(memoria);

        return respostaIA;
    }
}

package com.lojinhasystem.system.resources.chat;

import com.lojinhasystem.system.entities.InteracaoChat;
import com.lojinhasystem.system.services.ChatBotService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/ia/chat-bot")
public class ChatController {

    private static final Logger LOGGER = Logger.getLogger(ChatController.class.getName());

    private final ChatBotService chatBotService;

    public ChatController(ChatBotService chatBotService) {
        this.chatBotService = chatBotService;
    }

    @PostMapping
    ChatMessage chatBot(@RequestBody ChatMessage chatMessage) {
        LOGGER.info("Passou aqui: " + chatMessage);
        String resposta = chatBotService.processarMensagem(chatMessage.message());
        return new ChatMessage(resposta);
    }

    @GetMapping
    public List<InteracaoChat> buscarHistorico() {
        return chatBotService.buscarHistoricoCompleto();
    }
}
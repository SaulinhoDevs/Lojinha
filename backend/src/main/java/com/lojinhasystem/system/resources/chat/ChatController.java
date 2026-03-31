package com.lojinhasystem.system.resources.chat;

import com.lojinhasystem.system.services.ChatBotService;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
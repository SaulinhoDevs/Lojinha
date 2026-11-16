package com.lojinhasystem.system.resources.chat;

import com.lojinhasystem.system.security.UsuarioAutenticado;
import com.lojinhasystem.system.services.ChatBotService;
import jakarta.validation.constraints.NotNull;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/ia/chat-bot")
public class ChatController {

    private static final Logger LOGGER = Logger.getLogger(ChatController.class.getName());

    private final ChatBotService chatBotService;

    public ChatController(@NotNull ChatBotService chatBotService) {
        this.chatBotService = chatBotService;
    }

    @PostMapping
    ChatMessage chatBot(@RequestBody ChatMessage chatMessage,
                        @AuthenticationPrincipal UsuarioAutenticado usuarioLogado) {
        LOGGER.info("Passou aqui: " + chatMessage);

        Long usuarioId = usuarioLogado.getUsuario().getId();

        var response = chatBotService.responder(chatMessage.message(), usuarioId);
        return new ChatMessage(response);
    }
}
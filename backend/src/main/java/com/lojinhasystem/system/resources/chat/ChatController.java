package com.lojinhasystem.system.resources.chat;

import jakarta.validation.constraints.NotNull;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/ia/chat-bot")
public class ChatController {

    private static final Logger LOGGER = Logger.getLogger(ChatController.class.getName());

    private final ChatClient chatClient;

    public ChatController(@NotNull ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @PostMapping
    ChatMessage chatBot(@RequestBody ChatMessage chatMessage) {
        LOGGER.info("Passou aqui: " + chatMessage);
        var response = this.chatClient.prompt()
                .user(chatMessage.message())
                .call()
                .content();
        return new ChatMessage(response);
    }
}
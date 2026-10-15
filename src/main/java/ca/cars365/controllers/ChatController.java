package ca.cars365.controllers;

import ca.cars365.services.GeminiChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {
    private final GeminiChatService chat;
    @PostMapping
    public GeminiChatService.ChatResponse reply(@RequestBody GeminiChatService.ChatRequest request, Authentication authentication) {
        return chat.reply(authentication.getName(),request);
    }
}

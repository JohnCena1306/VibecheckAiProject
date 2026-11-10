package com.example.vibecheckaiproject.controller;

import com.example.vibecheckaiproject.dto.ChatRequest;
import com.example.vibecheckaiproject.dto.ChatResponse;
import com.example.vibecheckaiproject.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@RestController
public class VibecheckController {

    private final WebClient webClient;

    @Value("${app.api-key}")
    private String apiKey;

    @Value("${app.url}")
    private String url;

    @Value("${app.model}")
    private String model;

    @Autowired
    public VibecheckController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @GetMapping("/test")
    public String test() {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setModel(model);

        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", "You are a music recommendation assistant"));
        messages.add(new Message("user", "Recommend 3 hip hop songs for a happy mood, when considering that favorite artist is Nas"));
        chatRequest.setMessages(messages);

        ChatResponse chatResponse = webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + apiKey)
                .bodyValue(chatRequest)
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .block();

        return chatResponse.getChoices().get(0).getMessage().getContent();
    }
}

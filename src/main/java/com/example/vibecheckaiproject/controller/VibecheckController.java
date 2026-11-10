package com.example.vibecheckaiproject.controller;

import com.example.vibecheckaiproject.dto.ChatRequest;
import com.example.vibecheckaiproject.dto.ChatResponse;
import com.example.vibecheckaiproject.dto.Message;
import com.example.vibecheckaiproject.dto.Song;
import com.example.vibecheckaiproject.model.Genre;
import com.example.vibecheckaiproject.model.Mood;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/recommend")
    public List<Song> recommend(@RequestParam Genre genre,
                                @RequestParam Mood mood,
                                @RequestParam String artists) {

        String prompt = "Genre: " + genre + ". Mood: " + mood + ". "
                + "Artists I have listened to lately: " + artists + ".";

        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setModel(model);

        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system",
                "You are a music recommendation assistant. "
                        + "Recommend exactly 3 real, existing songs. "
                        + "Genre and mood are the most important criteria. "
                        + "The user's favorite artists are only a hint about their taste, such as style, era or sound. "
                        + "Use them to make the recommendations more personal, but do not base the recommendations only on them. "
                        + "Do not recommend songs by the artists the user mentions. "
                        + "Answer in plain text. Do not use Markdown, tables, bold text or emojis. "
                        + "Write one song per line in this format: Artist - Title - one short sentence about why it fits. "
                        + "Do not write anything else."));
        messages.add(new Message("user", prompt));
        chatRequest.setMessages(messages);

        ChatResponse chatResponse = webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + apiKey)
                .bodyValue(chatRequest)
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .block();

        // Gemmer ai response som variabel
        String content = chatResponse.getChoices().get(0).getMessage().getContent();

        List<String> lines = List.of(content.split("\n"));
        List<Song> songs = new ArrayList<>();

        for (String line : lines){
            List<String> parts = List.of(line.split(" - ", 3));

            if (parts.size() == 3){
                songs.add(new Song(parts.get(0).trim(), parts.get(1).trim(), parts.get(2).trim()));
            }
        }

        return songs;
    }
}

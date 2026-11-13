package com.example.vibecheckaiproject.service;

import com.example.vibecheckaiproject.dto.ChatRequest;
import com.example.vibecheckaiproject.dto.ChatResponse;
import com.example.vibecheckaiproject.dto.Message;
import com.example.vibecheckaiproject.dto.Song;
import com.example.vibecheckaiproject.model.Genre;
import com.example.vibecheckaiproject.model.Mood;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class VibecheckService {

    private static final Logger logger = LoggerFactory.getLogger(VibecheckService.class);

    private final WebClient webClient;

    @Value("${app.api-key}")
    private String apiKey;

    @Value("${app.url}")
    private String url;

    @Value("${app.model}")
    private String model;

    @Autowired
    public VibecheckService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public List<Song> getRecommendations (Genre genre, Mood mood, String artists){
        String prompt = "Genre: " + genre + ". Mood: " + mood + ". "
                + "Artists I have listened to lately: " + artists + ".";

        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setModel(model);

        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system",
                "You are a music recommendation assistant. "
                        + "Recommend exactly 3 real, existing songs. "
                        + "Never ask the user questions or for more information. Always recommend songs, even if the user only mentions one artist. "
                        + "Every song must match the user's chosen genre and mood. "
                        + "Use the user's favorite artists as the final layer to understand their taste, such as era, sound, lyrics or energy. "
                        + "Choose songs within the genre and mood that someone who likes those artists would probably enjoy. "
                        + "If the user mentions several artists, find what their taste has in common across all of them. "
                        + "If the genre is very different from the user's favorite artists, choose songs from the part of the genre that is closest to those artists. "
                        + "Avoid the most obvious mainstream hits. Prefer songs that feel like a personal discovery. "
                        + "Do not recommend songs by the artists the user mentions. "
                        + "Answer in plain text. Do not use Markdown, tables, bold text or emojis. "
                        + "Write one song per line in this format: Artist - Title - one short, casual sentence of at most 25 words. "
                        + "Write the sentence like a friend texting you a song: simple words, talk to the user as 'you', no music critic language. "
                        + "Example of the style: Mac DeMarco - Chamber of Reflection - Super dreamy and slow, perfect when you just want to zone out, and if you're into Tame Impala you'll love the hazy sound. "
                        + "If the user mentions several artists, mention the one each song is closest to, so different artists can appear across the songs. Only mention artists the user wrote, never other artists. "
                        + "Start the sentence with a capital letter. "
                        + "Do not write anything else."));
        messages.add(new Message("user", prompt));
        chatRequest.setMessages(messages);

        ChatResponse chatResponse;

        try {
            chatResponse = webClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + apiKey)
                    .bodyValue(chatRequest)
                    .retrieve()
                    .bodyToMono(ChatResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            logger.error("Error response status code: " + e.getStatusCode());
            logger.error("Error response body: " + e.getResponseBodyAsString());
            logger.error("WebClientResponseException", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The AI service failed. Try again.");
        }

        // Gemmer ai response som variabel
        String content = chatResponse.getChoices().get(0).getMessage().getContent();

        logger.info("AI response: " + content);

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

package com.example.vibecheckaiproject.controller;

import com.example.vibecheckaiproject.dto.Song;
import com.example.vibecheckaiproject.model.Genre;
import com.example.vibecheckaiproject.model.Mood;
import com.example.vibecheckaiproject.service.VibecheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class VibecheckController {

    private final VibecheckService vibecheckService;

    @Autowired
    public VibecheckController(VibecheckService vibecheckService) {
        this.vibecheckService = vibecheckService;
    }

    @GetMapping("/recommend")
    public List<Song> recommend(@RequestParam Genre genre,
                                @RequestParam Mood mood,
                                @RequestParam String artists) {

        return vibecheckService.getRecommendations(genre, mood, artists);
    }

}

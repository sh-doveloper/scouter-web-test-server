package com.scouter.scouterwebtestserver.controller;


import com.scouter.scouterwebtestserver.service.FileReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "http://localhost:3000") // React URL 허용
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WebTestController {
    private final FileReaderService fileReaderService;

    @GetMapping("/chart-data")
    public ResponseEntity<String> getGraphData() {
        try {
            String jsonData = fileReaderService.readJsonFile("chart-data.json");
            return ResponseEntity.ok(jsonData); // HTTP 200 응답
        } catch (IOException e) {
            return ResponseEntity.status(500).body("{\"error\": \"Failed to load data\"}");
        }
    }
}
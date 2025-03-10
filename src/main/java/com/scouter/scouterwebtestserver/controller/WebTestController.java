package com.scouter.scouterwebtestserver.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.scouter.scouterwebtestserver.service.FileReaderService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WebTestController {
    private final FileReaderService fileReaderService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/chart-data")
    public ResponseEntity<String> getGraphData() {
        try {
            String jsonData = fileReaderService.readJsonFile("chart-data.json");
            return ResponseEntity.ok(jsonData); // HTTP 200 응답
        } catch (IOException e) {
            return ResponseEntity.status(500).body("{\"error\": \"Failed to load data\"}");
        }
    }

    @GetMapping("/developers")
    public ResponseEntity<String> getDevelopers(@RequestParam Integer page, @RequestParam Integer pageSize) {
        try {
            // JSON 파일 읽기
            String jsonData = fileReaderService.readJsonFile("developers.json");

            // JSON 문자열을 JsonNode로 변환
            JsonNode rootNode = objectMapper.readTree(jsonData);

            // ✅ JSON 배열을 직접 ArrayList로 변환하여 순서 유지
            ArrayNode arrayNode = (ArrayNode) rootNode.get("contributionDevelopers");
            List<Map<String, Object>> developers = new ArrayList<>();

            for (JsonNode node : arrayNode) {
                developers.add(objectMapper.convertValue(node, new TypeReference<>() {}));
            }

            // 전체 데이터 개수
            int totalCount = developers.size();

            // 페이지네이션 적용
            int fromIndex = page * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, totalCount);

            // 범위 체크 (잘못된 pageNumber 요청 시 빈 리스트 반환)
            List<Map<String, Object>> pagedDevelopers = (fromIndex < totalCount) ? developers.subList(fromIndex, toIndex) : List.of();

            // 결과 JSON 생성
            Map<String, Object> response = Map.of(
                "totalCount", totalCount,
                "totalPageCount", (int) Math.ceil((double) totalCount / pageSize),
                "contributionDeveloperCount", pagedDevelopers.size(),
                "contributionDevelopers", pagedDevelopers
            );

            return ResponseEntity.ok(objectMapper.writeValueAsString(response)); // HTTP 200 응답
        } catch (IOException e) {
            return ResponseEntity.status(500).body("{\"error\": \"Failed to load data\"}");
        }
    }
}
package com.nitjsr.autobot.AutoBot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nitjsr.autobot.AutoBot.payload.EmailRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class EmailGeneratorServiceImpl implements EmailGeneratorService {
    public EmailGeneratorServiceImpl(WebClient.Builder webClient) {
        this.webClient = webClient
                .baseUrl("https://generativelanguage.googleapis.com")
                .build();
    }


    private final WebClient webClient;
    @Value("${gemini.api.url}")
    private String geminiApiUrl;
    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public String emailGenerator(EmailRequest emailRequest) {
        String prompt = promptGenerator(emailRequest);

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        String response = webClient.post()
                .uri(geminiApiUrl+geminiApiKey)
                .header("content-type","application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return extractResponseContent(response);
    }

    private String extractResponseContent(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        } catch (Exception e) {
            return "Error processing request: " + e.getMessage();
        }
    }

    private String promptGenerator(EmailRequest emailRequest) {
        StringBuilder sb = new StringBuilder();
        sb.append("Generate a professional email reply for the following email content. Please don't generate a subject line. ");
        if (emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()) {
            sb.append("Use a ").append(emailRequest.getTone()).append(" tone. ");
        }
        sb.append("\nOriginal email: \n").append(emailRequest.getEmailContent());
        return sb.toString();
    }

}

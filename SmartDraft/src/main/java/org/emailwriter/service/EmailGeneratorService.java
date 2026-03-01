package org.emailwriter.service;

import org.emailwriter.request.EmailRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class EmailGeneratorService {

    private final WebClient webClient;
    private final String geminiApiUrl;
    private final String geminiApiKey;

    public EmailGeneratorService(
            WebClient.Builder webClientBuilder,
            @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent}") String geminiApiUrl,
            @Value("${gemini.api.key:dummy-key}") String geminiApiKey) {

        this.webClient = webClientBuilder.build();
        this.geminiApiUrl = geminiApiUrl;
        this.geminiApiKey = geminiApiKey;
    }

    public Mono<String> generateEmailReply(EmailRequest emailRequest) {

        String prompt = buildPrompt(emailRequest);

        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        return webClient.post()
                .uri(geminiApiUrl + "?key=" + geminiApiKey)
                .header("Content-Type", "application/json")
                .header("x-goog-api-key", geminiApiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::extractResponseContent)
                .onErrorReturn("Error: Unable to call Gemini API (no valid key).");
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

    private String buildPrompt(EmailRequest emailRequest) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a professional email reply for the following content. Please don't generate a subject line. ");

        if (emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()) {
            prompt.append("Use a ").append(emailRequest.getTone()).append(" tone. ");
        }

        prompt.append("\nOriginal email:\n")
                .append(emailRequest.getEmailContent());

        return prompt.toString();
    }
}
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
            @Value("${GEMINI_URL:https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent}") String geminiApiUrl,
            @Value("${GEMINI_KEY:dummy-key}") String geminiApiKey) {

        this.webClient = webClientBuilder.build();
        this.geminiApiUrl = geminiApiUrl;
        this.geminiApiKey = geminiApiKey;
    }

    public Mono<String> generateEmailReply(EmailRequest emailRequest) {
        String prompt = buildPrompt(emailRequest);

        // ✅ Correct request format for Gemini 2.5
        Map<String, Object> requestBody = Map.of(
                "input", prompt,
                "temperature", 0.7,
                "maxOutputTokens", 512
        );

        return webClient.post()
                .uri(geminiApiUrl + "?key=" + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::extractResponseContent)
                .onErrorResume(e -> Mono.just("REAL ERROR: " + e.getMessage()));
    }

    // ✅ Updated to parse the correct response
    private String extractResponseContent(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            return rootNode.path("output").asText(); // "output" is the new field returned by Gemini
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
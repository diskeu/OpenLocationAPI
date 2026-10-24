package dev.timjelenz.openlocationapi.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.timjelenz.openlocationapi.services.AIWeatherService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * REST endpoint for weather questions answered by the local AI model.
 */
@RestController
@RequestMapping("/api/ai/weather")
public class AIWeatherController {

    private final AIWeatherService aiWeatherService;

    public AIWeatherController(final AIWeatherService aiWeatherService) {
        this.aiWeatherService = aiWeatherService;
    }

    /**
     * Sends a message to the weather assistant.
     *
     * @param request the chat request
     * @return the assistant's answer
     */
    @PostMapping("/chat")
    public ChatResponse chat(@Valid @RequestBody final ChatRequest request) {
        return new ChatResponse(aiWeatherService.chat(request.message()));
    }

    /**
     * Chat request sent to the assistant.
     *
     * @param message the user message
     */
    public record ChatRequest(
        @NotBlank
        @Size(max = 2000)
        String message
    ) { }

    /**
     * Chat response returned by the assistant.
     *
     * @param answer the assistant's answer
     */
    public record ChatResponse(String answer) { }
}

package dev.timjelenz.openlocationapi.services;

import java.time.LocalDate;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import dev.timjelenz.openlocationapi.tools.AIWeatherTools;

/**
 * Chat client for weather related questions.
 */
@Service
public class AIWeatherService {

    private final ChatClient chatClient;

    public AIWeatherService(
        final ChatClient.Builder chatClientBuilder,
        final AIWeatherTools tools
    ) {
        this.chatClient = chatClientBuilder
            .defaultTools(tools)
            .build();
    }

    /**
     * Sends a message to the weather assistant.
     *
     * @param message the user message
     * @return the assistant's answer
     */
    public String chat(final String message) {
        return chatClient.prompt()
            .system(systemPrompt())
            .user(message)
            .call()
            .content();
    }

    private static String systemPrompt() {
        return """
You are the weather assistant for the Open Location API.
You exclusively answer questions about locations and weather.
Use only the provided tools for all factual information.
Call listLocations if you do not know the locationId of a location.
Never invent locations, location IDs, or weather values.
Historical weather data is not available.
Respond in the language of the user’s question, defaulting to English.
Today’s date: %s.
            """.formatted(LocalDate.now());
    }
}

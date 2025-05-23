package cz.vsb.fei.project.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import cz.vsb.fei.project.data.GameSessionEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.*;
import java.util.List;
import java.util.function.Consumer;

@Log4j2
public class GameSessionClient {
    private static final String BACKEND_URL = "http://localhost:8080/api/game-sessions";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public void getAll(Consumer<List<GameSession>> callback) {
        HttpRequest req = HttpRequest.newBuilder(URI.create(BACKEND_URL))
                .GET()
                .build();

        client.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> {
                    try {
                        return objectMapper.readValue(json, new TypeReference<List<GameSession>>() {});
                    } catch (IOException e) {
                        log.error("Error parsing GameSession list from server", e);
                        throw new UncheckedIOException(e);
                    }
                })
                .thenAccept(callback)
                .exceptionally(ex -> {
                    log.error("Error while loading game sessions from server", ex);
                    return null;
                });
    }

    public void create(GameSession gs, Consumer<GameSession> callback) {
        try {
            String json = objectMapper.writeValueAsString(gs);
            HttpRequest req = HttpRequest.newBuilder(URI.create(BACKEND_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(body -> {
                        try {
                            return objectMapper.readValue(body, GameSession.class);
                        } catch (IOException e) {
                            log.error("Error parsing GameSession JSON from server", e);
                            throw new UncheckedIOException(e);
                        }
                    })
                    .thenAccept(callback)
                    .exceptionally(ex -> {
                        log.error("Error while creating game session on the server", ex);
                        return null;
                    });
        } catch (JsonProcessingException e) {
            log.error("Error serializing GameSession to JSON", e);
        }
    }

    public void update(long id, GameSession gs, Consumer<GameSession> callback) {
        try {
            String json = objectMapper.writeValueAsString(gs);
            HttpRequest req = HttpRequest.newBuilder(URI.create(BACKEND_URL + "/" + id))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(body -> {
                        try {
                            return objectMapper.readValue(body, GameSession.class);
                        } catch (IOException e) {
                            log.error("Error parsing updated GameSession JSON from server", e);
                            throw new UncheckedIOException(e);
                        }
                    })
                    .thenAccept(callback)
                    .exceptionally(ex -> {
                        log.error("Error while updating game session on the server", ex);
                        return null;
                    });
        } catch (JsonProcessingException e) {
            log.error("Error serializing GameSession to JSON", e);
        }
    }

    public void delete(long id, Runnable callback) {
        HttpRequest req = HttpRequest.newBuilder(URI.create(BACKEND_URL + "/" + id))
                .DELETE()
                .build();

        client.sendAsync(req, HttpResponse.BodyHandlers.discarding())
                .thenRun(callback)
                .exceptionally(ex -> {
                    log.error("Error while deleting game session on the server", ex);
                    return null;
                });
    }
}

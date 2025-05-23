package cz.vsb.fei.project.game;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.type.TypeReference;
import cz.vsb.fei.project.data.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.http.*;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;

@Log4j2
public class PlayerClient {
    private static final String BACKEND_URL = "http://localhost:8080/api/players";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void getAll(Consumer<List<Player>> callback) {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BACKEND_URL))
                .GET()
                .build();

        client.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> {
                    try {
                        return objectMapper.readValue(json, new TypeReference<List<Player>>(){});
                    } catch (Exception e) {
                        log.error("Error while loading pLayers form the server", e);
                        throw new CompletionException(e);
                    }
                })
                .thenAccept(callback)
                .exceptionally(ex -> { log.error("Error while loading pLayers form the server", ex); return null; });
    }

    public void create(Player p, Consumer<Player> callback) {
        try {
            String json = objectMapper.writeValueAsString(p);
            HttpRequest req = HttpRequest.newBuilder(URI.create(BACKEND_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(body ->{
                        try {
                            return objectMapper.readValue(body, Player.class);
                        } catch (IOException e) {
                            log.error("Error while creating player on the server", e);
                            throw new UncheckedIOException(e);
                        }
                    })
                    .thenAccept(callback)
                    .exceptionally(ex -> {
                        log.error("Error while creating player on the server", ex);
                        return null;
                    });
        } catch (JacksonException e) {
            log.error("Error while creating player on the server", e);
        }
    }


    public void update(long id, Player p, Runnable onSuccess) {
        try {
            String json = objectMapper.writeValueAsString(p);
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(BACKEND_URL + "/" + id))
                    .header("Content-Type","application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.sendAsync(req, HttpResponse.BodyHandlers.discarding())
                    .thenRun(onSuccess)
                    .exceptionally(ex -> { log.error("Error while updating player on the server", ex); return null; });

        } catch (Exception e) {
            log.error("Error while updating player on the server", e);
        }
    }

    public void deletePlayer(Long id, Runnable onSuccess) {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BACKEND_URL + "/" + id))
                .DELETE()
                .build();

        client.sendAsync(req, HttpResponse.BodyHandlers.discarding())
                .thenRun(onSuccess)
                .exceptionally(ex -> { log.error("Error while deleting player on the server", ex); return null; });
    }
}

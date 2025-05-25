package cz.vsb.fei.project.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import cz.vsb.fei.project.data.PlayerDTO;
import cz.vsb.fei.project.data.ScoreDTO;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Log4j2
public class ScoreClient {
    private static final String BACKEND_URL = "http://localhost:8080/api/scores";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();



    public void create(ScoreDTO dto, Consumer<ScoreDTO> callback) {
        try {
            String json = objectMapper.writeValueAsString(dto);
            HttpRequest req = HttpRequest.newBuilder(URI.create(BACKEND_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(body -> {
                        try {
                            return objectMapper.readValue(body, ScoreDTO.class);
                        } catch (IOException e) {
                            log.error("Error while creating score on the server", e);
                            throw new UncheckedIOException(e);
                        }
                    })
                    .thenAccept(callback)
                    .exceptionally(e -> {
                        log.error("Error while creating score on the server", e);
                        return null;
                    });
        } catch (JsonProcessingException e) {
            log.error("Error while score JSON serialization ", e);
        }
    }

    public void getAll(Consumer<List<ScoreDTO>> callback){
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BACKEND_URL))
                .GET()
                .build();

        client.sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(json -> {
                    try {
                        return objectMapper.readValue(json, new TypeReference<List<ScoreDTO>>(){});
                    } catch (Exception e) {
                        log.error("Error while loading score form the server", e);
                        throw new CompletionException(e);
                    }
                })
                .thenAccept(callback)
                .exceptionally(ex -> { log.error("Error while loading score form the server", ex); return null; });
    }


    public void deleteScore(Long id, Runnable onSuccess, Consumer<Throwable> onError) {
        HttpRequest req = HttpRequest.newBuilder(URI.create(BACKEND_URL + "/" + id))
                .DELETE()
                .build();

        client.sendAsync(req, HttpResponse.BodyHandlers.discarding())
                .thenAccept(response -> {
                    if (response.statusCode() == 204) {
                        onSuccess.run();
                    } else {
                        onError.accept(new Exception("Delete failed with code: " + response.statusCode()));
                    }
                })
                .exceptionally(ex -> {
                    onError.accept(ex);
                    return null;
                });
    }

}

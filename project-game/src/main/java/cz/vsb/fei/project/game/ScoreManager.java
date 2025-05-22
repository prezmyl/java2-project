package cz.vsb.fei.project.game;

import cz.vsb.fei.project.data.Score;
import cz.vsb.fei.project.storage.ScoreStorageFactory;
import cz.vsb.fei.project.storage.ScoreStorageInterface;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import cz.vsb.fei.project.storage.FileManager;
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
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Log4j2
public class ScoreManager {
    private static final String BACKEND_URL = "http://localhost:8080/api/scores";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("messages", Locale.getDefault());

    @Getter
    @Setter
    private int score = 0;
    private GraphicsContext gc;

    public void saveScore(){
        Score scoreObj = new Score(this.score);
        try {
            String json = objectMapper.writeValueAsString(scoreObj);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BACKEND_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> log.info("Server response: {}", response.body()))
                    .exceptionally(ex -> {
                        log.error("Error while sending score to server. {}", ex.getMessage());
                        return null;
                    });
        } catch (Exception e) {
            log.error("Error while score JSON serialization ", e);
        }
    }

    public void loadScores() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BACKEND_URL))
                .GET()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    try {
                        List<Score> scores = objectMapper.readValue(response.body(), new TypeReference<>() {
                        });
                        scores.forEach(sc -> log.info("{}: Score: {}", sc.getNick(), sc.getPoints()));
                    } catch (IOException e) {
                        log.error("Error while loading scores from server", e);
                    }
                })
                .exceptionally(ex -> {
                    log.error("Error while loading scores from server", ex);
                    return null;

                });

    }


    public void getHighScores(Consumer<List<Score>> callback) {
        HttpRequest rq = HttpRequest.newBuilder()
                .uri(URI.create(BACKEND_URL))
                .GET()
                .build();

        client.sendAsync(rq, HttpResponse.BodyHandlers.ofString())
                .thenAccept(rp -> {
                    try {
                        List<Score> scores = objectMapper.readValue(rp.body(), new TypeReference<>() {
                        });
                        List<Score> topScores = scores.stream()
                                .sorted(Comparator.comparingInt(Score::getPoints).reversed())
                                .limit(10)
                                .collect(Collectors.toList());

                        callback.accept(topScores);

                    } catch (IOException e) {
                        log.error("Error while loading scores from server", e);
                    }
                })
                .exceptionally(ex -> {
                    log.error("Error while loading scores from server", ex);
                    return null;
                });
    }


    public void update() {
        if (gc != null) {
            gc.setFill(Color.BLACK);
            gc.fillText(
                    MessageFormat.format(BUNDLE.getString("score.points"), score),
                    10, 40
            );
        }
    }


    public void reset(){
        score = 0;
    }

    public void increaseScore(int amount){
        score += amount;
    }


}

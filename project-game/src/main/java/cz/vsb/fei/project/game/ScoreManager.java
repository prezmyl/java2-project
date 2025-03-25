package cz.vsb.fei.project.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import cz.vsb.fei.project.file.FileManager;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;


import java.util.List;

@Log4j2
public class ScoreManager {
    @Getter
    private int score;
    private GraphicsContext gc;
    private final FileManager fileManager;

    public ScoreManager() {
        this.score = 0;
        this.fileManager = new FileManager();
    }

    public void increaseScore(int points) {
        score += points;
        log.info("Increasing Score");
    }

    public void resetScore() {
        score = 0;
    }

    public void update() {
        if (gc != null) {
            gc.setFill(Color.BLACK);
            gc.fillText("Score: " + score, 10, 40); // Zobrazení skóre
        }
    }

    // Nacte nejvyssi skore
    public List<Integer> getHighScores() {
        return fileManager.loadScores();
    }

    // Ulozi aktualni score
    public void saveCurrentScore() {
        fileManager.saveScore(score);
    }
}

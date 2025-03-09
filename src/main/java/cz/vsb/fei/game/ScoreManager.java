package cz.vsb.fei.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class ScoreManager {
    private int score;
    private GraphicsContext gc;
    private final FileManager fileManager;

    public ScoreManager() {
        this.score = 0;
        this.fileManager = new FileManager();
    }

    public int getScore() {
        return score;
    }

    public void increaseScore(int points) {
        score += points;
        System.out.println("Increasing Score");
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

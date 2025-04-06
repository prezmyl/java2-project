package cz.vsb.fei.project.game;

import cz.vsb.fei.project.data.Score;
import cz.vsb.fei.project.storage.ScoreStorageFactory;
import cz.vsb.fei.project.storage.ScoreStorageInterface;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import cz.vsb.fei.project.storage.FileManager;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;


import java.util.List;

@Log4j2
public class ScoreManager {
    @Getter
    private int points;
    private GraphicsContext gc;
    private final ScoreStorageInterface scoreStorage;

    public ScoreManager() {
        this.points = 0;
        this.scoreStorage = ScoreStorageFactory.createStorage();
    }

    public void increaseScore(int points) {
        this.points += points;
        log.info("Increasing Score");
    }

    public void resetScore() {
        points = 0;
    }

    public void update() {
        if (gc != null) {
            gc.setFill(Color.BLACK);
            gc.fillText("Score: " + points, 10, 40); // Zobrazení skóre
        }
    }

    // Nacte nejvyssi skore
    public List<Score> getHighScores() {
        return scoreStorage.getTopScores(GameSettings.getInstance().getMaxScore());
    }

    // Ulozi aktualni score
    public void saveCurrentScore() {
        scoreStorage.insertScore(new Score(points));
    }
}

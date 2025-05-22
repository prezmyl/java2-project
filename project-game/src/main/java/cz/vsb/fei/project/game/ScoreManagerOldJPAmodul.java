package cz.vsb.fei.project.game;

import cz.vsb.fei.project.data.Score;
import cz.vsb.fei.project.storage.ScoreStorageFactory;
import cz.vsb.fei.project.storage.ScoreStorageInterface;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import cz.vsb.fei.project.storage.FileManager;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;


import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Log4j2
public class ScoreManagerOldJPAmodul {
    @Getter
    private int points;
    private GraphicsContext gc;
    private final ScoreStorageInterface scoreStorage;

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("messages", Locale.getDefault());

    public ScoreManagerOldJPAmodul() {
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
            gc.fillText(MessageFormat.format(BUNDLE.getString("score.points"), points), 10, 40);
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
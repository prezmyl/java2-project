package cz.vsb.fei.project.game;

import lombok.Getter;
import lombok.Setter;

import java.util.Locale;
import java.util.ResourceBundle;

@Setter
@Getter
public class ScoreManager {
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("messages", Locale.getDefault());

    private int currentScore = 0;

    public void increase(int value) {
        currentScore += value;
    }

    public void reset() {
        currentScore = 0;
    }

}

package cz.vsb.fei.project.game;


import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.extern.log4j.Log4j2;

import java.util.*;

@Log4j2
public class GameController implements GameStateObserver {

    @FXML
    private Label scoreLabel;
    @FXML
    private Label livesLabel;

    private GameSession gameSession;
    private AnimationTimer inputHandler;
    private final Map<KeyCode, Runnable> keyAction = new HashMap<>();
    private final Set<KeyCode> activeKeys = new HashSet<>();

    public GameController() {
        // Bezparametrický konstruktor pro FXML
    }

    public void setGameSession(GameSession gameSession, DrawingThread drawingThread) {
        this.gameSession = gameSession;

        //init valid input mapping using lambda
        Player player = gameSession.getPlayer();
        keyAction.put(KeyCode.LEFT, () -> player.moveLeft(drawingThread.getDeltaT()));
        keyAction.put(KeyCode.RIGHT, () -> player.moveRight(drawingThread.getDeltaT()));
        keyAction.put(KeyCode.SPACE, () -> player.shoot(gameSession, drawingThread.getCurrentNow()));
        keyAction.put(KeyCode.H, this::displayHighScores);
        keyAction.put(KeyCode.J, this::saveCurrentScore);

        // continuous input handling
        Scene gameScene = gameSession.getScene();

        gameScene.setOnKeyPressed(event -> {
            activeKeys.add(event.getCode());  // Přidáme stisknutou klávesu do aktivních
        });

        gameScene.setOnKeyReleased(event -> {
            activeKeys.remove(event.getCode());  // Odebereme klávesu při uvolnění
        });

        //pressed continues action - anonymous Class
        inputHandler = new AnimationTimer() {
          @Override
          public void handle(long now) {
              handleContinuousInput();
          }
        };
        inputHandler.start();

    }

    public void initialize() {
        log.info("GameController initialized.");
    }

    @FXML
    protected void handleKeyPress(KeyEvent keyEvent) {
        log.debug("Key pressed: {}", keyEvent.getCode()); // Výpis klávesy
        Runnable action = keyAction.get(keyEvent.getCode());
        if (action != null) {
            log.debug("Action triggered for: {}", keyEvent.getCode());
            action.run();
        } else {
            log.debug("No action mapped for: {}", keyEvent.getCode());
        }
    }

    private void handleContinuousInput() {
        for (KeyCode key : activeKeys) {
            Runnable action = keyAction.get(key);
            if (action != null) {
                log.debug("Executing action for: {}", key);
                action.run();
            }
        }
    }




    @Override
    public void onScoreUpdate(int newScore) {
        scoreLabel.setText("Score: " + newScore);
    }

    @Override
    public void onLivesUpdate(int remainingLives) {
        livesLabel.setText("Lives: " + remainingLives);
    }

    @Override
    public void onGameOver() {
        inputHandler.stop();
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText("invasion succeed");
            alert.setContentText("You lost and the planet Earth is lost too");
            alert.showAndWait();
            saveCurrentScore();
        });

    }

    @Override
    public void onGameWin() {
        inputHandler.stop();
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Victory");
            alert.setHeaderText("The invasion has been stopped ");
            alert.setContentText("All enemies have been destroyed and the planet Earth is saved");
            alert.showAndWait();
            saveCurrentScore();
        });
    }

    @FXML
    private void displayHighScores() {
        gameSession.getScoreClient().getHighScores(list -> {
            //jump back from http thread to JavaFX thred
            Platform.runLater(() -> {
                if (list.isEmpty()){
                    showAlert("No high scores available.");
                } else {
                    StringBuilder sb = new StringBuilder("Top High Scores:\n");
                    for (int i = 0; i < list.size(); i++) {
                        sb.append(i + 1).append(". ").append(list.get(i)).append("\n");
                    }
                    showAlert(sb.toString());
                }
            });
        });

      /*  List<Score> highScores = gameSession.getScoreManager().getHighScores();
        if (highScores.isEmpty()) {
            showAlert("No high scores available.");
        } else {
            StringBuilder sb = new StringBuilder("Top High Scores:\n");
            for (int i = 0; i < highScores.size(); i++) {
                sb.append(i + 1).append(". ").append(highScores.get(i)).append("\n");
            }
            showAlert(sb.toString());
        }*/
    }

    @FXML
    public void handleHighScoresButton() {
        displayHighScores();
        log.info("High Scores button clicked");
        requestFocusToCanvas();


    }

    @FXML
    public void handleSaveScoreButton() {
        saveCurrentScore();
        log.info("Save Score button clicked");
        requestFocusToCanvas();
    }

    private void requestFocusToCanvas() {
        if (scoreLabel.getScene() != null) {
            scoreLabel.getScene().getRoot().requestFocus();
        }
    }

    private void saveCurrentScore() {
        gameSession.getScoreClient().saveScore();
        showAlert("Score saved successfully!");
    }

    private void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("High Scores");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}

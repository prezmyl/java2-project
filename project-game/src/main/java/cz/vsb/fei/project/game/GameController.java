package cz.vsb.fei.project.game;


import cz.vsb.fei.project.data.GameSessionDTO;
import cz.vsb.fei.project.data.PlayerDTO;
import cz.vsb.fei.project.data.ScoreDTO;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.*;

@Log4j2
public class GameController implements GameStateObserver {

    @FXML
    private Label scoreLabel;
    @FXML
    private Label livesLabel;

    @Getter
    private final PlayerClient playerClient;

    @Getter
    private final GameSessionClient gameSessionClient;

    @Getter
    private final ScoreClient scoreClient;

    private GameSession gameSession;
    private AnimationTimer inputHandler;
    private final Map<KeyCode, Runnable> keyAction = new HashMap<>();
    private final Set<KeyCode> activeKeys = new HashSet<>();

    public GameController() {
        // Bezparametrický konstruktor pro FXML
        this.playerClient = new PlayerClient();
        this.gameSessionClient = new GameSessionClient();
        this.scoreClient = new ScoreClient();
    }

    public void setGameSession(GameSession gameSession, DrawingThread drawingThread) {
        this.gameSession = gameSession;

        //init valid input mapping using lambda
        Player player = gameSession.getPlayer();
        keyAction.put(KeyCode.LEFT, () -> player.moveLeft(drawingThread.getDeltaT()));
        keyAction.put(KeyCode.RIGHT, () -> player.moveRight(drawingThread.getDeltaT()));
        keyAction.put(KeyCode.SPACE, () -> player.shoot(gameSession, drawingThread.getCurrentNow()));
        keyAction.put(KeyCode.H, this::displayHighScores);
        keyAction.put(KeyCode.J, this::saveCurrentScoreToBE);

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

    public void saveCurrentScoreToBE() {
        int score = gameSession.getScoreManager().getCurrentScore();

        // 1. vytovorim noveho hrace (nahodny nick ted)
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setNickname("Player" + UUID.randomUUID().toString().substring(0, 8));
        playerClient.create(playerDTO, createdPlayer -> {
            System.out.println("RESPONSE PlayerDTO from BE: " + createdPlayer);
            System.out.println("PlayerDTO.id = " + createdPlayer.getId());
            System.out.println("PlayerDTO.nickname = " + createdPlayer.getNickname());

            Long playerId = createdPlayer.getId();

            // Overeni ze id neni null!
            if (playerId == null) {
                showAlert("Chyba: Hráč nebyl na backendu vytvořen!");
                return;
            }

            // 2. Vytvorim novo session
            GameSessionDTO sessionDTO = new GameSessionDTO();
            sessionDTO.setGameName("Session " + UUID.randomUUID().toString().substring(0, 8));
            gameSessionClient.create(sessionDTO, createdSession -> {
                Long sessionId = createdSession.getId();

                // overim not null
                if (sessionId == null) {
                    showAlert("Chyba: Herní session nebyla na backendu vytvořena!");
                    return;
                }

                // 3. Pockam azz obe id hotove, a pak vytvor ScoreDTO a odesli na BE
                ScoreDTO dto = new ScoreDTO();
                dto.setPoints(score);
                dto.setPlayerId(playerId);
                dto.setGameSessionId(sessionId);

                scoreClient.create(dto, response -> {
                    showAlert("Score: " + score + " saved with random player and session!");
                });
            });
        });


        playerClient.create(playerDTO, createdPlayer -> {

            System.out.println("RESPONSE PlayerDTO from BE: " + createdPlayer);
            Long playerId = createdPlayer.getId();
            if (playerId == null) {
                showAlert("Chyba: Hráč nebyl na backendu vytvořen!");
                return;
            }


        });

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
            saveCurrentScoreToBE();
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
            saveCurrentScoreToBE();
        });
    }

    @FXML
    private void displayHighScores() {
        scoreClient.getAll(list -> {

            //seradim nejdriv
            list.sort((a, b) -> Integer.compare(b.getPoints(), a.getPoints()));

            //jump back from http thread to JavaFX thred
            Platform.runLater(() -> {
                if (list.isEmpty()){
                    showAlert("No high scores available.");
                } else {
                    StringBuilder sb = new StringBuilder("Top High Scores:\n");
                    for (int i = 0; i < list.size(); i++) {
                        ScoreDTO score = list.get(i);
                        sb.append(i + 1)
                                .append(". ")
                                .append(score.getPlayerNickname() != null ? score.getPlayerNickname() : "Unknown")
                                .append(" - ")
                                .append(score.getPoints())
                                .append("\n");
                    }
                    showAlert(sb.toString());
                }
            });
        });

    }

    @FXML
    public void handleHighScoresButton() {
        displayHighScores();
        log.info("High Scores button clicked");
        requestFocusToCanvas();


    }

    @FXML
    public void handleSaveScoreButton() {
        saveCurrentScoreToBE();
        log.info("Save Score button clicked");
        requestFocusToCanvas();
    }

    @FXML
    private void handleDeleteRandomScore() {
        // Tohle je jen demo, vyamze proste prvni skore

        scoreClient.getAll(scores -> {
            if (!scores.isEmpty()) {
                Long id = scores.get(0).getId(); // smaze prvni score
                scoreClient.deleteScore(id, () -> {
                    Platform.runLater(() -> showAlert("Score was deleted successfully!"));
                }, ex -> {
                    Platform.runLater(() -> showAlert("score deletion failed: " + ex.getMessage()));
                });
            }
        });

    }

    @FXML
    private void handleDeleteFirstScore() {
        scoreClient.getAll(list -> {
            // Seradit podle bodu sestupne, pak az smazu opravdu to prvni
            list.sort((a, b) -> Integer.compare(b.getPoints(), a.getPoints()));

            if (!list.isEmpty()) {
                ScoreDTO topScore = list.get(0); // ted uz tohle je nejvyssi
                Long id = topScore.getId();
                scoreClient.deleteScore(id, () -> {
                    Platform.runLater(() -> showAlert("Top Score was deleted successfully!"));
                }, ex -> {
                    Platform.runLater(() -> showAlert("deletion failed: " + ex.getMessage()));
                });
            } else {
                Platform.runLater(() -> showAlert("no score to delete!"));
            }
        });
    }


    private void requestFocusToCanvas() {
        if (scoreLabel.getScene() != null) {
            scoreLabel.getScene().getRoot().requestFocus();
        }
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

package cz.vsb.fei.game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class MenuController {

    private AppGame appGame; // Odkaz na hlavni tridu

    public void setApp(AppGame appGame) {
        this.appGame = appGame;
    }

    @FXML
    private void startGame(ActionEvent event) {
        Stage primaryStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        appGame.startGame(primaryStage); // Prepnuti na herni scenu
    }

    @FXML
    private void exitGame(ActionEvent event) {
        // Ukonceni aplikace
        System.exit(0);
    }
}

package cz.vsb.fei.project.game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AppGame extends Application {

	private DrawingThread drawingThread;
	private GameSession gameSession;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			// Nacteni menu jako vychozi obrazovka
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/vsb/fei/project/game/menuWindow.fxml"));
			Pane menuRoot = loader.load();

			// Nastaveni akce pro prepnuti na herni obrazovku
			MenuController menuController = loader.getController();
			menuController.setApp(this); // Propojeni s intanci App

			Scene menuScene = new Scene(menuRoot);
			primaryStage.setScene(menuScene);
			primaryStage.setTitle("Space Invaders - Menu");
			primaryStage.show();
		} catch (Exception e) {
			log.error("Error during start of application.", e);
		}
	}

	// Metoda pro prepnuti na obrazovku
	public void startGame(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/cz/vsb/fei/project/game/gameWindow.fxml"));
			Pane gameRoot = loader.load();

			Scene gameScene = new Scene(gameRoot, Constant.GAME_WIDTH, Constant.GAME_HEIGHT);
			gameSession = new GameSession(gameScene);

			GameController gameController = loader.getController();

			Canvas canvas = new Canvas(Constant.GAME_WIDTH, Constant.GAME_HEIGHT);
			gameRoot.getChildren().add(1, canvas);
			canvas.setFocusTraversable(false);

			drawingThread = new DrawingThread(canvas, gameSession, gameController);
			gameController.setGameSession(gameSession, drawingThread);


			primaryStage.setScene(gameScene);
			primaryStage.setTitle("Space Invaders");

			canvas.requestFocus();

			drawingThread.start();		

		} catch (Exception e) {
			log.error("Error during start of a game.", e);
		}
	}

	@Override
	public void stop() throws Exception {
		if (gameSession != null) {
			gameSession.getScoreManager().saveCurrentScore();
		}
		if (drawingThread != null) {
			drawingThread.stop();
		}
		super.stop();
	}
}

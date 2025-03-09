package cz.vsb.fei;

import lombok.extern.log4j.Log4j2;
import javafx.application.Application;
import cz.vsb.fei.game.AppGame;

@Log4j2
public class App {
	public static void main(String[] args) {
		log.info("Launching Java application.");
		// Spustí JavaFX aplikaci definovanou v AppGame
		Application.launch(AppGame.class, args);
	}
}

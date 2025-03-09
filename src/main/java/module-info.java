module cz.vsb.fei {
	requires static lombok;
	requires org.apache.logging.log4j;
	requires javafx.controls;
	requires javafx.fxml;

	exports cz.vsb.fei;       //App.java main class (entry point)
	exports cz.vsb.fei.game; //AppGame.java and game logic
	opens cz.vsb.fei.game to javafx.fxml; // open to reflexion
}
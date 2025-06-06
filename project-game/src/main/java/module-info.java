module cz.vsb.fei.project.game {
	// JavaFX moduly lze nechat (ty jsou modulární)
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;


	// Vlastní modul pro práci se soubory
	//requires cz.vsb.fei.project.storage;
	requires cz.vsb.fei.project.data;
    requires static lombok;
	requires org.apache.logging.log4j;
    requires java.net.http;
	requires com.fasterxml.jackson.databind;
	requires java.logging;


	// Exporty balícku
	exports cz.vsb.fei.project.game;
	opens cz.vsb.fei.project.game to javafx.fxml; //otevreni pro reflexi
}

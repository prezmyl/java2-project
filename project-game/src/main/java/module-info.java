module cz.vsb.fei.project.game {
	// JavaFX moduly lze nechat (ty jsou modulární)
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;


	// Vlastní modul pro práci se soubory
	requires cz.vsb.fei.project.file;

	// Exporty balíčků
	exports cz.vsb.fei.project.game;
	opens cz.vsb.fei.project.game to javafx.fxml;
}

package cz.vsb.fei.project.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class HealthDisplay implements DrawAble {
    private final Player.Health health;
    private GraphicsContext gc;
    private static final ResourceBundle BUNDLE =
            ResourceBundle.getBundle("messages", Locale.getDefault());

    public HealthDisplay(Player.Health health) {
        this.health = health;
    }

    @Override
    public void draw(GraphicsContext gc) {
        String fmt = BUNDLE.getString("health.lives");
        gc.setFill(Color.BLACK);
        gc.fillText(MessageFormat.format(fmt, this.health), 10, 20);
    }

    public void render(GraphicsContext gc, int lives) {
        // Custom logic to render health visually using GraphicsContext
        gc.clearRect(0, 0, 100, 20); // Example: Clear a section for health
        gc.fillText("Lives: " + lives, 10, 10); // Example: Draw text
    }


    public void update() {
        if (gc != null) {
            draw(gc);
        }
    }
}

package cz.vsb.fei.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class HealthDisplay implements DrawAble {
    private final Player.Health health;
    private GraphicsContext gc;

    public HealthDisplay(Player.Health health) {
        this.health = health;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillText("Lives: " + health.getLives(), 10, 20);
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

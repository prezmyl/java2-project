package cz.vsb.fei.project.game;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Barricade extends GameObject implements DrawAble, Collisionable{

    @Getter
    private static final double BARRICADE_WIDTH = 50;
    @Getter
    private static final double BARRICADE_HEIGHT = 15;
    private static final int MAX_HEALTH = 3;

    @Getter
    private int health;
    private boolean active = true;


    public Barricade(double x, double y) {
        super(x, y);
        this.health = MAX_HEALTH;
    }

    @Override
    public void draw(GraphicsContext gc) {

        switch (health) {
            case 3 -> gc.setFill(Color.GRAY); // Plně nepoškozená
            case 2 -> gc.setFill(Color.DARKGRAY); // Poškozená
            case 1 -> gc.setFill(Color.LIGHTGRAY); // Téměř zničená
            default -> {
                return; // Nevykresluj, pokud je barikáda zničená
            }
        }
        gc.fillRect(position.getX(), position.getY(), BARRICADE_WIDTH, BARRICADE_HEIGHT);
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), BARRICADE_WIDTH, BARRICADE_HEIGHT);
    }

    @Override
    public boolean intersect(Rectangle2D another) {
        return getBoundingBox().intersects(another);
    }

    @Override
    public void hitBy(Collisionable another) {
        if (another instanceof Bullet bullet && bullet.getType() == Bullet.Type.PLAYER) {
            log.debug("Barricade hit by bullet.");
            health--;
            if (health <= 0) {
                setActive(false); // Deaktivace bariéry při zásahu střelou
                log.debug("Barricade destroyed!");
            }
        }
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

}

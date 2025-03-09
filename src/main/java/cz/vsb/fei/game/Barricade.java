package cz.vsb.fei.game;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Barricade extends GameObject implements DrawAble, Collisionable{

    private static final double BARRICADE_WIDTH = 50;
    private static final double BARRICADE_HEIGHT = 15;
    private static final int MAX_HEALTH = 3;

    private int health;
    private boolean active = true;


    public Barricade(double x, double y) {
        super(x, y);
        this.health = MAX_HEALTH;
    }

    @Override
    public void draw(GraphicsContext gc) {
        //gc.setFill(Color.GRAY);
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

    public double getWidth() {
        return BARRICADE_WIDTH;
    }

    public double getHeight() {
        return BARRICADE_HEIGHT;
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
            System.out.println("Barricade hit by bullet.");
            health--;
            if (health <= 0) {
                setActive(false); // Deaktivace bariéry při zásahu střelou
                System.out.println("Barricade destroyed!");
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

    public int getHealth() {
        return health;
    }

}

package cz.vsb.fei.game;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bullet extends GameObject implements DrawableSimulable, Collisionable {
    public enum Type {PLAYER, ENEMY}
    protected static final double BULLET_WIDTH = 5;
    protected static final double BULLET_HEIGHT = 10;
    private boolean active = true;

    private final Type type;

    public Bullet(double x, double y, Type type) {
        super(x, y);
        this.speedY = type == Type.PLAYER ? - Constant.PLAYER_BULLET_SPEED : Constant.ENEMY_BULLET_SPEED;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public void simulate(double deltaT) {
        position = new Point2D(position.getX(), position.getY() + speedY * deltaT);
        if (position.getY() < 0 || position.getY() > Constant.GAME_HEIGHT) {
            setActive(false);
            System.out.println("Bullet removed: " + position);
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (this.type == Type.PLAYER) {
            gc.setFill(Color.BLACK);
            gc.fillRect(position.getX(), position.getY(), BULLET_WIDTH, BULLET_HEIGHT);
        }
        if (this.type == Type.ENEMY) {
            gc.setFill(Color.DARKORANGE);
            gc.fillRect(position.getX(), position.getY(), BULLET_WIDTH, BULLET_HEIGHT);
        }
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), BULLET_WIDTH, BULLET_HEIGHT);
    }

    @Override
    public boolean intersect(Rectangle2D another) {
        return getBoundingBox().intersects(another);
    }

    @Override
    public void hitBy(Collisionable another) {
        if (this.type == Type.ENEMY && (another instanceof Enemy)) {
            return;
        }
        this.setActive(false);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    public double getWidth() {
        return BULLET_WIDTH;
    }

    public double getHeight() {
        return BULLET_HEIGHT;
    }
}

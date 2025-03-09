package cz.vsb.fei.game;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Ground extends GameObject implements DrawAble, Collisionable {
    private final int WIDTH = Constant.GAME_WIDTH;
    private final int HEIGHT = 10;
    private boolean active = true;


    public Ground() {
        super(0, Constant.GAME_HEIGHT - 10);
        this.active = true;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.LIGHTGREEN);
        gc.fillRect(position.getX(), position.getY(), WIDTH, HEIGHT);
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), WIDTH, HEIGHT);
    }

    @Override
    public boolean intersect(Rectangle2D another) {
        return getBoundingBox().intersects(another);
    }

    @Override
    public void hitBy(Collisionable another) {

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


package cz.vsb.fei.game;

import javafx.geometry.Point2D;

public abstract class GameObject {
    protected Point2D position;
    protected double speedX, speedY;
    protected double width, height;

    public GameObject(double x, double y) {
        this.position = new Point2D(x, y);
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public double getWidth(){
        return width;
    }

    public double getHeight() {
        return height;
    }
}
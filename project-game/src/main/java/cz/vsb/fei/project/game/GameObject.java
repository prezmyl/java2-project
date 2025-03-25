package cz.vsb.fei.project.game;

import javafx.geometry.Point2D;
import lombok.Getter;
import lombok.Setter;


public abstract class GameObject {
    @Setter
    @Getter
    protected Point2D position;
    protected double speedX, speedY;
    @Getter
    protected double width;
    @Getter
    protected double height;
    public GameObject(double x, double y) {
        this.position = new Point2D(x, y);
    }

}
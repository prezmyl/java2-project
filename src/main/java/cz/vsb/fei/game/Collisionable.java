package cz.vsb.fei.game;

import javafx.geometry.Rectangle2D;

public interface Collisionable {
    Rectangle2D getBoundingBox();

    boolean intersect(Rectangle2D another);

    void hitBy(Collisionable another);

    boolean isActive();

    void setActive(boolean active);

    default void handleCollision(Collisionable another) {
        if (this.intersect(another.getBoundingBox())){
            another.hitBy(this);
            this.hitBy(another);
        }
    }
}

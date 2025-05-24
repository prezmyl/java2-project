package cz.vsb.fei.project.game;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Enemy extends GameObject implements DrawableSimulable, Collisionable{
    private static final double ENEMY_WIDTH = 30;
    private static final double ENEMY_HEIGHT = 20;

    
    private long lastBulletTime = 0;
    @Getter
    private final GameSession gameSession;
    @Getter
    @Setter
    private Direction direction ;

    private boolean active = true;

    public Enemy(double x, double y, GameSession gameSession) {
        super(x, y);
        this.speedY = 0;
        this.speedX = 100;
        this.direction = Direction.RIGHT;
        this.gameSession = gameSession;
    }

    @Override
    public void simulate(double deltaT) {
      // System.out.println("Simulating enemy at Y: " + position.getY());
       double speedMultiplier = gameSession.getSpeedMultiplier();
        double adjustedSpeed = speedX * deltaT * speedMultiplier;
       if (direction == Direction.RIGHT) {
            position = position.add(speedX * speedMultiplier  * deltaT, 0);
        } else {
            position = position.subtract(speedX * speedMultiplier * deltaT, 0);
        }

       // System.out.printf("Enemy Speed: %.2f | Adjusted Speed: %.2f | DeltaT: %.5f%n", speedX, adjustedSpeed, deltaT);

        if (!(this instanceof Ufo)) {
            if (position.getX() < 0) {
                position = new Point2D(0, position.getY());

                gameSession.updateAllEnemiesDirection(this.direction);
                gameSession.moveAllEnemiesDown(Constant.MOVE_STEP);

            } else if (position.getX() + getWidth() > Constant.GAME_WIDTH) {
                position = new Point2D(Constant.GAME_WIDTH - getWidth(), position.getY());

                gameSession.updateAllEnemiesDirection(this.direction);
                gameSession.moveAllEnemiesDown(Constant.MOVE_STEP);
            }
        }


        // System.out.println("Enemy position after move: X=" + position.getX() + ", Y=" + position.getY());

    }





    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillRect(position.getX(), position.getY(), ENEMY_WIDTH, ENEMY_HEIGHT);
    }

    public void shoot(long now) {
        if (now - lastBulletTime > getShootInterval()) { // Střelba v určitém intervalu
            Bullet bullet = new Bullet(
                    position.getX() + getWidth() / 2 - Bullet.BULLET_WIDTH / 2,
                    position.getY() + getHeight(),
                    Bullet.Type.ENEMY
            );
            gameSession.addBullet(bullet);
            lastBulletTime = now;
           // System.out.println("Enemy shooting at: " + position);
        }
    }

    private long getShootInterval() {
        double distanceToPlayer = gameSession.getPlayer().getPosition().getY() - position.getY();
        return (long) (Constant.BULLET_INTERVAL * (distanceToPlayer / Constant.GAME_HEIGHT));
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), ENEMY_WIDTH, ENEMY_HEIGHT);
    }

    @Override
    public boolean intersect(Rectangle2D another) {
        return getBoundingBox().intersects(another);
    }

    @Override
    public void hitBy(Collisionable another) {
        if (another instanceof Bullet bullet) {


            if (bullet.getType() == Bullet.Type.PLAYER){
                log.info("Enemy hit by player bullet.");
                gameSession.getScoreManager().increase(100);
                setActive(false);
            }
        }
        if (another instanceof Player) {
            log.info("Enemy hit by player ship.");
            gameSession.getScoreManager().increase(50);
            setActive(false);

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

    @Override
    public double getWidth() {
        return ENEMY_WIDTH;
    }

    @Override
    public double getHeight() {
        return ENEMY_HEIGHT;
    }

}

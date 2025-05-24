package cz.vsb.fei.project.game;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.extern.log4j.Log4j2;

import java.util.Random;

@Log4j2
public class Ufo extends Enemy {
    private static final double UFO_WIDTH = 50;
    private static final double UFO_HEIGHT = 20;
    private static final double UFO_SPEED = 100;
    private static final double UFO_CURVE_INTENSITY = 50;
    private static final long MIN_SHOT_INTERVAL = 1000; // 1 sekunda
    private static final double SHOOT_TOLERANCE_FACTOR = 10;
    private boolean movingRight;
    private double angle = 0;
    private final Random random;
    private final GameSession gameSession;
    private long lastShotTime = 0;


    public Ufo(double x, double y, GameSession gameSession, boolean movingRight) {
        super(x, y, gameSession);
        this.gameSession = gameSession;
        this.speedX = movingRight ? UFO_SPEED : -UFO_SPEED;
        this.movingRight = movingRight;
        this.random = new Random();
    }

    @Override
    public void simulate(double deltaT) {
        angle += deltaT * 2; // Změna úhlu pro zakřivení
        double curveY = Math.sin(angle) * UFO_CURVE_INTENSITY;

        double newY = Math.max(30, Math.min(80, position.getY() + curveY));
        position = new Point2D(position.getX() + speedX * deltaT, newY);

        checkShooting(System.currentTimeMillis());

        // change ufo a direction if hit the scene boarder
        if (position.getX() < 0 || position.getX() + UFO_WIDTH > Constant.GAME_WIDTH) {
            speedX = -speedX;
            movingRight = !movingRight;
        }
    }



    private void checkShooting(long now) {
        double playerX = gameSession.getPlayer().getPosition().getX();
        double playerWidth = gameSession.getPlayer().getWidth();
        double leftLine = playerX - 2 * playerWidth;
        double middleLine = playerX + playerWidth / 2;
        double rightLine = playerX + 2 * playerWidth;

        if (now - lastShotTime > MIN_SHOT_INTERVAL && (Math.abs(position.getX() - leftLine) < UFO_SPEED / SHOOT_TOLERANCE_FACTOR ||
                    Math.abs(position.getX() - middleLine) < UFO_SPEED / SHOOT_TOLERANCE_FACTOR ||
                    Math.abs(position.getX() - rightLine) < UFO_SPEED / SHOOT_TOLERANCE_FACTOR)) {
                shoot(now);
                lastShotTime = now;
            }

    }


    @Override
    public void shoot(long now) {
        Bullet bullet = new Bullet(position.getX() + UFO_WIDTH / 2, position.getY() + UFO_HEIGHT, Bullet.Type.ENEMY);
        log.debug("UFO vystřelilo střelu na pozici: {}", bullet.getPosition());
        gameSession.addBullet(bullet);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.PURPLE);
        gc.fillRect(position.getX(), position.getY(), UFO_WIDTH, UFO_HEIGHT);
    }

    @Override
    public void hitBy(Collisionable another) {
        if (another instanceof Bullet bullet) {
            if (bullet.getType() == Bullet.Type.PLAYER){
                log.debug("UFO hit by player bullet.");
                gameSession.getScoreClient().increaseScore(300); // Přidání bodů za zničení UFO
                setActive(false);
            }
        }
    }

}

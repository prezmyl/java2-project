package cz.vsb.fei.project.game;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Player extends GameObject implements DrawableSimulable, Collisionable {

    private static final double PLAYER_WIDTH = 40;
    private static final double PLAYER_HEIGHT = 20;
    private long lastBulletTime = 0;
    private Health health;
    private boolean active = true;

    public Player(double x, double y) {
        super(x, y);
        this.speedX = 100;
        this.health = new Health(Constant.MAX_HEALTH);
    }

    @Override
    public void simulate(double deltaT){
       /* if (position.getX() + speedX < Constant.GAME_WIDTH - PLAYER_WIDTH && position.getX() + speedX > 0) {
            position = new Point2D(position.getX() + speedX, position.getY());
        }
        else{
            this.speedX = -1 * speedX;
        }*/

    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.fillRect(position.getX(), position.getY(), PLAYER_WIDTH, PLAYER_HEIGHT);

    }

    public void moveLeft(double deltaT){
        if (position.getX() > 0) {
            position = new Point2D(position.getX() - speedX * deltaT, position.getY());
        }
    }


    public void moveRight(double deltaT){
        if (position.getX() + PLAYER_WIDTH < Constant.GAME_WIDTH) {
            position = new Point2D(position.getX() + speedX * deltaT, position.getY());
        }
    }

    /*public void shoot(){
        Bullet bullet = new Bullet(position.getX() + getWidth() / 2, position.getY() - Bullet.BULLET_HEIGHT);
    }*/
    public void shoot(GameSession gameSession, long now) {
        if (now - lastBulletTime > Constant.BULLET_INTERVAL) {
            Bullet bullet = new Bullet(
                    position.getX() + getWidth() / 2 - Bullet.BULLET_WIDTH / 2,
                    position.getY() - Bullet.BULLET_HEIGHT,
                    Bullet.Type.PLAYER
            );
            gameSession.addBullet(bullet); // Přidání střely do GameSession
            lastBulletTime = now;
            System.out.println("Player shooting at: " + position);
        }
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D(position.getX(), position.getY(), PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    @Override
    public boolean intersect(Rectangle2D another) {
        return getBoundingBox().intersects(another);
    }


    @Override
    public void hitBy(Collisionable another) {
        System.out.println("Player hit by: " + another.getClass().getSimpleName());
        if (another instanceof Enemy || another instanceof Bullet) {
            this.getHealth().decreaseLives();
            if (this.getHealth().getLives() <= 0) {
                setActive(false);
                System.out.println("Player is no longer active.");
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

    public Health getHealth(){
        return health;
    }

    public double getWidth() {
        return PLAYER_WIDTH;
    }

    public double getHeight() {
        return PLAYER_HEIGHT;
    }

    //Vnorena trida (zavislost Health na vnejsi tride Player)
    public class Health{
        private int lives;

        public Health(int lives){
            this.lives = lives;
        }

        public int getLives(){
            return lives;
        }

        public void decreaseLives() {
            if (lives > 0){
                lives--;
                System.out.println("Deacresing lives");
            }
        }

        public void increaseLives() {
            lives++;
        }
    }

}

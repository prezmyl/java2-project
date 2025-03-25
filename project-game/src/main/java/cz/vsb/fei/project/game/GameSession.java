package cz.vsb.fei.project.game;

import javafx.scene.Scene;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Log4j2
public class GameSession {
    @Getter
    private final Player player;
    @Getter
    private final ScoreManager scoreManager;
    @Getter
    private final List<Enemy> enemies;
    @Getter
    private final List<Barricade> barricades;
    @Getter
    private final List<Bullet> bullets;
    @Getter
    private final Scene scene;
    @Getter
    private final Ground ground;
    private long lastEnemyShotTime = 0;
    private final double SHOOT_PROBABILITY = 0.3; // 30% probabilty for shooting

    @Getter
    private double gameTime = 0; // Celkový herní čas, nevyuzito


    public GameSession(Scene scene) {
        this.scene = scene;
        this.player = new Player(Constant.PLAYER_START.getX(), Constant.PLAYER_START.getY());
        this.enemies = new ArrayList<>();
        this.barricades = new ArrayList<>();
        this.bullets = new ArrayList<>();
        this.scoreManager = new ScoreManager();
        this.ground = new Ground();

        initializeEnemies();
        initializeBarricades();
    }



    public void removeInactiveObjects() {
        int before = bullets.size();
        bullets.removeIf(bullet -> !bullet.isActive());
        int after = bullets.size();
        if (before != after) {
            log.debug("Odstraněno {}{}" ,(before - after), " střel.");
        }
        enemies.removeIf(enemy -> !enemy.isActive());
        barricades.removeIf(barricade -> !barricade.isActive());

    }

    //endGame condition check
    public boolean checkEnemyReachedGround() {
        return enemies.stream()
                .filter(enemy -> !(enemy instanceof Ufo))
                .anyMatch(enemy -> enemy.getBoundingBox().intersects(ground.getBoundingBox()));
    }

    public boolean checkIsEnemyDefeated() {
        return enemies.isEmpty();
    }

    // Uziti swith(ENUM), -> lambda
    public void updateAllEnemiesDirection(Direction direction) {
        Direction newDirection = switch (direction){
            case LEFT -> Direction.RIGHT;
            case RIGHT -> Direction.LEFT;
            case UP -> Direction.UP;
            case DOWN -> Direction.DOWN;
            default -> {
                log.error("Unexpected direction: {}", direction);
                throw new IllegalStateException("Unexpected value: " + direction);
            }
        };

        // this logic does not drive ufo
        for (Enemy enemy : enemies) {
            if (!(enemy instanceof Ufo)) {
                enemy.setDirection(newDirection);
            }
        }

    }

    public void moveAllEnemiesDown(double distance) {
        for (Enemy enemy : enemies) {
            if (!(enemy instanceof Ufo)) {
                enemy.setPosition(enemy.getPosition().add(0, distance)); // Posun dolů
            }
        }
    }


    public double getSpeedMultiplier() {
        double timeFactor = 1.0 + (gameTime / 60.0); // Každou minutu o 10 % rychlejší
        double enemyFactor = 1.0 + ((double) (Constant.INITIAL_ENEMY_COUNT - enemies.size()) / Constant.INITIAL_ENEMY_COUNT); // Čím méně nepřátel, tím rychlejší
        double distanceFactor = 1.0 + (1.0 - (getAverageEnemyDistanceToPlayer() / Constant.GAME_HEIGHT)); // Čím blíže jsou hráči, tím rychlejší
        double multiplier = timeFactor * enemyFactor * distanceFactor;

        //System.out.printf("Speed Multiplier: %.2f | Time Factor: %.2f | Enemy Factor: %.2f | Distance Factor: %.2f%n",
          //      multiplier, timeFactor, enemyFactor, distanceFactor);

        return multiplier;// Kombinace všech faktorů
    }


    private double getAverageEnemyDistanceToPlayer() {
        if (enemies.isEmpty()) return 1.0; // Pokud nejsou nepřátelé, vrátíme základní hodnotu

        double playerY = player.getPosition().getY();
        return enemies.stream()
                .mapToDouble(enemy -> Math.abs(playerY - enemy.getPosition().getY()))
                .average()
                .orElse(1.0);
    }



    public void enemyShoot(long now) {
        //limits the amount of the shooting
        if (now - lastEnemyShotTime < Constant.BULLET_INTERVAL) {
            return;
        }

        List<Enemy> shootingCandidates = enemies.stream()
                .filter(enemy -> Math.random() < SHOOT_PROBABILITY)
                .toList();

        if (!shootingCandidates.isEmpty()) {
            Enemy shooter = shootingCandidates.get(new Random().nextInt(shootingCandidates.size()));
            shooter.shoot(now);
            lastEnemyShotTime = now;
        }
    }



    private void initializeEnemies() {
        for (int i = 0; i < 11; i++) {
            enemies.add(new Enemy(100 + i * 60, 50, this)); // Předání GameSession
        }
    }

    private void initializeBarricades() {
        for (int i = 0; i < 5; i++) {
            barricades.add(new Barricade(200 + i * 100, 300));
        }
    }

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    public void updateGameTime(double deltaT) {
        gameTime += deltaT;
    }

    public void attemptSpawn() {
        if (hasActiveUfo()) {

         //   System.out.println("Attempting to spawn UFO, UFO exists already...");
            return;
        }

        log.info("Attempting to spawn UFO...");
        Random random = new Random();
        if (random.nextDouble() < 0.001) {
            boolean movingRight = random.nextBoolean();
            double startX = movingRight ? 0 : (Constant.GAME_WIDTH - Constant.UFO_WIDTH);
            addEnemy(new Ufo(startX, 50, this, movingRight));
        }
    }

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    public boolean hasActiveUfo() {
        return enemies.stream().anyMatch(enemy -> enemy instanceof Ufo);
    }


    public Stream<DrawableSimulable> getDrawableSimulables() {
        return Stream.concat(
                Stream.concat(
                        bullets.stream(),
                        enemies.stream()
                ),
                Stream.of(player)
        );
    }

    public Stream<DrawAble> getDrawables() {
        return Stream.concat(
                Stream.concat(
                        barricades.stream(),
                        Stream.of(player)
                ),
                Stream.of(ground)
        );
    }

    public Stream<Enemy> streamEnemies() {
        return enemies.stream();
    }


    public Stream<Barricade> streamBarricades() {
        return barricades.stream();
    }


}

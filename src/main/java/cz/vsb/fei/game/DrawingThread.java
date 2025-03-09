package cz.vsb.fei.game;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;


public class DrawingThread extends AnimationTimer {
	private final GraphicsContext gc;
	private final ArrayList<GameObject> gameObject = new ArrayList<>();
	private final Player player;
	private final GameStateObserver gameStateObserver;
	private final ScoreManager scoreManager;
	private final HealthDisplay healthDisplay;
	private long lastBulletTime = 0;
	private long lastTime;
	private final GameSession gameSession;
	private double deltaT = 0.016;
	private long currentNow;

	private long lastSecond = 0;
	private int frameCount = 0;

	public DrawingThread(Canvas canvas, GameSession gameSession, GameStateObserver gameStateObserver ) {
		this.gc = canvas.getGraphicsContext2D();
		this.player = gameSession.getPlayer();
		this.scoreManager = gameSession.getScoreManager();
		this.gameStateObserver = gameStateObserver;
		this.healthDisplay = new HealthDisplay(player.new Health(3));
		this.gameSession = gameSession;
	}

	@Override
	public void start() {
		lastTime = System.nanoTime(); // Inicializace času při startu
		super.start(); // Spuštění herní smyčky AnimationTimer
	}

	@Override
	public void handle(long now) {
		currentNow = now;
		deltaT = (now - lastTime) / 1e9;
		lastTime = now;
		gameSession.updateGameTime(deltaT);
	/*	System.out.println("deltaT: " + deltaT);

		// Počet snímků za sekundu
		long currentSecond = now / 1_000_000_000;
		if (lastSecond == currentSecond) {
			frameCount++;
		} else {
			System.out.println("FPS: " + frameCount);
			frameCount = 0;
			lastSecond = currentSecond;
		}*/

		if (gameSession.getPlayer().getHealth().getLives() <= 0 || gameSession.checkEnemyReachedGround()) {
			stop();
			gameStateObserver.onGameOver();
		}

		if (gameSession.checkIsEnemyDefeated()){
			stop();
			gameStateObserver.onGameWin();
		}

		gameSession.enemyShoot(now);

		gc.clearRect(0, 0, Constant.GAME_WIDTH, Constant.GAME_HEIGHT);


		gameSession.removeInactiveObjects();


		checkCollisions();



		// Vykreslení a simulace DrawableSimulable
		gameSession.getDrawableSimulables().forEach(obj -> {
			obj.simulate(deltaT);
			obj.draw(gc);
		});

		// Vykreslení DrawAble
		gameSession.getDrawables().forEach(obj -> obj.draw(gc));

		gameStateObserver.onScoreUpdate(scoreManager.getScore());
		gameStateObserver.onLivesUpdate(player.getHealth().getLives());

		gameSession.attemptSpawn();



	}

	private void checkCollisions() {
		List<Collisionable> activeObjects = new ArrayList<>();
		activeObjects.addAll(gameSession.getBullets());
		activeObjects.addAll(gameSession.getEnemies());
		activeObjects.add(player);
		activeObjects.addAll(gameSession.getBarricades());

		for (int i = 0; i < activeObjects.size(); i++) {
			for (int j = i + 1; j < activeObjects.size(); j++) {
				Collisionable col1 = activeObjects.get(i);
				Collisionable col2 = activeObjects.get(j);

				col1.handleCollision(col2);
			}
		}
	}




	public void addBullet(Bullet bullet) {
		gameObject.add(bullet);
	}

	public double getDeltaT(){
		return deltaT;
	}

	public long getCurrentNow() {
		return currentNow;
	}
}

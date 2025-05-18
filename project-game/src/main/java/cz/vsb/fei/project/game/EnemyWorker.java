package cz.vsb.fei.project.game;

import javafx.application.Platform;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
public class EnemyWorker  implements Runnable {

    private final Enemy enemy;
    private final Object lock;
    private final GameSession gameSession;
    private volatile boolean running = true;

    public EnemyWorker(Enemy enemy, GameSession session) {
        this.gameSession = session;
        this.enemy = enemy;
        this.lock = session.getGameLock();
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {

        try {
            while (running && !Thread.currentThread().isInterrupted() && enemy.isActive()){
                double deltaT = gameSession.getLastDeltaT();

                Platform.runLater(() -> {
                    synchronized (lock) {
                        enemy.simulate(deltaT);

                        if (enemy instanceof Ufo) {
                            gameSession.enemyShoot(System.currentTimeMillis());
                        }
                    }
                });

                Thread.sleep(2);

            }


        } catch(InterruptedException e) {
            running = false;
            Thread.currentThread().interrupt();
            log.debug("EnemyWorker thread interrupted for {}", enemy);
        }
    }
}

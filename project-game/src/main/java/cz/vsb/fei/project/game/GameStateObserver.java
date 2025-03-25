package cz.vsb.fei.project.game;

public interface GameStateObserver {
    void onScoreUpdate(int newScore);
    void onLivesUpdate(int remainingLives);

    void onGameOver();
    void onGameWin();
}

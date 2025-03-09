package cz.vsb.fei.game;

public interface GameStateObserver {
    void onScoreUpdate(int newScore);
    void onLivesUpdate(int remainingLives);

    void onGameOver();
    void onGameWin();
}

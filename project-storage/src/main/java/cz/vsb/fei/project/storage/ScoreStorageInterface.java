package cz.vsb.fei.project.storage;

import cz.vsb.fei.project.data.Score;

import java.util.List;

public interface ScoreStorageInterface {
    void init();
    void insertScore(Score score);

    List<Score> getAll();
    List<Score> getTopScores(int number);
    Score getScoreByName(String name);

}
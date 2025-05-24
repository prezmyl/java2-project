package cz.vsb.fei.project.storage;

import cz.vsb.fei.project.data.ScoreDTO;

import java.util.List;

public interface ScoreStorageInterface {
    void init();
    void insertScore(ScoreDTO score);

    List<ScoreDTO> getAll();
    List<ScoreDTO> getTopScores(int number);
    ScoreDTO getScoreByName(String name);

}
package cz.vsb.fei.project.storage;

import cz.vsb.fei.project.data.Score;
import lombok.extern.log4j.Log4j2;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Log4j2
public class FileManager implements ScoreStorageInterface {
    private static final String FILE_PATH = "high_scores.txt";

    public FileManager() {
        init();
    }

    @Override
    public void init() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                log.error("Error creating scores file: {}", e.getMessage(), e);
            }
        }
    }

    @Override
    public void insertScore(Score score) {
        List<Score> scores = getAll();
        scores.add(score);
        scores.sort(Comparator.comparingInt(Score::getPoints).reversed());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))){
            for (Score s : scores) {
                writer.write(s.getNick() + "," + s.getPoints());
                writer.newLine();
            }
        } catch (IOException e) {
            log.error("Error writing scores file: {}", e.getMessage(), e);
        }


    }

    @Override
    public List<Score> getAll() {
        List<Score> scores = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))){
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.trim().split(",");
                if (tokens.length == 2) {
                    try {
                        String nick = tokens[0];
                        int points = Integer.parseInt(tokens[1]);
                        scores.add(new Score(nick, points));

                    } catch (NumberFormatException e) {
                        log.warn("Invalid score in file: {}", line, e);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error reading scores file: {}", e.getMessage(), e);
        }

        return scores;
    }


    @Override
    public List<Score> getTopScores(int number) {
        return getAll().stream()
                .sorted(Comparator.comparingInt(Score::getPoints).reversed())
                .limit(number)
                .toList();
    }

    @Override
    public Score getScoreByName(String name) {
        return getAll().stream()
                .filter(score -> score.getNick().equalsIgnoreCase(name))
                .max(Comparator.comparingInt(Score::getPoints))
                .orElse(new Score());
    }


    public List<Integer> loadScores() {
        List<Integer> scores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Odstranění bílých znaků kolem řádku
                if (!line.isEmpty()) { // Ignorování prázdných řádků
                    try {
                        int score = Integer.parseInt(line);
                        if (score >= 0) { // Povolení nulového skóre
                            scores.add(score);
                        } else {
                            log.warn("Ignoring negative score: {}", line);
                        }
                    } catch (NumberFormatException e) {
                        log.error("Invalid score in file: {}", line, e);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error reading scores file: {}",e.getMessage(), e);
        }
        Collections.sort(scores, Collections.reverseOrder());
        return scores;
    }

    public void saveScore(int score) {
        List<Integer> scores = loadScores();
        scores.add(score);
        Collections.sort(scores, Collections.reverseOrder());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (int i = 0; i < Math.min(scores.size(), 5); i++) {
                writer.write(scores.get(i) + "\n");
            }
        } catch (IOException e) {
            log.error("Error writing scores file: ", e.getMessage(), e);
        }
    }
}

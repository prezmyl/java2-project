package cz.vsb.fei.game;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileManager {
    private static final String FILE_PATH = "high_scores.txt";

    public FileManager() {
        ensureFileExists();
    }

    private void ensureFileExists() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating scores file: " + e.getMessage());
            }
        }
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
                            System.err.println("Ignoring negative score: " + line);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid score in file: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading scores file: " + e.getMessage());
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
            System.err.println("Error writing scores file: " + e.getMessage());
        }
    }
}

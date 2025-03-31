package cz.vsb.fei.project.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class GameSettings {
    private static GameSettings instance;

    @Default
    private int gameWidth = 800;
    @Default
    private int gameHeight = 400;
    @Default
    private int initialEnemyCount = 10;
    @Default
    private double playerBulletSpeed = 300;
    @Default
    private double moveStep = 10;
    @Default
    private int maxHealth = 3;
    @Default
    private double enemyBulletSpeed = 200;
    @Default
    private double playerStartX = 100;
    @Default
    private double playerStartY = 350;

    public static void configure(GameSettings settings) {
        instance = settings;
    }

    public static GameSettings getInstance() {
        if (instance == null) {
            instance = GameSettings.builder().build();
        }
        return instance;
    }
}

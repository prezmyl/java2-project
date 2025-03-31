package cz.vsb.fei.project.data;

import jdk.jfr.DataAmount;
import lombok.*;

// DTO - Data transfer Object: jednoducha datova trida ma jen atributy
// zadne logicke metody
//slouzi k prenosu dat mezi vrstvami (napr. databaze <-> UI)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Score {
    private String nick;
    private int points;
}

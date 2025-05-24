package cz.vsb.fei.project.data;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScoreDTO {
    private Long id;
    private int points;
    private Long playerId;
    private String playerNickname;
    private Long gameSessionId;
    private String gameSessionName;
}

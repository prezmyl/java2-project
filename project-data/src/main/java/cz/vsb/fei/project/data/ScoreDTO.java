package cz.vsb.fei.project.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScoreDTO {
    private Long id;
    private int points;
    private Long playerId;
    private String playerNickname;
    private Long gameSessionId;
    private String gameSessionName;
}

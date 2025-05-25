package cz.vsb.fei.project.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameSessionDTO {
    private Long id;
    private String gameName;
    private LocalDateTime date; // nebo String, pokud tak máš v entitě

    // Pokud chceš mít detailní výpis skóre k session (ne nutné pro všechno)
    private List<ScoreDTO> scores;
}

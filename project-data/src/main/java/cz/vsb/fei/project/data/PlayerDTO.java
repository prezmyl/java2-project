package cz.vsb.fei.project.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class PlayerDTO {
    private Long id;
    private String nickname;

    // Když potřebuješ (volitelné)
    private String firstName;
    private String lastName;


}

package cz.vsb.fei.project.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;





@JsonIgnoreProperties(ignoreUnknown = true)

public class PlayerDTO {
    private Long id;
    private String nickname;

    public PlayerDTO() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "PlayerDTO(id=" + id + ", nickname=" + nickname + ")";
    }
}


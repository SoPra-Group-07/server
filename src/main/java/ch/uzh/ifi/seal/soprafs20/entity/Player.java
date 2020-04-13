package ch.uzh.ifi.seal.soprafs20.entity;

import org.mapstruct.BeanMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.nio.MappedByteBuffer;
import java.util.List;

/**
 *
 */
//@MappedSuperclass
@Entity
public abstract class Player implements Serializable{

    private static final long serialVersionUID = 1L;

    public Player(){}

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long playerId;


    @Column(name = "game_id")
    private long gameId;

    @Column
    private String playerName;

    @Column
    private float currentScore;

    @Column(name = "user_id")
    private long userId;

    // Todo: return void
    public abstract Clue giveClue(String word);


    public Long getPlayerId() {
        return playerId;
    }
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public long getGameId() {
        return gameId;
    }
    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }


    public float getCurrentScore() {
        return currentScore;
    }
    public void setCurrentScore(float currentScore) {
        this.currentScore = currentScore;
    }

    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
}

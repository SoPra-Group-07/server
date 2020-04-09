package ch.uzh.ifi.seal.soprafs20.entity;

import org.mapstruct.BeanMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 */
@Entity
public abstract class Player {
    public Player(){
        this.playerId = id;
        id++;
        this.currentScore = 0;
    }

    private static long id = 1;
    @Id
    private Long playerId;
    private String playerName;
    private boolean isGuessingPlayer;
    private float currentScore;

    // Todo: return void
    public abstract Clue giveClue(String word);

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean isGuessingPlayer() {
        return isGuessingPlayer;
    }

    public void setIsGuessingPlayer(boolean guessingPlayer) {
        isGuessingPlayer = guessingPlayer;
    }

    public float getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(float currentScore) {
        this.currentScore = currentScore;
    }
}

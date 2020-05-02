package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;


/**
 *Internal Player Representation
 *This class composes the internal representation of the player and defines how the player is stored in the database.
 */
@Entity
public abstract class Player implements Serializable{

    private static final long serialVersionUID = 1L;

    public Player(){/* empty constructor*/ }

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long playerId;


    @Column(name = "game_id")
    private long gameId;

    @Column
    private String playerName;

    @Column
    private double currentScore;

    @Column(name = "user_id")
    private long userId;


    public abstract String giveClue(String word) throws IOException;


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


    public double getCurrentScore() {
        return currentScore;
    }
    public void setCurrentScore(double currentScore) {
        this.currentScore = currentScore;
    }

    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
}

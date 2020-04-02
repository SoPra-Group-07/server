package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Internal Player Representation
 * This class composes the internal representation of the player and defines how the game is stored in the database.
 * Every variable will be mapped into a database field with the @Column annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes the primary key
 */
@Entity
@Table(name = "Player")
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long playerId;

    @Column(nullable = false, unique = true)
    private String playerName;

    @Column()
    private boolean isGuessingPlayer;

    @Column()
    private Game game;

    @Column()
    private float currentScore;

    @Column
    private User user;



    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long gameId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean isGuessingPlayer() {
        return isGuessingPlayer;
    }

    public Game getGame() {
        return game;
    }

    public float getCurrentScore() {
        return currentScore;
    }

    public User getUser() {
        return user;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setGuessingPlayer(boolean guessingPlayer) {
        isGuessingPlayer = guessingPlayer;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setCurrentScore(float currentScore) {
        this.currentScore = currentScore;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

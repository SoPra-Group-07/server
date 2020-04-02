package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Internal Guess Representation
 * This class composes the internal representation of the guess and defines how the guess is stored in the database.
 * Every variable will be mapped into a database field with the @Column annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes the primary key
 */
@Entity
@Table(name = "GUESS")
public class Guess implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long guessId;

    @Column(nullable = false, unique = true)
    private long playerId;

    @Column()
    private long gameRoundId;

    @Column()
    private String guess;

    @Column()
    private long guessingTime;

    @Column()
    private boolean isGuessRight;

    @Column
    private long startTime;

    @Column
    private long submitTime;


    public Long getGuessId() {
        return guessId;
    }

    public void setGuessId(Long guessId) {
        this.guessId = guessId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public long getGameRoundId() {
        return gameRoundId;
    }

    public void setGameRoundId(long gameRoundId) {
        this.gameRoundId = gameRoundId;
    }

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public long getGuessingTime() {
        return guessingTime;
    }

    public void setGuessingTime(long guessingTime) {
        this.guessingTime = guessingTime;
    }

    public boolean isGuessRight() {
        return isGuessRight;
    }

    public void setGuessRight(boolean guessRight) {
        isGuessRight = guessRight;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(long submitTime) {
        this.submitTime = submitTime;
    }
}
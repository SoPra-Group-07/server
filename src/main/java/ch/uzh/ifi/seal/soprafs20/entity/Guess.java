package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 *Internal Guess Representation
 *This class composes the internal representation of the guess and defines how the guess is stored in the database.
 */
@Entity
public class Guess extends Submission implements Serializable {

    private static final long serialVersionUID = 1L;

    public Guess(){ /* empty constructor*/ }

    @Column(name = "game_round_id")
    private Long gameRoundId;

    @Column
    private boolean correctGuess;

    public Long getGameRoundId() {
        return gameRoundId;
    }


    public void setGameRoundId(Long gameRoundId) {
        this.gameRoundId = gameRoundId;
    }

    public boolean isCorrectGuess() {
        return correctGuess;
    }

    public boolean getCorrectGuess() {
        return correctGuess;
    }
    public void setCorrectGuess(boolean correctGuess) {
        this.correctGuess = correctGuess;
    }



}
package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 */
@Entity
public class Guess extends Submission implements Serializable {

    private static final long serialVersionUID = 1L;

    public Guess(){ /* empty constructor*/ }

    @Column(name="game_round_id")
    private Long gameRoundId;

    @Column
    private boolean correctGuess;
    @Column
    private boolean didGuess;

    public boolean isCorrectGuess() {
        return correctGuess;
    }
    public void setCorrectGuess(boolean correctGuess) {
        this.correctGuess = correctGuess;
    }

    public boolean isDidGuess() {
        return didGuess;
    }
    public void setDidGuess(boolean didGuess) {
        this.didGuess = didGuess;
    }

    @Override
    public Long getGameRoundId() {
        return gameRoundId;
    }

    @Override
    public void setGameRoundId(Long gameRoundId) {
        this.gameRoundId = gameRoundId;
    }
}
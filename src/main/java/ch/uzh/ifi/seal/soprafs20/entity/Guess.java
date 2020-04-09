package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 */

public class Guess extends Submission {

    private static final long serialVersionUID = 1L;

    private boolean correctGuess;
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
}
package ch.uzh.ifi.seal.soprafs20.rest.dto.GameRound;

import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Clue;
import ch.uzh.ifi.seal.soprafs20.entity.Guess;
import ch.uzh.ifi.seal.soprafs20.entity.Submission;


import java.util.ArrayList;
import java.util.List;

public class GameRoundDTO {

    private long gameRoundId;
    private long gameId;
    private long guessingPlayerId;
    private String mysteryWord;
    private Card card;
    private ArrayList<Clue> submissions;
    private Guess guess;


    public Guess getGuess() {
        return guess;
    }

    public void setGuess(Guess guess) {
        this.guess = guess;
    }

    public long getGameRoundId() {
        return gameRoundId;
    }

    public void setGameRoundId(long gameRoundId) {
        this.gameRoundId = gameRoundId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getGuessingPlayerId() {
        return guessingPlayerId;
    }

    public void setGuessingPlayerId(long guessingPlayerId) {
        this.guessingPlayerId = guessingPlayerId;
    }

    public ArrayList<Clue> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(ArrayList<Clue> submissions) {
        this.submissions = submissions;
    }

    public String getMysteryWord() {
        return mysteryWord;
    }

    public void setMysteryWord(String mysteryWord) {
        this.mysteryWord = mysteryWord;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

}

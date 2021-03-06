package ch.uzh.ifi.seal.soprafs20.rest.dto.gameround;

import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Clue;
import ch.uzh.ifi.seal.soprafs20.entity.Guess;
import java.util.List;

public class GameRoundDTO {

    private long gameRoundId;
    private long gameId;
    private long guessingPlayerId;
    private int actualGameRound;
    private int totalGameRounds;
    private String mysteryWord;
    private Card card;
    private List<Clue> submissions;
    private Guess guess;

    public int getActualGameRound() {
        return actualGameRound;
    }

    public void setActualGameRound(int actualGameRound) {
        this.actualGameRound = actualGameRound;
    }

    public int getTotalGameRounds() {
        return totalGameRounds;
    }

    public void setTotalGameRounds(int totalGameRounds) {
        this.totalGameRounds = totalGameRounds;
    }

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

    public List<Clue> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<Clue> submissions) {
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

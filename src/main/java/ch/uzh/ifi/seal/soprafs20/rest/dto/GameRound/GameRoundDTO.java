package ch.uzh.ifi.seal.soprafs20.rest.dto.GameRound;

import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Clue;


import java.util.List;

public class GameRoundDTO {

    private long gameRoundId;
    private long gameId;
    private long guessingPlayerId;
    private String mysteryWord;
    private Card card;
    private List<Clue> submissions;




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

    public List<Clue> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<Clue> submissions) {
        this.submissions = submissions;
    }
}

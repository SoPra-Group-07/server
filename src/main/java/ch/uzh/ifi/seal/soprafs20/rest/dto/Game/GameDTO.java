package ch.uzh.ifi.seal.soprafs20.rest.dto.Game;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Player;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.List;

public class GameDTO {
    private Long gameId;
    private String gameName;
    private GameStatus gameStatus;
    private List<Player> players;
    private int numberOfPlayers;
    private int actualGameRoundIndex;
    private List<Card> cards;
    private boolean hasBot;
    private long adminPlayerId;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getActualGameRoundIndex() {
        return actualGameRoundIndex;
    }

    public void setActualGameRoundIndex(int actualGameRoundIndex) {
        this.actualGameRoundIndex = actualGameRoundIndex;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public boolean isHasBot() {
        return hasBot;
    }

    public void setHasBot(boolean hasBot) {
        this.hasBot = hasBot;
    }

    public long getAdminPlayerId() {
        return adminPlayerId;
    }

    public void setAdminPlayerId(long adminPlayerId) {
        this.adminPlayerId = adminPlayerId;
    }
}

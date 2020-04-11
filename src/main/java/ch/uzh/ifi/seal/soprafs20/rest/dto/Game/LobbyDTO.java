package ch.uzh.ifi.seal.soprafs20.rest.dto.Game;

import ch.uzh.ifi.seal.soprafs20.entity.Player;
import java.util.List;

public class LobbyDTO {
    private Long gameId;
    private String gameName;
    private List<Player> players;
    private int numberOfPlayers;

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
}
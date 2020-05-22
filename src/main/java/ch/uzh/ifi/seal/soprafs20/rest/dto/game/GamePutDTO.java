package ch.uzh.ifi.seal.soprafs20.rest.dto.game;

public class GamePutDTO {
    private long gameId;
    private long userId;

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
package ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerStatistic;


public class PlayerStatisticDTO {
    private Long playerStatisticId;
    private Long gameRoundId;
    private Long playerId;
    private float points;

    public Long getPlayerStatisticId() {
        return playerStatisticId;
    }

    public void setPlayerStatisticId(Long playerStatisticId) {
        this.playerStatisticId = playerStatisticId;
    }

    public Long getGameRoundId() {
        return gameRoundId;
    }

    public void setGameRoundId(Long gameRoundId) {
        this.gameRoundId = gameRoundId;
    }

    public float getPoints() {
        return points;
    }

    public void setPoints(float points) {
        this.points = points;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }
}

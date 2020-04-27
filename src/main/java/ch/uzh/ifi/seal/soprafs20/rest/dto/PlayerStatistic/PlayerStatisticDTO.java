package ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerStatistic;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class PlayerStatisticDTO {
    private Long playerStatisticId;
    private Long gameRoundId;
    private Long playerID;
    private int submissionTime;
    private int points;

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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}

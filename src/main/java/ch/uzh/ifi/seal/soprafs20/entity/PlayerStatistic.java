package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.io.Serializable;


/**
 *
 */

@Entity
public class PlayerStatistic implements Serializable {

    private static final long serialVersionUID = 1L;

    public PlayerStatistic() {/* empty constructor*/  }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playerStatisticId;

    @Column(name = "game_round_Id")
    private Long gameRoundId;

    @Column
    private float points;

    @Column
    private Long playerId;

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
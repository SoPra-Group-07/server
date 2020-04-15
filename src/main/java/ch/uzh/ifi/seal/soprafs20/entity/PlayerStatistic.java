package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.io.Serializable;


/**
 *
 */

@Entity
public abstract class PlayerStatistic implements Serializable {

    private static final long serialVersionUID = 1L;

    public PlayerStatistic() {/* empty constructor*/  }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playerStatisticId;

    @Column(name = "game_round_Id")
    private Long gameRoundId;

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
}
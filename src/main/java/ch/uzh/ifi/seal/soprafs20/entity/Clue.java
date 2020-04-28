package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.io.Serializable;


/**
 *
 */
@Entity
public class Clue extends Submission implements Serializable {

    private static final long serialVersionUID = 1L;

    public Clue(){/* empty constructor*/ }

    @Column()
    private boolean isDuplicate;

    @Column(name = "game_round_id")
    private Long gameRoundId;

    private String stemmedClue;

    public boolean getIsDuplicate() {
        return isDuplicate;
    }

    public void setDuplicate(boolean duplicate) {
        isDuplicate = duplicate;
    }

    public String getStemmedClue() {
        return stemmedClue;
    }

    public void setStemmedClue(String stemmedClue) {
        this.stemmedClue = stemmedClue;
    }


    public Long getGameRoundId() {
        return gameRoundId;
    }

    public void setGameRoundId(Long gameRoundId) {
        this.gameRoundId = gameRoundId;
    }
}
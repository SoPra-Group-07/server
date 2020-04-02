package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Internal Clue Representation
 * This class composes the internal representation of the clue and defines how the clue is stored in the database.
 * Every variable will be mapped into a database field with the @Column annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes the primary key
 */
@Entity
@Table(name = "CLUE")
public class Clue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long clueId;

    @Column(nullable = false, unique = true)
    private Long playerId;

    @Column()
    private Long gameRoundId;

    @Column()
    private String clue;

    @Column()
    private long clueingTime;

    @Column()
    private boolean duplicateClue;

    @Column
    private long startTime;

    @Column
    private long submitTime;


    public Long getClueId() {
        return clueId;
    }

    public void setClueId(Long clueId) {
        this.clueId = clueId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getGameRoundId() {
        return gameRoundId;
    }

    public void setGameRoundId(Long gameRoundId) {
        this.gameRoundId = gameRoundId;
    }

    public long getClueingTime() {
        return clueingTime;
    }

    public void setClueingTime(int clueingTime) {
        this.clueingTime = clueingTime;
    }

    public boolean isDuplicateClue() {
        return duplicateClue;
    }

    public void setDuplicateClue(boolean duplicateClue) {
        this.duplicateClue = duplicateClue;
    }

    public void setClueingTime(long clueingTime) {
        this.clueingTime = clueingTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(long submitTime) {
        this.submitTime = submitTime;
    }

    public String getClue() {
        return clue;
    }

    public void setClue(String clue) {
        this.clue = clue;
    }
}
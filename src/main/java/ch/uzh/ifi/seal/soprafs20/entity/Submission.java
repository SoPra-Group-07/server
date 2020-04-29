package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 *Internal Submission Representation
 *This class composes the internal representation of the submission and defines how the submission is stored in the database.
 */
@Table(name = "SUBMISSION")
@MappedSuperclass
public abstract class Submission implements Serializable {

    public Submission(){/* empty constructor */}


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long submissionId;

    @Column
    private Long playerId;

    @Column
    private String word;

    @Column
    private long duration;

    @Column
    private long startTime;

    @Column
    private long endTime;

    @Column
    private boolean didSubmit;


    public boolean getDidSubmit() {
        return didSubmit;
    }

    public void setDidSubmit(boolean didSubmit) {
        this.didSubmit = didSubmit;
    }


    public Long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

}

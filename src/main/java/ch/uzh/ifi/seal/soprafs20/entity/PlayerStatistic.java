package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.io.Serializable;


/**
 *Internal PlayerStatistic Representation
 *This class composes the internal representation of the playerStatistic and defines how the playerStatistic is stored in the database.
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
    private Long playerId;

    @Column
    private String playerName;

    @Column
    private double totalPoints;

    @Column
    private boolean wasGuessingPlayer;

    @Column
    private double duration;

    @Column
    private double durationPoints;

    @Column
    private String guess;

    @Column
    private boolean didNotClue = false;

    @Column
    private String clue;

    @Column
    private boolean duplicateClue = false;

    @Column
    private double duplicateCluePoints;

    @Column
    private double notCluePoints;

    @Column
    private boolean rightGuess = false;

    @Column
    private double rightGuessPoints;

    public boolean getWasGuessingPlayer() {
        return wasGuessingPlayer;
    }

    public void setWasGuessingPlayer(boolean wasGuessingPlayer) {
        this.wasGuessingPlayer = wasGuessingPlayer;
    }

    public boolean getDidNotClue() {
        return didNotClue;
    }

    public void setDidNotClue(boolean didNotClue) {
        this.didNotClue = didNotClue;
    }

    public double getNotCluePoints() {
        return notCluePoints;
    }

    public void setNotCluePoints(double notCluePoints) {
        this.notCluePoints = notCluePoints;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public double getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(double totalPoints) {
        this.totalPoints = totalPoints;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getDurationPoints() {
        return durationPoints;
    }

    public void setDurationPoints(double durationPoints) {
        this.durationPoints = durationPoints;
    }

    public boolean getDuplicateClue() {
        return duplicateClue;
    }

    public void setDuplicateClue(boolean duplicateClue) {
        this.duplicateClue = duplicateClue;
    }

    public double getDuplicateCluePoints() {
        return duplicateCluePoints;
    }

    public void setDuplicateCluePoints(double duplicateCluePoints) {
        this.duplicateCluePoints = duplicateCluePoints;
    }

    public boolean getRightGuess() {
        return rightGuess;
    }

    public void setRightGuess(boolean rightGuess) {
        this.rightGuess = rightGuess;
    }

    public double getRightGuessPoints() {
        return rightGuessPoints;
    }

    public void setRightGuessPoints(double rightGuessPoints) {
        this.rightGuessPoints = rightGuessPoints;
    }

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

    public double getPoints() {
        return totalPoints;
    }

    public void setPoints(double points) {
        this.totalPoints = points;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public String getClue() {
        return clue;
    }

    public void setClue(String clue) {
        this.clue = clue;
    }
}

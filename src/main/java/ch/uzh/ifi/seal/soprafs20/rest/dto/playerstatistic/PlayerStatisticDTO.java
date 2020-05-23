package ch.uzh.ifi.seal.soprafs20.rest.dto.playerstatistic;


public class PlayerStatisticDTO {
    // for all players
    private Long playerStatisticId;
    private Long gameRoundId;
    private Long playerId;
    private String playerName;
    private double totalPoints;
    private boolean wasGuessingPlayer;
    private boolean rightGuess;
    private double rightGuessPoints;
    private String guess;
    private int duration;
    private double durationPoints;

    //display these things only for clueingplayer
    private String clue;
    private boolean duplicateClue;
    private double duplicateCluePoints;
    private boolean didNotClue;
    private double notCluePoints;


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

    public boolean getWasGuessingPlayer() {
        return wasGuessingPlayer;
    }

    public void setWasGuessingPlayer(boolean wasGuessingPlayer) {
        this.wasGuessingPlayer = wasGuessingPlayer;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getDurationPoints() {
        return durationPoints;
    }

    public void setDurationPoints(double durationpoints) {
        this.durationPoints = durationpoints;
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

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }
}

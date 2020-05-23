package ch.uzh.ifi.seal.soprafs20.rest.dto.game;

public class GameStatisticDTO {

    private Long playerId;
    private String playerName;
    private double totalPoints;
    private int numberOfCorrectGuesses;
    private int numberOfDuplicateClues;

    public int getNumberOfCorrectGuesses() {
        return numberOfCorrectGuesses;
    }

    public void setNumberOfCorrectGuesses(int numberOfCorrectGuesses) {
        this.numberOfCorrectGuesses = numberOfCorrectGuesses;
    }

    public int getNumberOfDuplicateClues() {
        return numberOfDuplicateClues;
    }

    public void setNumberOfDuplicateClues(int numberOfDuplicateClues) {
        this.numberOfDuplicateClues = numberOfDuplicateClues;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
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
}

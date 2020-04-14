package ch.uzh.ifi.seal.soprafs20.rest.dto.GameRound;

public class GameRoundPutDTO {
    private Long gameRoundId;
    private int wordNumber;

    public Long getGameRoundId() {
        return gameRoundId;
    }

    public void setGameRoundId(Long gameRoundId) {
        this.gameRoundId = gameRoundId;
    }

    public int getWordNumber() {
        return wordNumber;
    }

    public void setWordNumber(int wordNumber) {
        this.wordNumber = wordNumber;
    }
}

package ch.uzh.ifi.seal.soprafs20.rest.dto.leaderboard;

public class LeaderboardDTO {
    private String username;
    private float highScore;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getHighScore() {
        return highScore;
    }

    public void setHighScore(float highScore) {
        this.highScore = highScore;
    }
}

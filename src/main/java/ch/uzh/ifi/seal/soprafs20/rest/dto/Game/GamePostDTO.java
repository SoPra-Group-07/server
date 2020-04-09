package ch.uzh.ifi.seal.soprafs20.rest.dto.Game;

public class GamePostDTO {
    private String gameName;
    private boolean hasBot;
    private long adminPlayerId;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public boolean isHasBot() {
        return hasBot;
    }

    public void setHasBot(boolean hasBot) {
        this.hasBot = hasBot;
    }

    public long getAdminPlayerId() {
        return adminPlayerId;
    }

    public void setAdminPlayerId(long adminPlayerId) {
        this.adminPlayerId = adminPlayerId;
    }
}

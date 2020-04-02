package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


public class Leaderboard {
    private static Leaderboard leaderboard;

    private Leaderboard() { }

    public static Leaderboard getInstance(){
        if (leaderboard == null){
            synchronized (Leaderboard.class) {
                if (leaderboard == null) {
                    leaderboard = new Leaderboard();
                }
            }
        }
        // methods for update and sort
        return leaderboard;
    }


    private Long leaderboardId;

    private List<User> users;


    public Long getLeaderboardId() {
        return leaderboardId;
    }

    public void setLeaderboardId(Long leaderboardId) {
        this.leaderboardId = leaderboardId;
    }

    public List<User> getLeaderboard() {
        return users;
    }

    public void setLeaderboard(List<User> users) {
        this.users = users;
    }
}
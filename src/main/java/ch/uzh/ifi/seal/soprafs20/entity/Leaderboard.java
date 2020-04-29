package ch.uzh.ifi.seal.soprafs20.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *Internal Leaderboard Representation
 */
public class Leaderboard {
    private static Leaderboard leaderboard;


    private Long leaderboardId;

    private List<User> users;


    private Leaderboard() {
        this.leaderboardId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        users = new ArrayList<>();
    }

    public static synchronized Leaderboard getInstance(){
        if (leaderboard == null) {
            leaderboard = new Leaderboard();}
        return leaderboard;
    }



    public Long getLeaderboardId() {
        return leaderboardId;
    }

    public void setLeaderboardId(Long leaderboardId) {
        this.leaderboardId = leaderboardId;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }


}
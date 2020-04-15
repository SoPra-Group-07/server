package ch.uzh.ifi.seal.soprafs20.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



public class Leaderboard {
    private static Leaderboard leaderboard;


    private Long leaderboardId;

    // Todo: only users that played a game!!
    private List<User> users;


    private Leaderboard() {
        this.leaderboardId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        users = new ArrayList<>();
    }

    public static Leaderboard getInstance(){
        if (leaderboard == null){
            synchronized (Leaderboard.class) {
                if (leaderboard == null) {
                    leaderboard = new Leaderboard();
                }
            }
        }
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

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }


}
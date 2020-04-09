package ch.uzh.ifi.seal.soprafs20.entity;

import java.util.ArrayList;
import java.util.UUID;



public class Leaderboard {
    private static Leaderboard leaderboard;
    //private static LeaderboardService leaderboardService = new LeaderboardService(UserRepository);

    private Long leaderboardId;

    // Todo: only users that played a game!!
    private ArrayList<User> users;


    private Leaderboard() {
        this.leaderboardId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        users = new ArrayList<User>();
    }

    public static Leaderboard getInstance(){
        if (leaderboard == null){
            synchronized (Leaderboard.class) {
                if (leaderboard == null) {
                    leaderboard = new Leaderboard();
                }
            }
        }
        //leaderboardService.fillUsers();
        //leaderboardService.sortByHighScore();

        // methods for sortByHighScore + fill list highscore not null
        return leaderboard;
    }



    public Long getLeaderboardId() {
        return leaderboardId;
    }

    public void setLeaderboardId(Long leaderboardId) {
        this.leaderboardId = leaderboardId;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }


}
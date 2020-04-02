package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.mapstruct.BeanMapping;
import org.springframework.context.annotation.Bean;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


public class Leaderboard {
    private static Leaderboard leaderboard;
    //private static LeaderboardService leaderboardService = new LeaderboardService(UserRepository);

    private Leaderboard() { }

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


    private Long leaderboardId;

    //only users that played a game!!
    private ArrayList<User> users;


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
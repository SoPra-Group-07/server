package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class LeaderboardService {

    private final UserRepository userRepository;

    @Autowired
    public LeaderboardService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private static Leaderboard leaderboard = Leaderboard.getInstance();

    public void sortByHighScore(){
        Collections.sort(leaderboard.getUsers(), User.userHighscoreComparator);
    }

    public ArrayList<User> getUsers(){
        return leaderboard.getUsers();
    }

    public void fillUsers(){
        List<User> linkedList = userRepository.findAll();
        ArrayList<User> list = new ArrayList<User>(linkedList);
        leaderboard.setUsers(list);
    }

}

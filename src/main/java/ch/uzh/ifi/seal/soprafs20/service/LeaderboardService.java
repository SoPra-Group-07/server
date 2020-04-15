package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.Leaderboard;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // Todo: delete logger
    //private final Logger log = LoggerFactory.getLogger(UserService.class);

    private static Leaderboard leaderboard = Leaderboard.getInstance();

    public void sortByHighScore(){
        Collections.sort(leaderboard.getUsers(), User.userHighscoreComparator);
    }

    public List<User> getUsers(){
        return leaderboard.getUsers();
    }

    public void fillUsers(){
        List<User> linkedList = userRepository.findAll();
        ArrayList<User> list = new ArrayList<>();

        // add only users who played a game
        for (User user : linkedList) {
            if (user.getNumberOfGamesPlayed() > 0){
                list.add(user);
            }
        }
        
        leaderboard.setUsers(list);

    }

}

package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Leaderboard.LeaderboardDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.LeaderboardService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class LeaderboardController {

    private final LeaderboardService leaderboardService;


    LeaderboardController(LeaderboardService leaderboardService) { this.leaderboardService = leaderboardService;
    }

    @GetMapping("/leaderboards")
    @ResponseStatus(HttpStatus.OK)                                                  // Status code 200 ->  if everything went well
    @ResponseBody
    public List<LeaderboardDTO> getAllUsersFromLeaderboard() {


        leaderboardService.fillUsers();
        leaderboardService.sortByHighScore();

        // fetch all users in the internal representation

        List<User> users = leaderboardService.getUsers();
        List<LeaderboardDTO> leaderboardDTOS = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            leaderboardDTOS.add(DTOMapper.INSTANCE.convertEntityToLeaderboardDTO(user));
        }
        return leaderboardDTOS;
    }

}

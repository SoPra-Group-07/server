package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.leaderboard.LeaderboardDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.LeaderboardService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Leaderboard Controller
 * This class is responsible for handling all REST request that are related to the leaderboard.
 * The controller will receive the request and delegate the execution to the LeaderboardService and finally return the result.
 */
@RestController
public class LeaderboardController {

    private final LeaderboardService leaderboardService;


    LeaderboardController(LeaderboardService leaderboardService) { this.leaderboardService = leaderboardService;
    }

    @GetMapping("/leaderboards")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LeaderboardDTO> getAllUsersFromLeaderboard() {


        leaderboardService.fillUsers();
        leaderboardService.sortByHighScore();

        List<User> users = leaderboardService.getUsers();
        List<LeaderboardDTO> leaderboardDTOS = new ArrayList<>();

        for (User user : users) {
            leaderboardDTOS.add(DTOMapper.INSTANCE.convertEntityToLeaderboardDTO(user));
        }
        return leaderboardDTOS;
    }

}

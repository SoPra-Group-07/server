package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerStatistic.PlayerStatisticDTO;
import ch.uzh.ifi.seal.soprafs20.service.PlayerStatisticService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class PlayerStatisticController {
    public final PlayerStatisticService playerStatisticService;


    PlayerStatisticController(PlayerStatisticService playerStatisticService){
        this.playerStatisticService = playerStatisticService;
    }

    /*
    @GetMapping("/games/{gameId}/gameRounds/{roundId}/gameRoundStatistics")
    @ResponseStatus(HttpStatus.OK)
    @RequestBody
    public PlayerStatisticDTO getGameRoundStatisticBy(@PathVariable long gameId, @PathVariable long roundId){
        playerStatisticService.computeGameRoundStatistic()
    }

     */
}

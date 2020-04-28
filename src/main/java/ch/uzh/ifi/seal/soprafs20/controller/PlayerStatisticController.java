package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.PlayerStatistic;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Game.GameGetOpenDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerStatistic.PlayerStatisticDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.PlayerStatisticService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PlayerStatisticController {

    public final PlayerStatisticService playerStatisticService;


    PlayerStatisticController(PlayerStatisticService playerStatisticService){
        this.playerStatisticService = playerStatisticService;
    }

    @GetMapping("gameRounds/{roundId}/gameRoundStatistics")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PlayerStatisticDTO> getGameRoundStatisticByRoundId(@PathVariable long roundId){

        List<PlayerStatistic> playerStatistics = playerStatisticService.computeGameRoundStatistic(roundId);
        List<PlayerStatisticDTO> playerStatisticDTO = new ArrayList<>();

        // convert each game to the API representation
        for (PlayerStatistic playerStatistic : playerStatistics) {
            playerStatisticDTO.add(DTOMapper.INSTANCE.convertEntityToPlayerStatisticDTO(playerStatistic));
        }
        return playerStatisticDTO;
    }



}

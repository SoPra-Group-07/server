package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.entity.PlayerStatistic;
import ch.uzh.ifi.seal.soprafs20.rest.dto.PlayerStatistic.PlayerStatisticDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameRoundService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * PlayerStatistic Controller
 * This class is responsible for handling all REST request that are related to the playerStatistic.
 * The controller will receive the request and delegate the execution to the PlayerStatisticService and finally return the result.
 */
@RestController
public class PlayerStatisticController {

    private final GameRoundService gameRoundService;


    PlayerStatisticController(GameRoundService gameRoundService){
        this.gameRoundService = gameRoundService;
    }

    @GetMapping("gameRounds/{roundId}/gameRoundStatistics")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PlayerStatisticDTO> getGameRoundStatisticByRoundId(@PathVariable long roundId){
        try{
            GameRound gameRound = gameRoundService.getGameRoundByRoundId(roundId);
            List<PlayerStatisticDTO> playerStatisticDTO = new ArrayList<>();

            // convert each game to the API representation
            for (PlayerStatistic playerStatistic : gameRound.getPlayerStatistic()) {
                playerStatisticDTO.add(DTOMapper.INSTANCE.convertEntityToPlayerStatisticDTO(playerStatistic));  }

            return playerStatisticDTO; }

        catch (NullPointerException e){
            PlayerStatistic playerStatistic = new PlayerStatistic();
            playerStatistic.setGameRoundId(0L);
            List<PlayerStatisticDTO> emptyList = new ArrayList<>();
            emptyList.add(DTOMapper.INSTANCE.convertEntityToPlayerStatisticDTO(playerStatistic));
            return emptyList;
        }

    }



}

package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Game.GameRoundDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameRoundService;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameRoundController {

    private final GameRoundService gameRoundService;
    private final GameService gameService;

    public GameRoundController(GameRoundService gameRoundService, GameService gameService) {
        this.gameRoundService = gameRoundService;
        this.gameService = gameService;
    }

    @PostMapping("/games/{gameId}/gameRounds")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameRoundDTO createGameRound(@RequestBody long gameId){
        Game game = gameService.getGameByGameId(gameId);
        GameRound newGameRound = gameRoundService.createNewGameRound(game);
        GameRoundDTO newGameRoundDTO = DTOMapper.INSTANCE.convertEntityToGameRoundDTO(newGameRound);
        return newGameRoundDTO;
    }

    @GetMapping("/gameRounds/{roundId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameRoundDTO getGameRoundByRoundId(@RequestBody long roundId){
        GameRound gameRoundByRoundId = gameRoundService.getGameRoundByRoundId(roundId);
        GameRoundDTO gameRoundDTO = DTOMapper.INSTANCE.convertEntityToGameRoundDTO(gameRoundByRoundId);
        return gameRoundDTO;
    }




}

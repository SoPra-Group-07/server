package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Clue;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameRound.GameRoundClueDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameRound.GameRoundDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameRound.GameRoundPutDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameRoundService;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    public GameRoundDTO getGameRoundByRoundId(@PathVariable Long roundId){
        GameRound gameRoundByRoundId = gameRoundService.getGameRoundByRoundId(roundId);
        GameRoundDTO gameRoundDTO = DTOMapper.INSTANCE.convertEntityToGameRoundDTO(gameRoundByRoundId);
        return gameRoundDTO;
    }

    @PutMapping("/gameRounds")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameRoundDTO chooseMisteryWord(@RequestBody GameRoundPutDTO gameRoundPutDTO) throws IOException {
        GameRound gameRound = DTOMapper.INSTANCE.convertGameRoundPutDTOtoEntity(gameRoundPutDTO);
        GameRound gameRoundByRoundId = gameRoundService.getGameRoundByRoundId(gameRound.getGameRoundId());
        gameRoundService.chooseMisteryWord(gameRoundByRoundId, gameRoundPutDTO.getWordNumber());
        GameRoundDTO gameRoundDTO = DTOMapper.INSTANCE.convertEntityToGameRoundDTO(gameRoundByRoundId);
        return gameRoundDTO;
    }


    @PutMapping("/gameRounds/clues")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameRoundDTO submitClue(@RequestBody GameRoundClueDTO gameRoundClueDTO){
        Clue clue = DTOMapper.INSTANCE.convertGameRoundClueDTOtoEntity(gameRoundClueDTO);
        GameRound gameRoundByRoundId = gameRoundService.getGameRoundByRoundId(clue.getGameRoundId());
        gameRoundService.submitClue(gameRoundByRoundId,clue.getWord(),clue.getPlayerId());
        GameRoundDTO gameRoundDTO = DTOMapper.INSTANCE.convertEntityToGameRoundDTO(gameRoundByRoundId);
        return gameRoundDTO;

    }




}

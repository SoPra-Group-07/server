package ch.uzh.ifi.seal.soprafs20.controller;
import ch.uzh.ifi.seal.soprafs20.entity.Clue;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.entity.Guess;
import ch.uzh.ifi.seal.soprafs20.rest.dto.gameround.GameRoundClueDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.gameround.GameRoundDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.gameround.GameRoundGuessDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.gameround.GameRoundPutDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameRoundService;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
/**
 * GameRound Controller
 * This class is responsible for handling all REST request that are related to the GameRound.
 * The controller will receive the request and delegate the execution to the GameRoundService and finally return the result.
 */
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
    public GameRoundDTO createGameRound(@PathVariable Long gameId){
        Game game = gameService.getGameByGameId(gameId);
        GameRound newGameRound = gameRoundService.startNewGameRound(game);
        return DTOMapper.INSTANCE.convertEntityToGameRoundDTO(newGameRound); }

    @GetMapping("/gameRounds/{roundId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameRoundDTO getGameRoundByRoundId(@PathVariable Long roundId){
        GameRound gameRoundByRoundId = gameRoundService.getGameRoundByRoundId(roundId);
        return DTOMapper.INSTANCE.convertEntityToGameRoundDTO(gameRoundByRoundId);}


    @PutMapping("/gameRounds")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameRoundDTO chooseMysteryWord(@RequestBody GameRoundPutDTO gameRoundPutDTO) throws IOException {

            GameRound gameRound = DTOMapper.INSTANCE.convertGameRoundPutDTOtoEntity(gameRoundPutDTO);
            GameRound gameRoundByRoundId = gameRoundService.getGameRoundByRoundId(gameRound.getGameRoundId());
            gameRoundByRoundId = gameRoundService.chooseMysteryWord(gameRoundByRoundId, gameRoundPutDTO.getWordNumber());
            gameRoundService.createCluesAndGuesses(gameRoundByRoundId);
            return DTOMapper.INSTANCE.convertEntityToGameRoundDTO(gameRoundByRoundId);


    }


    @PutMapping("/gameRounds/clues")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameRoundDTO submitClue(@RequestBody GameRoundClueDTO gameRoundClueDTO){
        Clue clue = DTOMapper.INSTANCE.convertGameRoundClueDTOtoEntity(gameRoundClueDTO);
        GameRound gameRoundByRoundId = gameRoundService.getGameRoundByRoundId(clue.getGameRoundId());
        gameRoundService.submitClue(gameRoundByRoundId, clue.getWord(),clue.getPlayerId());
        return DTOMapper.INSTANCE.convertEntityToGameRoundDTO(gameRoundByRoundId);
    }

    @PutMapping("/gameRounds/guesses")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameRoundDTO submitGuess(@RequestBody GameRoundGuessDTO gameRoundGuessDTO){
            Guess guess = DTOMapper.INSTANCE.convertGameRoundGuessDTOtoEntity(gameRoundGuessDTO);
            GameRound gameRoundByRoundId = gameRoundService.getGameRoundByRoundId(guess.getGameRoundId());
            gameRoundByRoundId = gameRoundService.submitGuess(gameRoundByRoundId, guess.getWord(), guess.getPlayerId());
            return DTOMapper.INSTANCE.convertEntityToGameRoundDTO(gameRoundByRoundId);
    }


    @GetMapping("games/lobby/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameRoundDTO getGameRoundId(@PathVariable String gameId) {
        long id;
        id = Long.parseLong(gameId);

        List<GameRound> gameRounds = gameRoundService.getGameRoundByGameId(id);
        GameRound lastGameRound = gameRounds.get(gameRounds.size()-1);

        return DTOMapper.INSTANCE.convertEntityToGameRoundDTO(lastGameRound);
    }






}

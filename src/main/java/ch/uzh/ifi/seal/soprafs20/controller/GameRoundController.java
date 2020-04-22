package ch.uzh.ifi.seal.soprafs20.controller;
import ch.uzh.ifi.seal.soprafs20.entity.Clue;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.entity.Guess;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameRound.GameRoundClueDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameRound.GameRoundDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameRound.GameRoundGuessDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameRound.GameRoundPutDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameRoundService;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.util.List;

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
    public GameRoundDTO chooseMisteryWord(@RequestBody GameRoundPutDTO gameRoundPutDTO) throws IOException, InterruptedException {
        //try{
            GameRound gameRound = DTOMapper.INSTANCE.convertGameRoundPutDTOtoEntity(gameRoundPutDTO);
            GameRound gameRoundByRoundId = gameRoundService.getGameRoundByRoundId(gameRound.getGameRoundId());
            gameRoundService.chooseMisteryWord(gameRoundByRoundId, gameRoundPutDTO.getWordNumber());
            return DTOMapper.INSTANCE.convertEntityToGameRoundDTO(gameRoundByRoundId);
      //  }
     //   catch (Exception e){
      //      throw new ResponseStatusException(HttpStatus.CONFLICT, "choose a number between 1 and 5 to choose the mystery word");
      //  }

    }


    @PutMapping("/gameRounds/clues")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameRoundDTO submitClue(@RequestBody GameRoundClueDTO gameRoundClueDTO){
        try{
        Clue clue = DTOMapper.INSTANCE.convertGameRoundClueDTOtoEntity(gameRoundClueDTO);
        GameRound gameRoundByRoundId = gameRoundService.getGameRoundByRoundId(clue.getGameRoundId());
        gameRoundService.submitClue(gameRoundByRoundId, clue.getWord(),clue.getPlayerId());
        return DTOMapper.INSTANCE.convertEntityToGameRoundDTO(gameRoundByRoundId);}

        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "you can not submit a number as a clue, please submit a string");
        }

    }

    @PutMapping("/gameRounds/guesses")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameRoundDTO submitGuess(@RequestBody GameRoundGuessDTO gameRoundGuessDTO){
        //try{
            Guess guess = DTOMapper.INSTANCE.convertGameRoundGuessDTOtoEntity(gameRoundGuessDTO);
            GameRound gameRoundByRoundId = gameRoundService.getGameRoundByRoundId(guess.getGameRoundId());
            gameRoundService.submitGuess(gameRoundByRoundId, guess.getWord(), guess.getPlayerId());
            return DTOMapper.INSTANCE.convertEntityToGameRoundDTO(gameRoundByRoundId);

        //catch (Exception e){
          //  throw new ResponseStatusException(HttpStatus.CONFLICT, "you can not submit a number as a clue, please submit a string");
        //}
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

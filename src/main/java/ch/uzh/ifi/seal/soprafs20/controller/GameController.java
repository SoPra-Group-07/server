package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Game.GameDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Game.GameGetOpenDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Game.GamePostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Game Controller
 * This class is responsible for handling all REST request that are related to the game.
 * The controller will receive the request and delegate the execution to the GameService and finally return the result.
 */
@RestController
public class GameController {

    private final GameService gameService;

    GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // Todo: check path
    @GetMapping("/games")
    @ResponseStatus(HttpStatus.OK)                                                  // Status code 200 ->  if everything went well
    @ResponseBody
    public List<GameGetOpenDTO> getGames(@RequestParam GameStatus gameStatus) {
        // fetch all games in the internal representation
        List<Game> games = gameService.getGameByGameStatus(gameStatus);                                  //creates list with all games in internal representation
        List<GameGetOpenDTO> userGetDTOs = new ArrayList<>();

        // convert each game to the API representation
        for (Game game : games) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToGameGetOpenDTO(game));
        }
        return userGetDTOs;
    }


    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameDTO createNewGame(@RequestBody GamePostDTO gamePostDTO){
        Game gameInput = DTOMapper.INSTANCE.convertGamePostDTOToEntity(gamePostDTO);
        Game newGame = gameService.createNewGame(gameInput);
        return DTOMapper.INSTANCE.convertEntityToGameDTO(newGame);
    }

}
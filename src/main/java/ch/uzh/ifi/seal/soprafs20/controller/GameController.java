package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Game.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameRound.GameRoundDTO;
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

    @GetMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<GameGetOpenDTO> getGamesByGameStatus(@RequestParam(required = false) GameStatus gameStatus) {
        if (gameStatus != null){
            List<Game> games = gameService.getGameByGameStatus(gameStatus);
            List<GameGetOpenDTO> gameGetDTOS = new ArrayList<>();
            // convert each game to the API representation
            for (Game game : games) {
                gameGetDTOS.add(DTOMapper.INSTANCE.convertEntityToGameGetOpenDTO(game));
            }
            return gameGetDTOS;
        } else {
            List<Game> games = gameService.getAllGames();
            List<GameGetOpenDTO> gameGetDTOS = new ArrayList<>();
            // convert each game to the API representation
            for (Game game : games) {
                gameGetDTOS.add(DTOMapper.INSTANCE.convertEntityToGameGetOpenDTO(game));
            }
            return gameGetDTOS;
        }
    }

    // create a new game
    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameDTO createNewGame(@RequestBody GamePostDTO gamePostDTO){
        Game gameInput = DTOMapper.INSTANCE.convertGamePostDTOToEntity(gamePostDTO);
        Game newGame = gameService.createNewGame(gameInput);
        return DTOMapper.INSTANCE.convertEntityToGameDTO(newGame);
    }

    // join a game
    @PutMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameDTO joinAGame(@RequestBody GamePutDTO gamePutDTO){
        //Game gameInput = DTOMapper.INSTANCE.convertGamePutDTOToEntity(gamePutDTO);
        Game joinedGame = gameService.joinGame(gamePutDTO.getGameId(), gamePutDTO.getUserId());
        return DTOMapper.INSTANCE.convertEntityToGameDTO(joinedGame);

    }

    // get all players from a game   -> do we really need this?
    @GetMapping("/games/{gameId}/players")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Player> getPlayersFromGameAndAdminPlayerId(@PathVariable String gameId){
        long id;
        id = Long.parseLong(gameId);

        Game game = gameService.getGameByGameId(id);
        List<Player> players = game.getPlayers();
        return players;

    }

    // return a game object with only the attributes needed in the lobby (place where you wait until a game starts)
    // will be requested every xx seconds from front-end
    @GetMapping("games/{gameId}/lobby")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyDTO getGameLobby(@PathVariable String gameId){
        long id;
        id = Long.parseLong(gameId);

        Game game = gameService.getGameByGameId(id);
        return DTOMapper.INSTANCE.convertEntityToLobbyDTO(game);
    }

    // start the game and return first gameround
    @PutMapping("games/lobby")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameRoundDTO startGame(@RequestBody GameStartPutDTO gameStartPutDTO) {
        Game gameInput = DTOMapper.INSTANCE.convertGameStartDTOToEntity(gameStartPutDTO);
        GameRound gameRound = gameService.startGame(gameInput.getGameId());
        return DTOMapper.INSTANCE.convertEntityToGameRoundDTO(gameRound);
    }


}

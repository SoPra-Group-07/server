package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.repository.CardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class GameServiceTest {

    @Mock
    GameRepository gameRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    PlayerRepository playerRepository;


    @InjectMocks
    private GameService gameService;
    @InjectMocks
    private User testUser;
    @InjectMocks
    private User testUser1;
    @InjectMocks
    private Game testGame;
    @InjectMocks
    private PhysicalPlayer testPlayer;
    @InjectMocks
    private PhysicalPlayer testPlayer1;
    @InjectMocks
    private FriendlyBot testFriedlyBot;
    @InjectMocks
    private List<Player> players = new ArrayList<>();
    @InjectMocks
    private Card testCard;
    @InjectMocks
    private GameRound gameRound;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // given
        testUser.setUserId(1L);
        testUser.setPassword("testPassword");
        testUser.setUsername("testUsername");
        testUser1.setUserId(2L);
        testUser1.setPassword("testPassword1");
        testUser1.setUsername("tesUsername1");

        testPlayer.setPlayerId(1L);
        testPlayer.setUserId(1L);
        testPlayer.setGameId(1L);
        testFriedlyBot.setPlayerId(2L);
        testFriedlyBot.setGameId(1L);



        testPlayer1.setPlayerId(3L);
        testPlayer1.setUserId(2L);

        players.add(testPlayer);
        players.add(testFriedlyBot);

        testFriedlyBot.setGameId(1L);
        testFriedlyBot.setPlayerId(1L);


        testGame.setGameName("superGame");
        testGame.setAdminPlayerId(1L);
        testGame.setHasBot(true);
        testGame.setGameId(1L);
        testGame.setNumberOfPlayers(2);
        testGame.setPlayers(players);
        testGame.setGameStatus(GameStatus.CREATED);



        testCard.setCardId(1L);
        testCard.setWord1("a");
        testCard.setWord2("b");
        testCard.setWord3("c");
        testCard.setWord4("d");
        testCard.setWord5("e");


    }

    /**
     * tests that createNewGame() creates a game with the given parameters
     */
    @Test
    public void Test_createGame() {
        // when -> object is being save in the gameRepository -> return the dummy testUser
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
        Mockito.when(playerRepository.save(Mockito.any())).thenReturn(testPlayer);
        Mockito.when(userRepository.findByUserId(testUser.getUserId())).thenReturn(testUser);

        Game createdGame = gameService.createNewGame(testGame);

        assertEquals(testGame.getGameId(), createdGame.getGameId());
        assertEquals(testGame.getActualGameRoundIndex(), createdGame.getActualGameRoundIndex());
        assertEquals(testGame.getGameName(), createdGame.getGameName());
        assertEquals(testGame.getGameStatus(), createdGame.getGameStatus());
        assertEquals(testGame.getNumberOfPlayers(), createdGame.getNumberOfPlayers());
        assertEquals(testGame.getAdminPlayerId(), createdGame.getAdminPlayerId());
        assertEquals(testGame.getHasBot(), createdGame.getHasBot());
        assertEquals(testGame.getPlayers(), createdGame.getPlayers());
    }

    /**
     * tests that createNewGame(Game game) throws a response status exception if the gameName is already taken
     */
    @Test
    public void Test_createGame_withAlreadyTaken_gameName() {
        // when -> object is being save in the gameRepository -> return the dummy testUser
        Mockito.when(gameRepository.findByGameName(testGame.getGameName())).thenReturn(testGame);

        String exceptionMessage = "GameName is already taken!";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->gameService.createNewGame(testGame), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }

    /**
     * test that joinGame(gameId, userId) joins the user to the desired game
     */
    @Test
    public void test_joinGame(){
        testUser1.setStatus(UserStatus.ONLINE);
        testUser.setStatus(UserStatus.ONLINE);

        Mockito.when(gameRepository.findByGameId(testGame.getGameId())).thenReturn(testGame);
        Mockito.when(userRepository.findByUserId(testUser1.getUserId())).thenReturn(testUser1);

        gameService.joinGame(testGame.getGameId(), testUser1.getUserId());

        assertEquals(3, testGame.getNumberOfPlayers());
        assertEquals(3, testGame.getPlayers().size());
        assertTrue(testGame.getHasBot());
        assertTrue(testGame.getPlayers().get(1) instanceof FriendlyBot || testGame.getPlayers().get(1) instanceof MaliciousBot);
        //assertEquals(testGame.getPlayers().get(2).getUserId(), testUser1.getUserId());

    }

    /**
     * test that joinGame(gameId, userId) throws an response status exception if game is already full
     */
    @Test
    public void test_joinGame_when_Game_full(){
        testUser1.setStatus(UserStatus.ONLINE);
        testUser.setStatus(UserStatus.ONLINE);
        Mockito.when(gameRepository.findByGameId(testGame.getGameId())).thenReturn(testGame);
        Mockito.when(userRepository.findByUserId(testUser1.getUserId())).thenReturn(testUser1);
        List<Player> players = new ArrayList<>();
        players.add(testPlayer);
        players.add(testPlayer);
        players.add(testPlayer);
        players.add(testPlayer);
        players.add(testPlayer);
        players.add(testPlayer);
        players.add(testPlayer);
        testGame.setPlayers(players);



        String exceptionMessage = "Game already full! Join another game.";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->gameService.joinGame(testGame.getGameId(), testUser1.getUserId()), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }

    /**
     * test that joinGame(gameId, userId) throws an response status exception if this player is already in the game
     */
    @Test
    public void test_joinGame_when_player_already_in_game(){
        testUser1.setStatus(UserStatus.ONLINE);
        testUser.setStatus(UserStatus.ONLINE);
        Mockito.when(gameRepository.findByGameId(testGame.getGameId())).thenReturn(testGame);
        Mockito.when(userRepository.findByUserId(testUser.getUserId())).thenReturn(testUser);
        Mockito.when(playerRepository.findByUserIdAndGameId(testUser.getUserId(), testGame.getGameId()) ).thenReturn(testGame.getPlayers().get(0));

        String exceptionMessage = "The user is already in the game!";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->gameService.joinGame(testGame.getGameId(), testUser.getUserId()), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());
        assertEquals(HttpStatus.NO_CONTENT, exception.getStatus());

    }

    @Test
    public void test_getGameByGameStatus(){
        List<Game> game = new ArrayList<>();
        game.add(testGame);
        Mockito.when(gameRepository.findAllByGameStatus(GameStatus.CREATED)).thenReturn(game);
        List<Game> games = gameService.getGameByGameStatus(GameStatus.CREATED);
        assertEquals(game.get(0).getGameId(), games.get(0).getGameId());
        assertEquals(game, games);
    }

    /**
     * test that leaveGame(long gameId, Long userId) removes the player from the desired game
     */
    @Test
    public void test_leaveGame(){
        Mockito.when(gameRepository.findByGameId(testGame.getGameId())).thenReturn(testGame);
        Mockito.when(playerRepository.findByUserIdAndGameId(testUser.getUserId(), testGame.getGameId())).thenReturn(testPlayer);
        gameService.leaveGame(testGame.getGameId(), testUser.getUserId());

        assertEquals(1, testGame.getPlayers().size());
        assertFalse(testGame.getPlayers().contains(testPlayer));
    }

    /**
     * test that leaveGame(long gameId, Long userId) throws a response status exception when player that not exist wants to leave a game
     */
    @Test
    public void test_leaveGame_whenPlayerNotInRepo(){
        User testUser3 = new User();
        testUser3.setUserId(3L);
        testUser3.setPassword("newUser");
        testUser3.setUsername("newUser");

        Player testPlayer3 = new PhysicalPlayer();
        testPlayer3.setPlayerId(5L);
        testPlayer3.setUserId(4L);

        Mockito.when(gameRepository.findByGameId(testGame.getGameId())).thenReturn(testGame);
        Mockito.when(playerRepository.findByUserIdAndGameId(testUser3.getUserId(), testGame.getGameId())).thenReturn(null);


        String exceptionMessage = "only players that exist can leave a game.";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->gameService.leaveGame(testGame.getGameId(), testUser.getUserId()), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }

    /**
     * test that leaveGame(long gameId, Long userId) throws a response status exception when game that wants to be left does not exist in gameRepo
     */
    @Test
    public void test_leaveGame_whenGameNotInGameRepo(){
        User testUser3 = new User();
        testUser3.setUserId(3L);
        testUser3.setPassword("newUser");
        testUser3.setUsername("newUser");

        Mockito.when(gameRepository.findByGameId(testGame.getGameId())).thenReturn(null);

        String exceptionMessage = "you can only leave a game that exists.";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->gameService.leaveGame(testGame.getGameId(), testUser.getUserId()), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }

    /**
     * test that leaveGame(long gameId, Long userId) throws a response status exception when a player wants to leave a game he is not in
     */
    @Test
    public void test_leaveGame_whenPlayerNotInGame(){
        User testUser3 = new User();
        testUser3.setUserId(3L);
        testUser3.setPassword("newUser");
        testUser3.setUsername("newUser");

        Player testPlayer3 = new PhysicalPlayer();
        testPlayer3.setPlayerId(5L);
        testPlayer3.setUserId(3L);

        Mockito.when(gameRepository.findByGameId(testGame.getGameId())).thenReturn(testGame);
        Mockito.when(playerRepository.findByUserIdAndGameId(testUser3.getUserId(), testGame.getGameId())).thenReturn(testPlayer3);


        String exceptionMessage = "you can not leave this game since you are not in it.";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->gameService.leaveGame(testGame.getGameId(), testUser3.getUserId()), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }


    /**
     * test that leaveGame(long gameId, Long userId) throws a response status exception when a player wants to leave a game that is running
     */
    @Test
    public void test_leaveGame_whenGameIsRunning(){
        testGame.setGameStatus(GameStatus.RUNNING);


        Mockito.when(gameRepository.findByGameId(testGame.getGameId())).thenReturn(testGame);
        Mockito.when(playerRepository.findByUserIdAndGameId(testUser.getUserId(), testGame.getGameId())).thenReturn(testPlayer);


        String exceptionMessage = "you can not leave this game since it is already running or finished.";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->gameService.leaveGame(testGame.getGameId(), testUser.getUserId()), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }

    /**
     * test that startGame(long gameId) throws a response status exception when a player wants to start a game that is already running
     */
    @Test
    public void test_startGame_whenGameIsAlready_Running(){
        testGame.setGameStatus(GameStatus.RUNNING);
        Mockito.when(gameRepository.findByGameId(testGame.getGameId())).thenReturn(testGame);

        String exceptionMessage = "You cannot start a game that is finished or already running.";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->gameService.startGame(testGame.getGameId()), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }






//Todo: this is more likely a test for GameRoundService
    /*
    @Test
    public void test_startGame(){
        testUser1.setStatus(UserStatus.ONLINE);
        testUser.setStatus(UserStatus.ONLINE);
        Mockito.when(gameRepository.findByGameId(testGame.getGameId())).thenReturn(testGame);
        Mockito.when(userRepository.findByUserId(testUser1.getId())).thenReturn(testUser1);
        players.add(testPlayer1);
        testGame.setPlayers(players);
        testGame.setCardIds(gameService.getRandomUniqueCardIds());

        Mockito.when(cardRepository.findByCardId(Mockito.anyLong())).thenReturn(testCard);

        GameRound gameRound = gameService.startGame(testGame.getGameId());

        System.out.println(gameRound);
    }


     */

}

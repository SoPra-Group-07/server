package ch.uzh.ifi.seal.soprafs20.service;


import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
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
    private Game testGame1;
    @InjectMocks
    private PhysicalPlayer testPlayer;
    @InjectMocks
    private PhysicalPlayer testPlayer1;
    @InjectMocks
    private FriendlyBot testFriedlyBot;
    @InjectMocks
    private List<Player> players = new ArrayList<>();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // given
        testUser.setId(1L);
        testUser.setPassword("testPassword");
        testUser.setUsername("testUsername");
        testUser1.setId(2L);
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



        testGame.setGameName("superGame");
        testGame.setAdminPlayerId(1);
        testGame.setHasBot(true);
        testGame.setGameId(1L);
        testGame.setNumberOfPlayers(2);
        testGame.setPlayers(players);

        testPlayer.setPlayerId(1L);
        testFriedlyBot.setGameId(1);
        testFriedlyBot.setPlayerId(1L);


    }

    /**
     * tests that createNewFame() creates a game with the given parameters
     */
    @Test
    public void Test_createGame() {
        // when -> object is being save in the userRepository -> return the dummy testUser
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
        Mockito.when(playerRepository.save(Mockito.any())).thenReturn(testPlayer);
        Mockito.when(userRepository.findByUserId(testUser.getUserId())).thenReturn(testUser);

        // when -> any object is being save in the userRepository -> return the dummy testUser
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
     * test that joinGame(gameId, userId) joins the user to the desired game
     */
    @Test
    public void test_joinGame(){
        testUser1.setStatus(UserStatus.ONLINE);
        testUser.setStatus(UserStatus.ONLINE);

        Mockito.when(gameRepository.findByGameId(testGame.getGameId())).thenReturn(testGame);
        Mockito.when(userRepository.findByUserId(testUser1.getId())).thenReturn(testUser1);

        gameService.joinGame(testGame.getGameId(), testUser1.getId());

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
        Mockito.when(userRepository.findByUserId(testUser1.getId())).thenReturn(testUser1);
        testGame.setNumberOfPlayers(7);

        //GameRound gameRound = gameService.startGame(testGame.getGameId());

        String exceptionMessage = "Game already full! Join another game.";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->gameService.joinGame(testGame.getGameId(), testUser1.getId()), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }

    @Test
    public void test_joinGame_when_player_already_in_game(){
        testUser1.setStatus(UserStatus.ONLINE);
        testUser.setStatus(UserStatus.ONLINE);
        Mockito.when(gameRepository.findByGameId(testGame.getGameId())).thenReturn(testGame);
        Mockito.when(userRepository.findByUserId(testUser.getId())).thenReturn(testUser);
        Mockito.when(playerRepository.findByUserId(testUser.getUserId())).thenReturn(testGame.getPlayers().get(0));

        String exceptionMessage = "The user is already in the game!";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->gameService.joinGame(testGame.getGameId(), testUser.getId()), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());
        assertEquals(HttpStatus.NO_CONTENT, exception.getStatus());

    }

}

package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private FriendlyBot testFriedlyBot;


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

        testGame.setGameId(2L);
        testGame.setActualGameRoundIndex(0);
        testGame.setAdminPlayerId(1);
        testGame.setGameStatus(GameStatus.CREATED);
        testGame.setHasBot(true);

        testPlayer.setPlayerId(4L);

        testFriedlyBot.setGameId(2);
        testFriedlyBot.setPlayerId(3L);


    }

    /**
     * tests that createUser() creates a user with the given parameters
     */
    @Test
    public void Test_createGame() {
        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
        Mockito.when(playerRepository.save(Mockito.any())).thenReturn(testPlayer);
        Mockito.when(userRepository.findByUserId(testUser.getUserId())).thenReturn(testUser);

        // when -> any object is being save in the userRepository -> return the dummy testUser
        Game createdGame = gameService.createNewGame(testGame);

        // then
        // Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());                              <--- Works also without

        assertEquals(testGame.getGameId(), createdGame.getGameId());
        assertEquals(testGame.getActualGameRoundIndex(), createdGame.getActualGameRoundIndex());
        assertEquals(testGame.getGameName(), createdGame.getGameName());
        assertEquals(testGame.getGameStatus(), createdGame.getGameStatus());
        assertEquals(testGame.getNumberOfPlayers(), createdGame.getNumberOfPlayers());
        assertEquals(testGame.getAdminPlayerId(), createdGame.getAdminPlayerId());
        assertEquals(testGame.getHasBot(), createdGame.getHasBot());
        assertEquals(testGame.getPlayers(), createdGame.getPlayers());
    }

    @Test
    public void test_joinGame(){

        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
        Mockito.when(playerRepository.save(testPlayer)).thenReturn(testPlayer);
        Mockito.when(playerRepository.save(testFriedlyBot)).thenReturn(testFriedlyBot);

        Mockito.when(userRepository.findByUserId(testUser.getUserId())).thenReturn(testUser);
        Mockito.when(userRepository.findByUserId(testUser1.getUserId())).thenReturn(testUser1);
        Mockito.when(gameRepository.findByGameId(testGame.getGameId())).thenReturn(testGame);
        //Mockito.when(playerRepository.save(testFriedlyBot))
        Game createdGame = gameService.createNewGame(testGame);


        gameService.joinGame(testUser1.getId(), createdGame.getGameId());

        assertEquals(3, createdGame.getNumberOfPlayers());
        assertEquals(3, createdGame.getPlayers().size());
        assertTrue(createdGame.getHasBot());


    }


}

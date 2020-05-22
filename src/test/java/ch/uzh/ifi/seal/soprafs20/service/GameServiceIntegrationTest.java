package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the GameResource REST resource.
 * @see GameService
 */
@WebAppConfiguration
@SpringBootTest
class GameServiceIntegrationTest {

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("playerRepository")
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setup() {
        playerRepository.deleteAll();
        gameRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createGame_validInputs_success() {

        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setStatus(UserStatus.ONLINE);
        User user1 = userRepository.save(user);
        userRepository.flush();


        Game testGame = new Game();
        testGame.setGameName("testGameName");
        testGame.setAdminPlayerId(user1.getUserId());
        testGame.setHasBot(true);

        Game createdGame = gameService.createNewGame(testGame);

        // then
        assertNotNull(gameRepository.findByGameId(createdGame.getGameId()));
        assertNotNull(createdGame.getGameId());
        assertEquals(testGame.getGameName(), createdGame.getGameName());
        assertEquals(testGame.getHasBot(), createdGame.getHasBot());
        assertEquals(GameStatus.CREATED, createdGame.getGameStatus());
        assertEquals(0, createdGame.getActualGameRoundIndex());
        assertEquals(13, createdGame.getCardIds().size());
        assertEquals(2, createdGame.getPlayers().size());
        assertEquals(2, createdGame.getNumberOfPlayers());
        assertTrue(createdGame.getPlayers().get(0) instanceof PhysicalPlayer);
        assertTrue(createdGame.getPlayers().get(1) instanceof MaliciousBot
                || createdGame.getPlayers().get(1) instanceof FriendlyBot);


    }

    @Test
    void createGame_alreadyExistingGameName_throws_RSE() {

        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setStatus(UserStatus.ONLINE);
        User user1 = userRepository.save(user);
        userRepository.flush();


        Game testGame = new Game();
        testGame.setGameName("testGameName");
        testGame.setAdminPlayerId(user1.getUserId());
        testGame.setHasBot(true);
        gameRepository.save(testGame);
        gameRepository.flush();


        Game testGame1 = new Game();
        testGame1.setGameName("testGameName");
        testGame1.setAdminPlayerId(user1.getUserId());
        testGame1.setHasBot(true);


        String exceptionMessage = "GameName is already taken!";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->gameService.createNewGame(testGame1), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());

    }


}
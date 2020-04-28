package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
public class GameServiceIntegrationTest {

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameService gameService;

    @BeforeEach
    public void setup() {
        gameRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setStatus(UserStatus.ONLINE);
        userRepository.save(user);
    }

    @Test
    void createGame_validInputs_success() {
        // given
        assertNull(gameRepository.findByGameName("testGameName"));


        Game testGame = new Game();
        testGame.setGameName("testGameName");
        testGame.setAdminPlayerId(1L);
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
}

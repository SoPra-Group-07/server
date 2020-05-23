package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRoundRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@WebAppConfiguration
@SpringBootTest
public class GameRoundIntegrationTest {

    @Qualifier("gameRoundRepository")
    @Autowired
    private GameRoundRepository gameRoundRepository;

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
    private GameRoundService gameRoundService;

    @Autowired
    private GameService gameService;

    @BeforeEach
    public void setup() {
        gameRoundRepository.deleteAll();
        playerRepository.deleteAll();
        gameRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void createGameRound_validInputs_success() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setStatus(UserStatus.ONLINE);
        User user1 = userRepository.save(user);
        userRepository.flush();

        List<Player> players = new ArrayList<>();
        Player player = new PhysicalPlayer();
        player.setPlayerId(1L);
        players.add(player);

        Game testGame = new Game();
        testGame.setGameId(1L);
        testGame.setPlayers(players);
        testGame.setGameName("testGameName");
        testGame.setAdminPlayerId(user1.getUserId());
        testGame.setHasBot(true);
        testGame.setCardIds(gameService.getRandomUniqueCardIds());
        testGame.setTotalGameRounds(13);
        testGame.setActualGameRoundIndex(0);

        GameRound createdGameRound = gameRoundService.startNewGameRound(testGame);

        // then
        assertNotNull(gameRoundRepository.findByGameRoundId(createdGameRound.getGameRoundId()));
        assertNotNull(createdGameRound.getGameRoundId());
        assertEquals(testGame.getGameId(), createdGameRound.getGameRoundId());
        assertNotNull(createdGameRound.getGuessingPlayerId());
        assertNull(createdGameRound.getMysteryWord());
        assertEquals(testGame.getTotalGameRounds(), createdGameRound.getTotalGameRounds());
    }
}
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the GameResource REST resource.
 * @see GameService
 */
@WebAppConfiguration
@SpringBootTest
class PlayerStatisticIntegrationTest {

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Qualifier("gameRoundRepository")
    @Autowired
    private GameRoundRepository gameRoundRepository;

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

    @Autowired
    private PlayerStatisticService playerStatisticService;

    @BeforeEach
    void setup() {
        gameRoundRepository.deleteAll();
        playerRepository.deleteAll();
        gameRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void computeGameRoundStatistic_afterGameRound_returnArray() throws IOException, InterruptedException {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("testPassword");
        user1.setStatus(UserStatus.ONLINE);
        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("testPassword");
        user2.setStatus(UserStatus.ONLINE);
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.flush();

        Game testGame = new Game();
        testGame.setGameName("testGameName");
        testGame.setAdminPlayerId(user1.getUserId());
        testGame.setHasBot(true);
        testGame.setCardIds(gameService.getRandomUniqueCardIds());
        testGame.setTotalGameRounds(13);
        testGame.setActualGameRoundIndex(0);
        Game testGame1 = gameRepository.save(testGame);
        gameRepository.flush();


        Player player1 = new PhysicalPlayer();
        player1.setUserId(1L);
        player1.setGameId(testGame1.getGameId());
        player1.setPlayerName(user1.getUsername());
        player1.setCurrentScore(0);

        Player player2 = new PhysicalPlayer();
        player2.setGameId(testGame1.getGameId());
        player2.setUserId(2L);
        player2.setPlayerName(user2.getUsername());
        player2.setCurrentScore(0);

        Player bot = new FriendlyBot();
        bot.setGameId(testGame1.getGameId());
        bot.setUserId(3L);
        bot.setPlayerName("Bot");
        bot.setCurrentScore(0);

        Player firstPlayer = playerRepository.save(player1);
        Player secondPlayer = playerRepository.save(player2);
        Player thirdPlayer = playerRepository.save(bot);
        playerRepository.flush();

        List<Player> players = new ArrayList<>();
        players.add(firstPlayer);
        players.add(secondPlayer);
        players.add(thirdPlayer);
        testGame.setPlayers(players);

        //setup
        GameRound gameRound = gameRoundService.startNewGameRound(testGame);
        gameRound = gameRoundService.chooseMysteryWord(gameRound, 3);
        gameRound.setGuessingPlayerId(secondPlayer.getPlayerId());
        gameRoundService.createCluesAndGuesses(gameRound);
        Clue clue1 = gameRoundService.submitClue(gameRound, "firstClue", firstPlayer.getPlayerId());
        Clue clue2 = gameRoundService.submitClue(gameRound, "botClue", thirdPlayer.getPlayerId());
        gameRoundService.submitGuess(gameRound, "guess", secondPlayer.getPlayerId());

        //computeGameRoundStatistic
        List<Clue> clues = new ArrayList<>();
        clues.add(clue1);
        clues.add(clue2);
        gameRound.setSubmissions(clues);
        List<PlayerStatistic> playerStatistics = playerStatisticService.computeGameRoundStatistic(gameRound);

        // then
        assertEquals(3, playerStatistics.size());
        assertTrue(playerStatistics.get(0).getWasGuessingPlayer());
        assertFalse(playerStatistics.get(1).getWasGuessingPlayer());
        assertFalse(playerStatistics.get(2).getDuplicateClue());
        assertFalse(playerStatistics.get(1).getDuplicateClue());
        assertFalse(playerStatistics.get(0).getRightGuess());
    }

}
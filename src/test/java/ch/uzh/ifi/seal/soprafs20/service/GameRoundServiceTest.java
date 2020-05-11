package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.repository.CardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRoundRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


public class GameRoundServiceTest {

    @Mock
    GameRoundRepository gameRoundRepository;
    @Mock
    CardRepository cardRepository;

    @InjectMocks
    private GameRoundService gameRoundService;
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

        testPlayer.setPlayerId(2L);
        testPlayer.setUserId(1L);
        testPlayer1.setPlayerId(3L);
        testPlayer1.setUserId(2L);

        testFriedlyBot.setPlayerId(1L);
        testFriedlyBot.setGameId(1L);


        players.add(testPlayer);
        players.add(testFriedlyBot);
        players.add(testPlayer1);

        testFriedlyBot.setGameId(1L);
        testFriedlyBot.setPlayerId(1L);


        testGame.setGameName("superGame");
        testGame.setAdminPlayerId(1L);
        testGame.setHasBot(true);
        testGame.setGameId(1L);
        testGame.setNumberOfPlayers(2);
        testGame.setPlayers(players);
        testGame.setCardIds(gameService.getRandomUniqueCardIds());
        testGame.setActualGameRoundIndex(0);

        gameRound.setCard(testCard);
        gameRound.setGameId(testGame.getGameId());
        gameRound.setGameRoundId(1L);


        testCard.setCardId(1L);
        testCard.setWord1("a");
        testCard.setWord2("b");
        testCard.setWord3("c");
        testCard.setWord4("d");
        testCard.setWord5("e");


    }

    /**
     * tests that createNewGameRound(Game game) creates a gameRound of the current game
     */
    @Test
    public void createNewGameRoundTest() {
        // when -> object is being save in the gameRoundRepository -> return the dummy gameRound
        Mockito.when(gameRoundRepository.save(Mockito.any())).thenReturn(gameRound);
        Mockito.when(cardRepository.findByCardId(Mockito.anyLong())).thenReturn(testCard);

        GameRound createdGameRound = gameRoundService.createNewGameRound(testGame);

        assertEquals(testGame.getGameId(), createdGameRound.getGameId());
        assertEquals(null, createdGameRound.getMysteryWord());
        assertEquals(null, createdGameRound.getGuess());
        assertEquals(false, createdGameRound.getEveryoneSubmitted());
        assertEquals(testCard, createdGameRound.getCard());
        assertEquals(1L, createdGameRound.getGameRoundId());
    }

    /**
     * test that startNewGameRound(Game game) starts the actual gameRound of the desired game
     */
    @Test
    public void startNewGameRoundTest(){
        testUser1.setStatus(UserStatus.ONLINE);
        testUser.setStatus(UserStatus.ONLINE);

        Mockito.when(gameRoundRepository.save(Mockito.any())).thenReturn(gameRound);
        Mockito.when(cardRepository.findByCardId(Mockito.anyLong())).thenReturn(testCard);

        GameRound startedGameRound = gameRoundService.startNewGameRound(testGame);

        assertEquals(testGame.getGameId(), startedGameRound.getGameId());
        assertEquals(null, startedGameRound.getMysteryWord());
        assertEquals(null, startedGameRound.getGuess());
        assertEquals(false, startedGameRound.getEveryoneSubmitted());
        assertEquals(testCard, startedGameRound.getCard());
        assertEquals(1L, startedGameRound.getGameRoundId());
        assertTrue(1L <= startedGameRound.getGuessingPlayerId());
        assertTrue(7L >= startedGameRound.getGuessingPlayerId());

    }

    /**
     * test that the guessingPlayer is correctly computed and in the range of the # of players playing
     * also it is asserted, that the guessingPlayer can not be a Malicious of FriendlyBot and has to be a PhysicalPlayer
     */
    @Test
    public void computeGuessingPlayerIdTest(){
        long guessingPlayerId = gameRoundService.computeGuessingPlayerIdTest(testGame);

        Player guessingPlayer = new PhysicalPlayer();
        for (Player player: players){
            if(player.getPlayerId() == guessingPlayerId){
                guessingPlayer = player;
            }
        }

        assertTrue(testGame.getNumberOfPlayers() >= guessingPlayerId);
        assertTrue(1L <= guessingPlayerId);
        assertFalse(guessingPlayer instanceof MaliciousBot);
        assertFalse(guessingPlayer instanceof FriendlyBot);
        assertTrue(guessingPlayer instanceof PhysicalPlayer);

    }

    /***
     *  a simple helper method test of getGameRoundByRoundId
     */
    @Test
    public void getGameRoundByRoundIdTest(){
        given(gameRoundRepository.findByGameRoundId(1L)).willReturn(gameRound);
        GameRound gameRoundByRoundId = gameRoundService.getGameRoundByRoundId(1L);
        assertTrue(gameRoundByRoundId.equals(gameRound));
    }

    /***
     *  tests that the correct mysteryWord is determined depending on the wordNumber provided by the Client
     */
    @Test
    public void chooseMysteryWordTest() throws IOException, InterruptedException {
        GameRound gameRoundWithMysteryWord = gameRoundService.chooseMysteryWord(gameRound,1);
        assertTrue(gameRoundWithMysteryWord.getMysteryWord().equals("a"));

        gameRoundWithMysteryWord = gameRoundService.chooseMysteryWord(gameRound,2);
        assertTrue(gameRoundWithMysteryWord.getMysteryWord().equals("b"));

        gameRoundWithMysteryWord = gameRoundService.chooseMysteryWord(gameRound,3);
        assertTrue(gameRoundWithMysteryWord.getMysteryWord().equals("c"));

        gameRoundWithMysteryWord = gameRoundService.chooseMysteryWord(gameRound,4);
        assertTrue(gameRoundWithMysteryWord.getMysteryWord().equals("d"));

        gameRoundWithMysteryWord = gameRoundService.chooseMysteryWord(gameRound,5);
        assertTrue(gameRoundWithMysteryWord.getMysteryWord().equals("e"));
    }


}

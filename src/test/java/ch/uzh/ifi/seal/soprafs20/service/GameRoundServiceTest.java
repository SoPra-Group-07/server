package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.repository.CardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRoundRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


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
        testGame.setCardIds(gameService.getRandomUniqueCardIds());
        testGame.setActualGameRoundIndex(0);
        testGame.setTotalGameRounds(13);

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
     * test that joinGame(gameId, userId) throws an response status exception if game is already full
     */
    @Test
    public void computeGuessingPlayerIdTest(){
        testUser1.setStatus(UserStatus.ONLINE);
        testUser.setStatus(UserStatus.ONLINE);
        Mockito.when(gameRoundRepository.save(Mockito.any())).thenReturn(gameRound);
        Mockito.when(cardRepository.findByCardId(Mockito.anyLong())).thenReturn(testCard);

        //to test private method
        long guessingPlayerId = gameRoundService.computeGuessingPlayerIdTest(testGame);

        assertTrue(testGame.getNumberOfPlayers() >= guessingPlayerId);
        assertTrue(1L <= guessingPlayerId);
    }

    /**
     * test that joinGame(gameId, userId) throws an response status exception if this player is already in the game
     */    /*

    @Test
    public void test_joinGame_when_player_already_in_game(){
        testUser1.setStatus(UserStatus.ONLINE);
        testUser.setStatus(UserStatus.ONLINE);
        Mockito.when(gameRepository.findByGameId(testGame.getGameId())).thenReturn(testGame);
        Mockito.when(userRepository.findByUserId(testUser.getUserId())).thenReturn(testUser);
        Mockito.when(playerRepository.findByUserId(testUser.getUserId())).thenReturn(testGame.getPlayers().get(0));

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

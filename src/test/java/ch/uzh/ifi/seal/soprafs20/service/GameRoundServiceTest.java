package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.repository.CardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRoundRepository;
import ch.uzh.ifi.seal.soprafs20.repository.*;
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
import static org.mockito.BDDMockito.given;


class GameRoundServiceTest {

    @Mock
    GameRoundRepository gameRoundRepository;
    @Mock
    CardRepository cardRepository;
    @Mock
    GameRepository gameRepository;
    @Mock
    GuessRepository guessRepository;
    @Mock
    ClueRepository clueRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    PlayerStatisticService playerStatisticService;

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
    @InjectMocks
    private Guess guess;
    @InjectMocks
    private Clue clue;


    @BeforeEach
    void setup() {
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
        testGame.setTotalGameRounds(13);

        gameRound.setCard(testCard);
        gameRound.setGameId(testGame.getGameId());
        gameRound.setGameRoundId(1L);
        gameRound.setGuess(null);


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
    void createNewGameRoundTest() {
        // when -> object is being save in the gameRoundRepository -> return the dummy gameRound
        Mockito.when(gameRoundRepository.save(Mockito.any())).thenReturn(gameRound);
        Mockito.when(cardRepository.findByCardId(Mockito.anyLong())).thenReturn(testCard);

        GameRound createdGameRound = gameRoundService.createNewGameRound(testGame);

        assertEquals(testGame.getGameId(), createdGameRound.getGameId());
        assertNull(createdGameRound.getMysteryWord());
        assertNull(createdGameRound.getGuess());
        assertFalse(createdGameRound.getEveryoneSubmitted());
        assertEquals(testCard, createdGameRound.getCard());
        assertEquals(1L, createdGameRound.getGameRoundId());
    }

    /**
     * tests that you can not create new GameRounds if the game has finished
     */
    @Test
    void createNewGameRound_noMoreGameRounds() {
        testGame.setGameStatus(GameStatus.FINISHED);


        String exceptionMessage = "Game has finished! No more gameRounds.";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gameRoundService.createNewGameRound(testGame), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());

        testGame.setGameStatus(GameStatus.RUNNING);
    }

    /**
     * tests that you can not create new GameRounds if the game has finished
     */
    @Test
    void createNewGameRound_noMoreGameRounds2() {
        testGame.setActualGameRoundIndex(14);


        String exceptionMessage = "Game has finished! No more gameRounds.";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gameRoundService.createNewGameRound(testGame), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());


    }

    /**
     * tests that you can not start new game rounds if the game has finished
     */
    @Test
    void startNewGameRound_noMoreGameRounds() {
        testGame.setActualGameRoundIndex(13);

        String exceptionMessage = "Game has finished! No more gameRounds.";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gameRoundService.startNewGameRound(testGame), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }

    /**
     * test that startNewGameRound(Game game) starts the actual gameRound of the desired game
     */
    @Test
    void startNewGameRoundTest() {
        testUser1.setStatus(UserStatus.ONLINE);
        testUser.setStatus(UserStatus.ONLINE);

        Mockito.when(gameRoundRepository.save(Mockito.any())).thenReturn(gameRound);
        Mockito.when(cardRepository.findByCardId(Mockito.anyLong())).thenReturn(testCard);

        GameRound startedGameRound = gameRoundService.startNewGameRound(testGame);

        assertEquals(testGame.getGameId(), startedGameRound.getGameId());
        assertNull(startedGameRound.getMysteryWord());
        assertNull(startedGameRound.getGuess());
        assertFalse(startedGameRound.getEveryoneSubmitted());
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
    void computeGuessingPlayerIdTest() {
        long guessingPlayerId = gameRoundService.computeGuessingPlayerIdTest(testGame);

        Player guessingPlayer = new PhysicalPlayer();
        for (Player player : players) {
            if (player.getPlayerId() == guessingPlayerId) {
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
    void getGameRoundByRoundIdTest() {
        given(gameRoundRepository.findByGameRoundId(1L)).willReturn(gameRound);
        GameRound gameRoundByRoundId = gameRoundService.getGameRoundByRoundId(1L);
        assertEquals(gameRoundByRoundId, gameRound);
    }

    /***
     *  tests that the correct mysteryWord is determined depending on the wordNumber provided by the Client
     */
    @Test
    void chooseMysteryWordTest() throws IOException, InterruptedException {
        GameRound gameRoundWithMysteryWord = gameRoundService.chooseMysteryWord(gameRound, 1);
        assertEquals("a", gameRoundWithMysteryWord.getMysteryWord());

        gameRoundWithMysteryWord = gameRoundService.chooseMysteryWord(gameRound, 2);
        assertEquals("b", gameRoundWithMysteryWord.getMysteryWord());

        gameRoundWithMysteryWord = gameRoundService.chooseMysteryWord(gameRound, 3);
        assertEquals("c", gameRoundWithMysteryWord.getMysteryWord());

        gameRoundWithMysteryWord = gameRoundService.chooseMysteryWord(gameRound, 4);
        assertEquals("d", gameRoundWithMysteryWord.getMysteryWord());

        gameRoundWithMysteryWord = gameRoundService.chooseMysteryWord(gameRound, 5);
        assertEquals("e", gameRoundWithMysteryWord.getMysteryWord());

        String exceptionMessage = "Choose a number between 1-5";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gameRoundService.chooseMysteryWord(gameRound, 8), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());

    }

    /***
     * tests that the guess and clues are correctly created, startTime is set, endTime is not yet determined
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    void createCluesAndGuessesTest() throws IOException, InterruptedException {
        given(gameRepository.findByGameId(gameRound.getGameId())).willReturn(testGame);
        given(guessRepository.save(Mockito.any())).willReturn(guess);
        given(clueRepository.save(Mockito.any())).willReturn(clue);
        //given(testFriedlyBot.giveClue(Mockito.anyString())).willReturn("BotClue");

        given(gameRoundService.getGameRoundByRoundId(1L)).willReturn(gameRound);
        given(guessRepository.findByPlayerIdAndGameRoundId(1L, 1L)).willReturn(guess);

        long guessingPlayerId = gameRoundService.computeGuessingPlayerIdTest(testGame);
        gameRound.setGuessingPlayerId(guessingPlayerId);
        gameRound.setMysteryWord("mysteryWord");

        gameRoundService.createCluesAndGuesses(gameRound);

        assertNotNull(gameRound.getGuess());
        assertFalse(gameRound.getGuess().getDidSubmit());
        assertEquals(gameRound.getGameRoundId(), gameRound.getGuess().getGameRoundId());
        assertEquals(0, gameRound.getGuess().getStartTime());
        assertEquals(0, gameRound.getGuess().getEndTime());
    }


    /***
     * tests that the clue is submitted and therefore marked as didSubmit to true, the endTime is being
     * tracked and the duration automatically computed, if a clue is a duplicate this is also being
     * checked by an assertion, and the correct word is being stored
     * Further it checks that a ResponseStatusException is being thrown, in case that an guessingPlayer tries submitting
     * a clue
     */
    @Test
    void submitClue() {
        given(clueRepository.findByPlayerIdAndGameRoundId(3L, 1L)).willReturn(clue);
        given(gameRoundRepository.findByGameRoundId(1L)).willReturn(gameRound);
        given(gameRepository.findByGameId(1L)).willReturn(testGame);

        long guessingPlayerId = gameRoundService.computeGuessingPlayerIdTest(testGame);
        gameRound.setGuessingPlayerId(guessingPlayerId);
        Clue submittedClue = gameRoundService.submitClue(gameRound, "awesomeClue", 3L);


        assertTrue(submittedClue.getDidSubmit());
        assertFalse(submittedClue.getIsDuplicate());
        assertNotEquals(0, submittedClue.getEndTime());
        assertNotEquals(0, submittedClue.getDuration());
        assertEquals("awesomeClue", submittedClue.getWord());


        String exceptionMessage = "a guessing player can not submit a clue!";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gameRoundService.submitClue(gameRound, "illegalClue", 2L), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }

    /**
     * tests that did submit is set to false if player does not submit a clue
     */
    @Test
    void submitClue_noClue() {
        given(clueRepository.findByPlayerIdAndGameRoundId(3L, 1L)).willReturn(clue);
        given(gameRoundRepository.findByGameRoundId(1L)).willReturn(gameRound);
        given(gameRepository.findByGameId(1L)).willReturn(testGame);

        long guessingPlayerId = gameRoundService.computeGuessingPlayerIdTest(testGame);
        gameRound.setGuessingPlayerId(guessingPlayerId);
        Clue submittedClue = gameRoundService.submitClue(gameRound, "noClue", 3L);


        assertFalse(submittedClue.getDidSubmit());
        assertFalse(submittedClue.getIsDuplicate());
        assertEquals("noClue", submittedClue.getWord());

    }


    /***
     * tests that the guess is submitted and therefore marked as didSubmit to true, the endTime is being
     * tracked and the duration automatically computed, the word obviously is being stored in the guess object
     */
    @Test
    void submitGuess() {
        given(guessRepository.findByPlayerIdAndGameRoundId(2L, 1L)).willReturn(guess);
        given(gameRoundRepository.findByGameRoundId(1L)).willReturn(gameRound);
        given(gameRepository.findByGameId(1L)).willReturn(testGame);
        given(userRepository.findByUserId(1)).willReturn(testUser);
        given(userRepository.findByUserId(2)).willReturn(testUser1);

        List<PlayerStatistic> playerStatistics = new ArrayList<>();
        given(playerStatisticService.computeGameRoundStatistic(gameRound)).willReturn(playerStatistics);


        long guessingPlayerId = gameRoundService.computeGuessingPlayerIdTest(testGame);
        gameRound.setGuessingPlayerId(guessingPlayerId);
        gameRound.setMysteryWord("mysteryWord");
        gameRound = gameRoundService.submitGuess(gameRound, "awesomeGuess", 2L);
        Guess submittedGuess = gameRound.getGuess();

        assertTrue(submittedGuess.getDidSubmit());
        assertNotEquals(0, submittedGuess.getEndTime());
        assertNotEquals(0, submittedGuess.getDuration());
        assertEquals("awesomeGuess", submittedGuess.getWord());


        String exceptionMessage = "a clueing player can not submit a guess!";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> gameRoundService.submitGuess(gameRound, "illegalGuess", 3L), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }

    /**
     * tests that did submit is set to false when a player does not submit a guess
     */
    @Test
    void submitGuess_noGuess() {
        given(guessRepository.findByPlayerIdAndGameRoundId(2L, 1L)).willReturn(guess);
        given(gameRoundRepository.findByGameRoundId(1L)).willReturn(gameRound);
        given(gameRepository.findByGameId(1L)).willReturn(testGame);
        given(userRepository.findByUserId(1)).willReturn(testUser);
        given(userRepository.findByUserId(2)).willReturn(testUser1);

        List<PlayerStatistic> playerStatistics = new ArrayList<>();
        given(playerStatisticService.computeGameRoundStatistic(gameRound)).willReturn(playerStatistics);


        long guessingPlayerId = gameRoundService.computeGuessingPlayerIdTest(testGame);
        gameRound.setGuessingPlayerId(guessingPlayerId);
        gameRound.setMysteryWord("mysteryWord");
        gameRound = gameRoundService.submitGuess(gameRound, "noGuess", 2L);
        Guess submittedGuess = gameRound.getGuess();

        assertFalse(submittedGuess.getDidSubmit());
        assertEquals("noGuess", submittedGuess.getWord());
    }


    /**
     * tests that correct guess is set to false if the guess was not correct
     */
    @Test
    void submitGuess_wrongGuess() {
        given(guessRepository.findByPlayerIdAndGameRoundId(2L, 1L)).willReturn(guess);
        given(gameRoundRepository.findByGameRoundId(1L)).willReturn(gameRound);
        given(gameRepository.findByGameId(1L)).willReturn(testGame);
        given(userRepository.findByUserId(1)).willReturn(testUser);
        given(userRepository.findByUserId(2)).willReturn(testUser1);

        List<PlayerStatistic> playerStatistics = new ArrayList<>();
        given(playerStatisticService.computeGameRoundStatistic(gameRound)).willReturn(playerStatistics);


        long guessingPlayerId = gameRoundService.computeGuessingPlayerIdTest(testGame);
        gameRound.setGuessingPlayerId(guessingPlayerId);
        gameRound.setMysteryWord("mysteryWord");
        gameRound = gameRoundService.submitGuess(gameRound, "wrongGuess", 2L);
        Guess submittedGuess = gameRound.getGuess();

        assertTrue(submittedGuess.getDidSubmit());
        assertEquals("wrongGuess", submittedGuess.getWord());
        assertFalse(submittedGuess.getCorrectGuess());
    }


    /**
     * tests that correct guess is set to true if the guess was correct
     */
    @Test
    void submitGuess_rightGuess() {
        given(guessRepository.findByPlayerIdAndGameRoundId(2L, 1L)).willReturn(guess);
        given(gameRoundRepository.findByGameRoundId(1L)).willReturn(gameRound);
        given(gameRepository.findByGameId(1L)).willReturn(testGame);
        given(userRepository.findByUserId(1)).willReturn(testUser);
        given(userRepository.findByUserId(2)).willReturn(testUser1);

        List<PlayerStatistic> playerStatistics = new ArrayList<>();
        given(playerStatisticService.computeGameRoundStatistic(gameRound)).willReturn(playerStatistics);


        long guessingPlayerId = gameRoundService.computeGuessingPlayerIdTest(testGame);
        gameRound.setGuessingPlayerId(guessingPlayerId);
        gameRound.setMysteryWord("mysteryWord");
        gameRound = gameRoundService.submitGuess(gameRound, "mysteryWord", 2L);
        Guess submittedGuess = gameRound.getGuess();

        assertTrue(submittedGuess.getDidSubmit());
        assertEquals("mysteryWord", submittedGuess.getWord());
        assertTrue(submittedGuess.getCorrectGuess());
    }

    /**
     * test that duplicate and stemmed clues that are equal to the mystery word get marked as
     * duplicate clue
     */
    @Test
    void checkDuplicates(){

        Clue clue = new Clue();
        clue.setPlayerId(testPlayer.getPlayerId());
        clue.setWord("duplicate");
        clue.setSubmissionId(1L);


        Clue clue1 = new Clue();
        clue1.setWord("duplicate");
        clue1.setSubmissionId(2L);

        Clue clue2 = new Clue();
        clue2.setWord("mysterywords");
        clue2.setSubmissionId(3L);

        Clue clue3 = new Clue();
        clue3.setWord("mYsTerYword");
        clue3.setSubmissionId(4L);

        Clue clue4 = new Clue();
        clue4.setWord("validClue");
        clue4.setSubmissionId(5L);

        List<Clue> submissions = new ArrayList<>();
        submissions.add(clue);
        submissions.add(clue1);
        submissions.add(clue2);
        submissions.add(clue3);

        gameRound.setSubmissions(submissions);
        gameRound.setMysteryWord("mysteryWord");

        gameRoundService.checkDuplicates(gameRound);

        assertTrue(clue.getIsDuplicate());
        assertTrue(clue1.getIsDuplicate());
        assertTrue(clue2.getIsDuplicate());
        assertTrue(clue3.getIsDuplicate());

    }







}

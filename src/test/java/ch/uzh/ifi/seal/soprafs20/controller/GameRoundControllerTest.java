package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.ClueRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRoundRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GuessRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameRound.GameRoundClueDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameRound.GameRoundDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameRound.GameRoundGuessDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameRound.GameRoundPutDTO;
import ch.uzh.ifi.seal.soprafs20.service.GameRoundService;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.mockito.MockitoAnnotations;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * GameRoundControllerTest
 * This is a WebMvcTest which allows to test the GameRoundController i.e. GET/POST request without actually sending them over the network.
 * This tests if the GameRoundController works.
 */
@WebMvcTest(GameRoundController.class)
public class GameRoundControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClueRepository clueRepository;

    @MockBean
    private GameRoundService gameRoundService;

    @MockBean
    private GuessRepository guessRepository;

    @MockBean
    private GameService gameService;

    @InjectMocks
    private PhysicalPlayer testPlayer;
    @InjectMocks
    private PhysicalPlayer testPlayer1;
    @InjectMocks
    private PhysicalPlayer testPlayer2;
    @InjectMocks
    private FriendlyBot testFriedlyBot = new FriendlyBot();
    @InjectMocks
    private List<Player> players = new ArrayList<>();
    @InjectMocks
    private Game game;
    @InjectMocks
    private GameRound gameRound;
    @InjectMocks
    private Guess guess;
    @InjectMocks
    private Clue clue;
    @InjectMocks
    private Card card;
    @InjectMocks
    List<Clue> submissions = new ArrayList<>();
    @InjectMocks
    private Clue clue1;
    @InjectMocks
    List<GameRound> gameRounds = new ArrayList<>();


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        testPlayer.setPlayerId(1L);
        testPlayer.setUserId(1L);
        testPlayer.setGameId(1L);

        testFriedlyBot.setPlayerId(2L);
        testFriedlyBot.setGameId(1L);

        testPlayer1.setPlayerId(3L);
        testPlayer1.setUserId(2L);
        testPlayer1.setGameId(1L);

        testPlayer2.setPlayerId(4L);
        testPlayer2.setUserId(3L);


        players.add(testPlayer);
        players.add(testFriedlyBot);
        players.add(testFriedlyBot);

        testFriedlyBot.setGameId(1L);
        testFriedlyBot.setPlayerId(1L);


        game.setGameId(1L);
        game.setGameName("abc");
        game.setNumberOfPlayers(2);
        game.setAdminPlayerId(1L);
        game.setGameStatus(GameStatus.CREATED);
        game.setHasBot(true);
        game.setPlayers(players);
        game.setActualGameRoundIndex(0);
        game.setRandomStartPosition(2);

        gameRound.setMysteryWord("");
        gameRound.setGuessingPlayerId(3L);
        gameRound.setGameId(1L);
        gameRound.setGameRoundId(1L);
        gameRound.setGuess(null);
        gameRound.setCard(card);

        gameRounds.add(gameRound);

        guess.setStartTime(22);
        guess.setEndTime(35);
        guess.setDuration(13);
        guess.setGameRoundId(1L);
        guess.setPlayerId(3L);
        guess.setCorrectGuess(false);
        guess.setDidSubmit(true);
        guess.setSubmissionId(4L);


        submissions.add(clue);
        submissions.add(clue1);
        clue.setWord("awesomeClue");
        clue.setStartTime(22);
        clue.setEndTime(35);
        clue.setDuration(13);
        clue.setGameRoundId(1L);
        clue.setPlayerId(1L);
        clue.setDuplicate(false);
        clue.setDidSubmit(true);
        clue.setSubmissionId(2L);

        clue1.setWord("superClue");
        clue1.setStartTime(22);
        clue1.setEndTime(35);
        clue1.setDuration(13);
        clue1.setGameRoundId(1L);
        clue1.setPlayerId(2L);
        clue1.setDuplicate(false);
        clue1.setDidSubmit(true);
        clue1.setSubmissionId(3L);

        card.setCardId(2L);
        card.setWord1("Sun");
        card.setWord2("Shine");
        card.setWord3("Bright");
        card.setWord4("Beach");
        card.setWord5("Sea");

        gameRound.setSubmissions(submissions);


    }

    @Test
    public void givenGame_whenCreateGameRound_thenReturnGameRoundDTO() throws Exception {
        given(gameService.getGameByGameId(1L)).willReturn(game);
        given(gameRoundService.startNewGameRound(game)).willReturn(gameRound);


        MockHttpServletRequestBuilder postRequest = post("/games/1/gameRounds").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(postRequest).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("gameId", is(gameRound.getGameId().intValue())))
                .andExpect(jsonPath("gameRoundId", is(gameRound.getGameRoundId().intValue())))
                .andExpect(jsonPath("guess").isEmpty())
                .andExpect(jsonPath("submissions").isNotEmpty())
                .andExpect(jsonPath("submissions").isArray());
    }


    /***
     * mocks a get request to "gameRounds/1" to get the gameRound with RoundId 1
     * asserts that the responseStatusCode is 200 and the attributes returned correspond with the attributes from the GameRound
     * with roundId 1
     * @throws Exception
     */
    @Test
    public void givenGameRound_whenGetGameRoundByRoundId_thenReturnGameRoundDTO() throws Exception {        //  --------------------------------------------------->   GET "/users" test
        // given
        given(gameRoundService.getGameRoundByRoundId(1L)).willReturn(gameRound);

        // when
        MockHttpServletRequestBuilder getRequest = get("/gameRounds/1").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("gameId", is(gameRound.getGameId().intValue())))
                .andExpect(jsonPath("guessingPlayerId", is(gameRound.getGuessingPlayerId().intValue())))
                .andExpect(jsonPath("mysteryWord", is(gameRound.getMysteryWord())))
                .andExpect(jsonPath("guess").isEmpty())
                .andExpect(jsonPath("card").isNotEmpty())
                .andExpect(jsonPath("gameRoundId", is(gameRound.getGameRoundId().intValue())))
                .andExpect(jsonPath("submissions").isArray());
    }

    /***
     * mocks a put request to "gameRounds/clues" with body gameRoundClueDTO to submit a clue
     * asserts that the responseStatusCode is 200 and the clue is correctly added to the submissions array in gameRound
     */
    @Test
    public void givenGameRound_whenChooseMysteryWord_thenSetMysteryWord() throws Exception {
        GameRound gameRoundWithWordSet = new GameRound();
        gameRoundWithWordSet.setMysteryWord("Bright");
        gameRoundWithWordSet.setGuessingPlayerId(3L);
        gameRoundWithWordSet.setGameId(1L);
        gameRoundWithWordSet.setGameRoundId(1L);
        gameRoundWithWordSet.setGuess(null);
        gameRoundWithWordSet.setCard(card);

        given(gameRoundService.getGameRoundByRoundId(1L)).willReturn(gameRound);
        given(gameRoundService.chooseMisteryWord(gameRound,3)).willReturn(gameRoundWithWordSet);

        GameRoundPutDTO gameRoundPutDTO = new GameRoundPutDTO();

        gameRoundPutDTO.setGameRoundId(1L);
        gameRoundPutDTO.setWordNumber(3);

        // when
        MockHttpServletRequestBuilder putRequest = put("/gameRounds").contentType(MediaType.APPLICATION_JSON).content(asJsonString(gameRoundPutDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("gameId", is(gameRound.getGameId().intValue())))
                .andExpect(jsonPath("guessingPlayerId", is(gameRound.getGuessingPlayerId().intValue())))
                .andExpect(jsonPath("guess").isEmpty())
                .andExpect(jsonPath("card").isNotEmpty())
                .andExpect(jsonPath("gameRoundId", is(gameRound.getGameRoundId().intValue())))
                .andExpect(jsonPath("submissions").isArray())
                .andExpect(jsonPath("submissions").isEmpty())
                .andExpect(jsonPath("mysteryWord").isNotEmpty());
    }

    /***
     * mocks a put request to "gameRounds/guesses" with body gameRoundGuessDTO to submit a guess
     * asserts that the responseStatusCode is 200 and the guess is correctly added to the gameRound
     */
    @Test
    public void givenGameRound_whenSubmitGuess_thenSetGuess() throws Exception {
        GameRound gameRoundWithSubmittedGuess = new GameRound();
        gameRoundWithSubmittedGuess.setMysteryWord("");
        gameRoundWithSubmittedGuess.setGuessingPlayerId(3L);
        gameRoundWithSubmittedGuess.setGameId(1L);
        gameRoundWithSubmittedGuess.setGameRoundId(1L);
        gameRoundWithSubmittedGuess.setGuess(guess);
        gameRoundWithSubmittedGuess.setCard(card);

        // given
        given(gameRoundService.getGameRoundByRoundId(1L)).willReturn(gameRound);
        given(gameRoundService.submitGuess(gameRound,"awesomeGuess",1L)).willReturn(gameRoundWithSubmittedGuess);
        given(guessRepository.findByPlayerIdAndGameRoundId(1L, 1L)).willReturn(guess);

        GameRoundGuessDTO gameroundGuessDTO = new GameRoundGuessDTO();

        gameroundGuessDTO.setGameRoundId(1L);
        gameroundGuessDTO.setGuess("awesomeGuess");
        gameroundGuessDTO.setPlayerId(1L);

        // when
        MockHttpServletRequestBuilder putRequest = put("/gameRounds/guesses").contentType(MediaType.APPLICATION_JSON).content(asJsonString(gameroundGuessDTO));

        // then
        mockMvc.perform(putRequest).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("gameId", is(gameRound.getGameId().intValue())))
                .andExpect(jsonPath("guessingPlayerId", is(gameRound.getGuessingPlayerId().intValue())))
                .andExpect(jsonPath("guess").isNotEmpty())
                .andExpect(jsonPath("card").isNotEmpty())
                .andExpect(jsonPath("gameRoundId", is(gameRound.getGameRoundId().intValue())))
                .andExpect(jsonPath("submissions").isArray())
                .andExpect(jsonPath("submissions").isEmpty())
                .andExpect(jsonPath("mysteryWord", is(gameRound.getMysteryWord())));
    }

    /***
     * mocks a get request to "games/lobby/1" with to get the last gameRound of the game with gameId 1
     * asserts that the responseStatusCode is 200 and the correct gameRound is returned
     */
    @Test
    public void getGameRoundId() throws Exception {
        // given
        given(gameRoundService.getGameRoundByGameId(1L)).willReturn(gameRounds);

        // when
        MockHttpServletRequestBuilder getRequest = get("/games/lobby/1");

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("gameId", is(gameRound.getGameId().intValue())))
                .andExpect(jsonPath("guessingPlayerId", is(gameRound.getGuessingPlayerId().intValue())))
                .andExpect(jsonPath("guess").isEmpty())
                .andExpect(jsonPath("card").isNotEmpty())
                .andExpect(jsonPath("gameRoundId", is(gameRound.getGameRoundId().intValue())))
                .andExpect(jsonPath("submissions").isArray())
                .andExpect(jsonPath("submissions").isNotEmpty())
                .andExpect(jsonPath("mysteryWord", is(gameRound.getMysteryWord())));
    }

    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     *
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new SopraServiceException(String.format("The request body could not be created.%s", e.toString()));
        }
    }
}
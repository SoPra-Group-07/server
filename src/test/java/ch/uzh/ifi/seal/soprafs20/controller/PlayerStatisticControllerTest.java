package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.ClueRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GuessRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameRound.GameRoundGuessDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameRound.GameRoundPutDTO;
import ch.uzh.ifi.seal.soprafs20.service.GameRoundService;
import ch.uzh.ifi.seal.soprafs20.service.GameService;
import ch.uzh.ifi.seal.soprafs20.service.PlayerStatisticService;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.mockito.MockitoAnnotations;

import java.util.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * PlayerStatisticControllerTest
 * This is a WebMvcTest which allows to test the PlayerStatisticController i.e. GET/POST request without actually sending them over the network.
 * This tests if the PlayerStatisticController works.
 */
@WebMvcTest(PlayerStatisticController.class)
public class PlayerStatisticControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerStatisticService playerStatisticService;
    @MockBean
    private GameRoundService gameRoundService;

    @InjectMocks
    private PlayerStatistic playerStatistic;
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
    private List<PlayerStatistic> playerStatistics = new ArrayList<>();
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
        testPlayer2.setGameId(1L);

        players.add(testPlayer);
        players.add(testPlayer1);
        players.add(testPlayer2);
        players.add(testFriedlyBot);

        guess.setWord("Bright");
        guess.setStartTime(22);
        guess.setEndTime(35);
        guess.setDuration(13);
        guess.setGameRoundId(1L);
        guess.setPlayerId(3L);
        guess.setCorrectGuess(false);
        guess.setDidSubmit(true);
        guess.setSubmissionId(4L);

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
        clue1.setPlayerId(4L);
        clue1.setDuplicate(false);
        clue1.setDidSubmit(true);
        clue1.setSubmissionId(3L);

        card.setCardId(2L);
        card.setWord1("Sun");
        card.setWord2("Shine");
        card.setWord3("Bright");
        card.setWord4("Beach");
        card.setWord5("Sea");

        submissions.add(clue);
        submissions.add(clue1);

        gameRound.setMysteryWord("Bright");
        gameRound.setGuessingPlayerId(3L);
        gameRound.setGameId(1L);
        gameRound.setGameRoundId(1L);
        gameRound.setWordNumber(3);
        gameRound.setGuess(guess);
        gameRound.setCard(card);
        gameRound.setSubmissions(submissions);
        gameRound.setEveryoneSubmitted(true);
        gameRound.setActualGameRound(1);
        gameRound.setTotalGameRounds(13);

        playerStatistic.setClue(clue.getWord());
        playerStatistic.setDidNotClue(false);
        playerStatistic.setDuplicateClue(true);
        playerStatistic.setDuplicateCluePoints(-0.5);
        playerStatistic.setDuration(clue.getDuration());
        playerStatistic.setDurationPoints(0.2);
        playerStatistic.setGameRoundId(gameRound.getGameRoundId());
        playerStatistic.setGuess(guess.getWord());
        playerStatistic.setNotCluePoints(0);
        playerStatistic.setPlayerId(testPlayer.getPlayerId());
        playerStatistic.setPlayerName(testPlayer.getPlayerName());
        playerStatistic.setPlayerStatisticId(1L);
        playerStatistic.setRightGuess(true);
        playerStatistic.setRightGuessPoints(1);
        playerStatistic.setPoints(0.7);
        playerStatistic.setWasGuessingPlayer(false);

        playerStatistics.add(playerStatistic);
        gameRound.setPlayerStatistic(playerStatistics);


    }

    @Test
    public void givenGameRound_whenGetGameRoundStatisticByRoundId_thenReturnGameRoundStatisticDTO() throws Exception {
        given(gameRoundService.getGameRoundByRoundId(1L)).willReturn(gameRound);

        MockHttpServletRequestBuilder getRequest = get("/gameRounds/1/gameRoundStatistics");

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].playerName", is(gameRound.getPlayerStatistic().get(0).getPlayerName())))
                .andExpect(jsonPath("$[0].totalPoints", is(gameRound.getPlayerStatistic().get(0).getTotalPoints())))
                .andExpect(jsonPath("$[0].wasGuessingPlayer", is(gameRound.getPlayerStatistic().get(0).getWasGuessingPlayer())))
                .andExpect(jsonPath("$[0].durationPoints", is(gameRound.getPlayerStatistic().get(0).getDurationPoints())))
                .andExpect(jsonPath("$[0].guess", is(gameRound.getPlayerStatistic().get(0).getGuess())))
                .andExpect(jsonPath("$[0].didNotClue", is(gameRound.getPlayerStatistic().get(0).getDidNotClue())))
                .andExpect(jsonPath("$[0].clue", is(gameRound.getPlayerStatistic().get(0).getClue())))
                .andExpect(jsonPath("$[0].duplicateClue", is(gameRound.getPlayerStatistic().get(0).getDuplicateClue())))
                .andExpect(jsonPath("$[0].duplicateCluePoints", is(gameRound.getPlayerStatistic().get(0).getDuplicateCluePoints())))
                .andExpect(jsonPath("$[0].notCluePoints", is(gameRound.getPlayerStatistic().get(0).getNotCluePoints())))
                .andExpect(jsonPath("$[0].rightGuess", is(gameRound.getPlayerStatistic().get(0).getRightGuess())))
                .andExpect(jsonPath("$[0].rightGuessPoints", is(gameRound.getPlayerStatistic().get(0).getRightGuessPoints())))
                .andExpect(jsonPath("$[0].playerStatisticId", is(gameRound.getPlayerStatistic().get(0).getPlayerStatisticId().intValue())))
                .andExpect(jsonPath("$[0].gameRoundId", is(gameRound.getGameRoundId().intValue())))
                .andExpect(jsonPath("$[0].playerId", is(gameRound.getPlayerStatistic().get(0).getPlayerId().intValue())))
                .andExpect(jsonPath("$[0].duration", is((int) gameRound.getPlayerStatistic().get(0).getDuration())));
    }

    @Test
    public void whenGetGameRoundStatisticByRoundId_gameRoundNotExists_thenReturnEmptyList() throws Exception {
        given(gameRoundService.getGameRoundByRoundId(2L)).willReturn(null);

        MockHttpServletRequestBuilder getRequest = get("/gameRounds/2/gameRoundStatistics");

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].gameRoundId", is(0)));
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
package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.gameround.GameRoundDTO;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * GameContorllerTest
 * This is a WebMvcTest which allows to test the GameController i.e. GET/POST/PUT request without actually sending them over the network.
 * This tests if the GameController works.
 */
@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private PlayerRepository playerRepository;

    @InjectMocks
    private PhysicalPlayer testPlayer;
    @InjectMocks
    private PhysicalPlayer testPlayer1 ;
    @InjectMocks
    private PhysicalPlayer testPlayer2;
    @InjectMocks
    private FriendlyBot testFriedlyBot = new FriendlyBot();
    @InjectMocks
    private List<Player> players = new ArrayList<>();
    @InjectMocks
    private Game game;


    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);

        testPlayer.setPlayerId(1L);
        testPlayer.setUserId(1L);
        testPlayer.setGameId(1L);
        testPlayer.setCurrentScore(4.6);

        testFriedlyBot.setPlayerId(2L);
        testFriedlyBot.setGameId(1L);

        testPlayer1.setPlayerId(3L);
        testPlayer1.setUserId(2L);
        testPlayer1.setGameId(1L);
        testPlayer1.setCurrentScore(5.6);

        testPlayer2.setPlayerId(4L);
        testPlayer2.setUserId(3L);


        players.add(testPlayer);
        players.add(testFriedlyBot);
        players.add(testPlayer1);

        testFriedlyBot.setGameId(1L);
        testFriedlyBot.setPlayerId(1L);
        testFriedlyBot.setCurrentScore(6.6);


        game.setGameId(1L);
        game.setGameName("abc");
        game.setNumberOfPlayers(2);
        game.setAdminPlayerId(1L);
        game.setGameStatus(GameStatus.CREATED);
        game.setHasBot(true);
        game.setPlayers(players);
        game.setActualGameRoundIndex(0);
        game.setRandomStartPosition(2);
    }
    @Test
    void givenGames_whenGetGames_thenReturnJsonArray() throws Exception {        //  --------------------------------------------------->   GET "/users" test
        // given
        List<Game> allGames = Collections.singletonList(game);

        given(gameService.getAllGames()).willReturn(allGames);

        // when
        MockHttpServletRequestBuilder getRequest = get("/games").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].gameId", is(game.getGameId().intValue())))
                .andExpect(jsonPath("$[0].gameName", is(game.getGameName())))
                .andExpect(jsonPath("$[0].numberOfPlayers", is(game.getNumberOfPlayers())));
    }

    @Test
    void givenGames_whenGetGames_CREATED_thenReturnJsonArray() throws Exception {
        // given
        List<Game> createdGames = Collections.singletonList(game);

        given(gameService.getGameByGameStatus(GameStatus.CREATED)).willReturn(createdGames);

        GameGetOpenDTO gameGetOpenDTO = new GameGetOpenDTO();
        gameGetOpenDTO.setGameName("abc");
        gameGetOpenDTO.setGameId(1L);
        gameGetOpenDTO.setNumberOfPlayers(3);
        // when
        MockHttpServletRequestBuilder getRequest = get("/games?gameStatus=CREATED").contentType(MediaType.APPLICATION_JSON).content(asJsonString(gameGetOpenDTO));    //=give it to me as JSON

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].gameId", is(game.getGameId().intValue())))
                .andExpect(jsonPath("$[0].gameName", is(game.getGameName())))
                .andExpect(jsonPath("$[0].numberOfPlayers", is(game.getNumberOfPlayers())))
        ;
    }


    @Test
    void givenGame_whenCreateGame_thenReturn_GameDTO() throws Exception {

        given(gameService.createNewGame(Mockito.any())).willReturn(game);

        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setGameName("abc");
        gamePostDTO.setAdminPlayerId(1L);
        gamePostDTO.setHasBot(true);
        // when
        MockHttpServletRequestBuilder postRequest = post("/games").contentType(MediaType.APPLICATION_JSON).content(asJsonString(gamePostDTO));    //=give it to me as JSON


        mockMvc.perform(postRequest).andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))                    // <-- Content-Type accepted?
                .andExpect(jsonPath("gameId", is(game.getGameId().intValue())))
                .andExpect(jsonPath("gameName", is(game.getGameName())))
                .andExpect(jsonPath("numberOfPlayers", is(game.getNumberOfPlayers())))
                .andExpect(jsonPath("gameStatus", is(game.getGameStatus().toString())))
                .andExpect(jsonPath("players", hasSize(3)))
                .andExpect(jsonPath("actualGameRoundIndex", is(game.getActualGameRoundIndex())))
                .andExpect(jsonPath("hasBot", is(game.getHasBot())))
                .andExpect(jsonPath("adminPlayerId", is(game.getAdminPlayerId().intValue())))
        ;
    }

    @Test
    void givenGame_whenJoinGame_thenReturn_GameDTO() throws Exception{

        game.getPlayers().add(testPlayer2);
        game.setNumberOfPlayers(4);


        given(gameService.joinGame(1L, 3L)).willReturn(game);

        GamePutDTO gamePutDTO = new GamePutDTO();
        gamePutDTO.setGameId(1);
        gamePutDTO.setUserId(3);

        MockHttpServletRequestBuilder putRequest = put("/games").contentType(MediaType.APPLICATION_JSON).content(asJsonString(gamePutDTO));

        mockMvc.perform(putRequest).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))                    // <-- Content-Type accepted?
                .andExpect(jsonPath("gameId", is(game.getGameId().intValue())))
                .andExpect(jsonPath("gameName", is(game.getGameName())))
                .andExpect(jsonPath("numberOfPlayers", is(game.getNumberOfPlayers())))
                .andExpect(jsonPath("gameStatus", is(game.getGameStatus().toString())))
                //.andExpect(jsonPath("players", instanceOf(PhysicalPlayer)))
                .andExpect(jsonPath("actualGameRoundIndex", is(game.getActualGameRoundIndex())))
                .andExpect(jsonPath("hasBot", is(game.getHasBot())))
                .andExpect(jsonPath("adminPlayerId", is(game.getAdminPlayerId().intValue())))
        ;
    }


    @Test
    void givenGame_whenGetLobby_thenReturn_LobbyDTO() throws Exception {
        given(gameService.getGameByGameId(1L)).willReturn(game);

        MockHttpServletRequestBuilder getRequest = get("/games/1/lobby").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("gameId", is(game.getGameId().intValue())))
                .andExpect(jsonPath("gameName", is(game.getGameName())))
                .andExpect(jsonPath("numberOfPlayers", is(game.getNumberOfPlayers())))
                .andExpect(jsonPath("gameStatus", is(game.getGameStatus().toString())))
                .andExpect(jsonPath("players", hasSize(3)))
                .andExpect(jsonPath("adminPlayerId", is((game.getAdminPlayerId().intValue()))))
        ;
    }

    @Test
    void givenGameRound_whenPUTGame_thenStartGameAndReturn_GameRoundDTO() throws Exception {
        Card card = new Card();
        card.setCardId(5L);
        card.setWord1("I");
        card.setWord2("am");
        card.setWord3("testing");
        card.setWord4("start");
        card.setWord5("game");


        GameRound gameRound = new GameRound();
        gameRound.setGameId(1L);
        gameRound.setGameRoundId(1L);
        gameRound.setCard(card);
        gameRound.setGuessingPlayerId(2L);

        given(gameService.startGame(1L)).willReturn(gameRound);

        GameRoundDTO gameRoundDTO = new GameRoundDTO();
        gameRoundDTO.setCard(card);
        gameRoundDTO.setGameId(1L);
        gameRoundDTO.setGameRoundId(1L);
        gameRoundDTO.setGuessingPlayerId(2L);


        MockHttpServletRequestBuilder putRequest = put("/games/lobby").contentType(MediaType.APPLICATION_JSON).content(asJsonString(gameRoundDTO));

        mockMvc.perform(putRequest).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))                    // <-- Content-Type accepted?
                .andExpect(jsonPath("gameId", is(gameRound.getGameId().intValue())))
                .andExpect(jsonPath("card", notNullValue()))
                .andExpect(jsonPath("gameRoundId", is(gameRound.getGameRoundId().intValue()))) // <--
                .andExpect(jsonPath("guessingPlayerId", is(gameRound.getGuessingPlayerId().intValue())))


        ;
    }

    @Test
    void givenGame_when_getPlayersFromGameByGameId_thenReturn_players() throws Exception {

        given(gameService.getGameByGameId(1L)).willReturn(game);
        MockHttpServletRequestBuilder getRequest = get("/games/1/players").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].playerName", is(game.getPlayers().get(0).getPlayerName())))
                .andExpect(jsonPath("$[0].gameId", is(game.getGameId().intValue())))
                .andExpect(jsonPath("$[1].playerName", is(game.getPlayers().get(1).getPlayerName())))
                .andExpect(jsonPath("$[1].gameId", is(game.getGameId().intValue())))
                .andExpect(jsonPath("$[2].playerName", is(game.getPlayers().get(2).getPlayerName())))
                .andExpect(jsonPath("$[2].gameId", is(game.getGameId().intValue())))


      ;
    }

    @Test
    void givenNotFinishedGame_whenGetIsGameFinished_ReturnFalse() throws Exception {
        given(gameService.getGameByGameId(1L)).willReturn(game);
        MockHttpServletRequestBuilder getRequest = get("/games/1").contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", is(false)))
        ;
    }

    @Test
    void givenFinishedGame_whenGetIsGameFinished_ReturnFalse() throws Exception {
        game.setGameStatus(GameStatus.FINISHED);
        given(gameService.getGameByGameId(1L)).willReturn(game);
        MockHttpServletRequestBuilder getRequest = get("/games/1").contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)))
        ;
    }

    @Test
    void givenGame_whenPutLeaveGame_return_nothingButOk() throws Exception {
        given(gameService.getGameByGameId(1L)).willReturn(game);
        given(playerRepository.findByPlayerId(3L)).willReturn(testPlayer2);

        GamePutDTO gamePutDTO = new GamePutDTO();
        gamePutDTO.setGameId(1L);
        gamePutDTO.setUserId(3L);

        MockHttpServletRequestBuilder putRequest = put("/games/out").contentType(MediaType.APPLICATION_JSON).content(asJsonString(gamePutDTO));
        mockMvc.perform(putRequest).andExpect(status().isOk())
        ;
    }

    @Test
    void givenGame_whenGetGameStatistics_thenReturn_GameStatisticDTO() throws Exception{
     given(gameService.getPlayersByGameId(game.getGameId())).willReturn(game.getPlayers());
     MockHttpServletRequestBuilder getRequest = get("/games/1/gameStatistics").contentType(MediaType.APPLICATION_JSON);
     mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                 .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                 .andExpect(jsonPath("$[0].playerName", is(game.getPlayers().get(0).getPlayerName())))
                .andExpect(jsonPath("$[0].playerId", is(game.getPlayers().get(0).getPlayerId().intValue())))
                 .andExpect(jsonPath("$[0].totalPoints", is(4.6)))
                 .andExpect(jsonPath("$[1].playerName", is(game.getPlayers().get(1).getPlayerName())))
                 .andExpect(jsonPath("$[1].playerId", is(game.getPlayers().get(1).getPlayerId().intValue())))
                 .andExpect(jsonPath("$[1].totalPoints", is(((double) 6.6))))
                 .andExpect(jsonPath("$[2].playerName", is(game.getPlayers().get(2).getPlayerName())))
                 .andExpect(jsonPath("$[2].playerId", is(game.getPlayers().get(2).getPlayerId().intValue())))
                 .andExpect(jsonPath("$[2].totalPoints", is((double)5.6)))



     ;

    }



    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
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

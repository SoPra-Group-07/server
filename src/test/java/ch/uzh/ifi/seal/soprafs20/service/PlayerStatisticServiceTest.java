package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class PlayerStatisticServiceTest {

    @Mock
    PlayerRepository playerRepository;
    @Mock
    PlayerStatisticRepository playerStatisticRepository;
    @InjectMocks
    PlayerStatisticService playerStatisticService;
    @InjectMocks
    private PhysicalPlayer testPlayer;
    @InjectMocks
    private PhysicalPlayer testPlayer1;
    @InjectMocks
    private PhysicalPlayer testPlayer2;
    @InjectMocks
    private Clue testClue;
    @InjectMocks
    private Clue testClue1;
    @InjectMocks
    private Guess testGuess;
    @InjectMocks
    PlayerStatistic playerStatistic;
    @InjectMocks
    PlayerStatistic playerStatistic1;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);

        testPlayer.setPlayerId(2L);
        testPlayer.setUserId(1L);
        testPlayer.setPlayerName("testPlayer");
        testPlayer1.setPlayerId(3L);
        testPlayer1.setUserId(2L);
        testPlayer1.setPlayerName("anotherPlayer");
        testPlayer2.setPlayerId(4L);
        testPlayer2.setUserId(3L);
        testPlayer2.setPlayerName("anotherOtherPlayer");

        testClue1.setPlayerName("anotherOtherPlayer");
        testClue1.setDuplicate(false);
        testClue1.setDidSubmit(false);
        testClue1.setDuration(60);
        testClue1.setWord("noClue");
        testClue1.setPlayerId(4L);

    }

    /**
     * tests that calculateClueingPlayerPoints(Clue clue, boolean rightGuess, PlayerStatistic playerStatistic) returns a
     * playerStatistic with the right amount of points. Here +1 for right guess +0.3 for duration and no deductions = 1.3.
     */
    @Test
    void testCalculateCluingPlayerPoints(){

        testClue.setPlayerName("testPlayer");
        testClue.setDuplicate(false);
        testClue.setDidSubmit(true);
        testClue.setDuration(10);
        testClue.setWord("hey");
        testClue.setPlayerId(2L);

        Mockito.when(playerRepository.findByPlayerId(Mockito.anyLong())).thenReturn(testPlayer);
        playerStatistic1 = playerStatisticService.calculateClueingPlayerPoints(testClue, true, playerStatistic);

        assertFalse(playerStatistic1.getDuplicateClue());
        assertEquals(0, playerStatistic1.getDuplicateCluePoints());
        assertFalse(playerStatistic1.getDidNotClue());
        assertEquals(0, playerStatistic1.getNotCluePoints());
        assertTrue(playerStatistic1.getRightGuess());
        assertEquals(1, playerStatistic1.getRightGuessPoints());
        assertFalse(playerStatistic1.getWasGuessingPlayer());
        assertEquals(10, playerStatistic1.getDuration());
        assertEquals(0.3, playerStatistic1.getDurationPoints());
        assertEquals(playerStatistic1.getNotCluePoints()+playerStatistic1.getRightGuessPoints()+
                playerStatistic1.getDuplicateCluePoints() + playerStatistic1.getDurationPoints(),playerStatistic1.getTotalPoints());
        assertEquals(1.3, playerStatistic1.getTotalPoints());
    }


    /**
     * tests that calculateClueingPlayerPoints(Clue clue, boolean rightGuess, PlayerStatistic playerStatistic) returns a
     * playerStatistic with the right amount of points. Here +1 for right guess +0.2 for duration and -0.5 for a duplicate clue = 0.7.
     */
    @Test
    void testCalculateCluingPlayerPoints2(){

        testClue.setPlayerName("testPlayer");
        testClue.setDuplicate(true);
        testClue.setDidSubmit(true);
        testClue.setDuration(16);
        testClue.setWord("hey");
        testClue.setPlayerId(2L);

        Mockito.when(playerRepository.findByPlayerId(Mockito.anyLong())).thenReturn(testPlayer);
        playerStatistic1 = playerStatisticService.calculateClueingPlayerPoints(testClue, true, playerStatistic);

        assertTrue(playerStatistic1.getDuplicateClue());
        assertEquals(-0.5, playerStatistic1.getDuplicateCluePoints());
        assertFalse(playerStatistic1.getDidNotClue());
        assertEquals(0, playerStatistic1.getNotCluePoints());
        assertTrue(playerStatistic1.getRightGuess());
        assertEquals(1, playerStatistic1.getRightGuessPoints());
        assertFalse(playerStatistic1.getWasGuessingPlayer());
        assertEquals(16, playerStatistic1.getDuration());
        assertEquals(0.2, playerStatistic1.getDurationPoints());
        assertEquals(playerStatistic1.getNotCluePoints()+playerStatistic1.getRightGuessPoints()+
                playerStatistic1.getDuplicateCluePoints() + playerStatistic1.getDurationPoints(),playerStatistic1.getTotalPoints());
        assertEquals(0.7, playerStatistic1.getTotalPoints());
    }

    /**
     * tests that calculateClueingPlayerPoints(Clue clue, boolean rightGuess, PlayerStatistic playerStatistic) returns a
     * playerStatistic with the right amount of points. Here +1 for right guess +0.1 for duration and -0.5 for a duplicate clue = 0.7.
     */
    @Test
    void testCalculateCluingPlayerPoints4(){

        testClue.setPlayerName("testPlayer");
        testClue.setDuplicate(true);
        testClue.setDidSubmit(true);
        testClue.setDuration(40);
        testClue.setWord("hey");
        testClue.setPlayerId(2L);

        Mockito.when(playerRepository.findByPlayerId(Mockito.anyLong())).thenReturn(testPlayer);
        playerStatistic1 = playerStatisticService.calculateClueingPlayerPoints(testClue, true, playerStatistic);

        assertTrue(playerStatistic1.getDuplicateClue());
        assertEquals(-0.5, playerStatistic1.getDuplicateCluePoints());
        assertFalse(playerStatistic1.getDidNotClue());
        assertEquals(0, playerStatistic1.getNotCluePoints());
        assertTrue(playerStatistic1.getRightGuess());
        assertEquals(1, playerStatistic1.getRightGuessPoints());
        assertFalse(playerStatistic1.getWasGuessingPlayer());
        assertEquals(40, playerStatistic1.getDuration());
        assertEquals(0.1, playerStatistic1.getDurationPoints());
        assertEquals(playerStatistic1.getNotCluePoints()+playerStatistic1.getRightGuessPoints()+
                playerStatistic1.getDuplicateCluePoints() + playerStatistic1.getDurationPoints(),playerStatistic1.getTotalPoints());
        assertEquals(0.6, playerStatistic1.getTotalPoints());
    }

    /**
     * tests that calculateClueingPlayerPoints(Clue clue, boolean rightGuess, PlayerStatistic playerStatistic) returns a
     * playerStatistic with the right amount of points. Here 0 for right guess 0 for duration and -0.6 for not submitting a clue.
     */
    @Test
    void testCalculateCluingPlayerPoints3(){

        testClue.setPlayerName("testPlayer");
        testClue.setDuplicate(false);
        testClue.setDidSubmit(false);
        testClue.setDuration(60);
        testClue.setWord("hey");
        testClue.setPlayerId(2L);

        Mockito.when(playerRepository.findByPlayerId(Mockito.anyLong())).thenReturn(testPlayer);
        playerStatistic1 = playerStatisticService.calculateClueingPlayerPoints(testClue, false, playerStatistic);

        assertFalse(playerStatistic1.getDuplicateClue());
        assertEquals(0, playerStatistic1.getDuplicateCluePoints());
        assertTrue(playerStatistic1.getDidNotClue());
        assertEquals(-0.6, playerStatistic1.getNotCluePoints());
        assertFalse(playerStatistic1.getRightGuess());
        assertEquals(0, playerStatistic1.getRightGuessPoints());
        assertFalse(playerStatistic1.getWasGuessingPlayer());
        assertEquals(0.0, playerStatistic1.getDuration());
        assertEquals(0, playerStatistic1.getDurationPoints());
        assertEquals(playerStatistic1.getNotCluePoints()+playerStatistic1.getRightGuessPoints()+
                playerStatistic1.getDuplicateCluePoints() + playerStatistic1.getDurationPoints(),playerStatistic1.getTotalPoints());
        assertEquals(-0.6, playerStatistic1.getTotalPoints());
    }

    /**
     * tests that calculateGuessingPlayerPoints(Guess guess, PlayerStatistic playerStatistic) returns a
     * playerStatistic with the right amount of points. Here +1.5 for right guess +0.3 for duration = 1.8.
     */
    @Test
    void testCalculateGuessingPlayerPoints(){

        testGuess.setPlayerName("anotherPlayer");
        testGuess.setDidSubmit(true);
        testGuess.setCorrectGuess(true);
        testGuess.setDuration(15);
        testGuess.setWord("hey");
        testGuess.setPlayerId(2L);

        Mockito.when(playerRepository.findByPlayerId(Mockito.anyLong())).thenReturn(testPlayer1);
        playerStatistic1 = playerStatisticService.calculateGuessingPlayerPoints(testGuess, playerStatistic);

        assertFalse(playerStatistic1.getDuplicateClue());
        assertEquals(0, playerStatistic1.getDuplicateCluePoints());
        assertFalse(playerStatistic1.getDidNotClue());
        assertEquals(0, playerStatistic1.getNotCluePoints());
        assertTrue(playerStatistic1.getRightGuess());
        assertEquals(1.5, playerStatistic1.getRightGuessPoints());
        assertTrue(playerStatistic1.getWasGuessingPlayer());
        assertEquals(15, playerStatistic1.getDuration());
        assertEquals(0.3, playerStatistic1.getDurationPoints());
        assertEquals(playerStatistic1.getRightGuessPoints()+
                playerStatistic1.getDurationPoints(),playerStatistic1.getTotalPoints());
        assertEquals(1.8, playerStatistic1.getTotalPoints());
    }

    /**
     * tests that calculateGuessingPlayerPoints(Guess guess, PlayerStatistic playerStatistic) returns a
     * playerStatistic with the right amount of points. Here +1.5 for right guess +0.2 for duration = 1.7.
     */
    @Test
    void testCalculateGuessingPlayerPoints2(){

        testGuess.setPlayerName("anotherPlayer");
        testGuess.setDidSubmit(true);
        testGuess.setCorrectGuess(true);
        testGuess.setDuration(30);
        testGuess.setWord("hey");
        testGuess.setPlayerId(2L);

        Mockito.when(playerRepository.findByPlayerId(Mockito.anyLong())).thenReturn(testPlayer1);
        playerStatistic1 = playerStatisticService.calculateGuessingPlayerPoints(testGuess, playerStatistic);

        assertFalse(playerStatistic1.getDuplicateClue());
        assertEquals(0, playerStatistic1.getDuplicateCluePoints());
        assertFalse(playerStatistic1.getDidNotClue());
        assertEquals(0, playerStatistic1.getNotCluePoints());
        assertTrue(playerStatistic1.getRightGuess());
        assertEquals(1.5, playerStatistic1.getRightGuessPoints());
        assertTrue(playerStatistic1.getWasGuessingPlayer());
        assertEquals(30, playerStatistic1.getDuration());
        assertEquals(0.2, playerStatistic1.getDurationPoints());
        assertEquals(playerStatistic1.getRightGuessPoints()+
                playerStatistic1.getDurationPoints(),playerStatistic1.getTotalPoints());
        assertEquals(1.7, playerStatistic1.getTotalPoints());
    }

    /**
     * tests that calculateGuessingPlayerPoints(Guess guess, PlayerStatistic playerStatistic) returns a
     * playerStatistic with the right amount of points. Here +1.5 for right guess +0.1 for duration = 1.6.
     */
    @Test
    void testCalculateGuessingPlayerPoints1(){

        testGuess.setPlayerName("anotherPlayer");
        testGuess.setDidSubmit(true);
        testGuess.setCorrectGuess(true);
        testGuess.setDuration(45);
        testGuess.setWord("hey");
        testGuess.setPlayerId(2L);

        Mockito.when(playerRepository.findByPlayerId(Mockito.anyLong())).thenReturn(testPlayer1);
        playerStatistic1 = playerStatisticService.calculateGuessingPlayerPoints(testGuess, playerStatistic);

        assertFalse(playerStatistic1.getDuplicateClue());
        assertEquals(0, playerStatistic1.getDuplicateCluePoints());
        assertFalse(playerStatistic1.getDidNotClue());
        assertEquals(0, playerStatistic1.getNotCluePoints());
        assertTrue(playerStatistic1.getRightGuess());
        assertEquals(1.5, playerStatistic1.getRightGuessPoints());
        assertTrue(playerStatistic1.getWasGuessingPlayer());
        assertEquals(45, playerStatistic1.getDuration());
        assertEquals(0.1, playerStatistic1.getDurationPoints());
        assertEquals(playerStatistic1.getRightGuessPoints()+
                playerStatistic1.getDurationPoints(),playerStatistic1.getTotalPoints());
        assertEquals(1.6, playerStatistic1.getTotalPoints());
    }

    /**
     * tests that calculateGuessingPlayerPoints(Guess guess, PlayerStatistic playerStatistic) returns a
     * playerStatistic with the right amount of points. Here 0 for not submitting a guess.
     */
    @Test
    void testCalculateGuessingPlayerPoints3(){

        testGuess.setPlayerName("anotherPlayer");
        testGuess.setDidSubmit(false);
        testGuess.setCorrectGuess(false);
        testGuess.setDuration(0);
        testGuess.setWord("hey");
        testGuess.setPlayerId(2L);

        Mockito.when(playerRepository.findByPlayerId(Mockito.anyLong())).thenReturn(testPlayer1);
        playerStatistic1 = playerStatisticService.calculateGuessingPlayerPoints(testGuess, playerStatistic);

        assertFalse(playerStatistic1.getDuplicateClue());
        assertEquals(0, playerStatistic1.getDuplicateCluePoints());
        assertFalse(playerStatistic1.getDidNotClue());
        assertEquals(0, playerStatistic1.getNotCluePoints());
        assertFalse(playerStatistic1.getRightGuess());
        assertEquals(0, playerStatistic1.getRightGuessPoints());
        assertTrue(playerStatistic1.getWasGuessingPlayer());
        assertEquals(0.0, playerStatistic1.getDuration());
        assertEquals(0, playerStatistic1.getDurationPoints());
        assertEquals(playerStatistic1.getRightGuessPoints()+
                playerStatistic1.getDurationPoints(),playerStatistic1.getTotalPoints());
        assertEquals(0, playerStatistic1.getTotalPoints());
    }

    /**
     * tests that calculateGuessingPlayerPoints(Guess guess, PlayerStatistic playerStatistic) returns a
     * playerStatistic with the right amount of points. Here 0 for not submitting a wrong guess.
     */
    @Test
    void testCalculateGuessingPlayerPoints4(){

        testGuess.setPlayerName("anotherPlayer");
        testGuess.setDidSubmit(true);
        testGuess.setCorrectGuess(false);
        testGuess.setDuration(46);
        testGuess.setWord("hey");
        testGuess.setPlayerId(2L);

        Mockito.when(playerRepository.findByPlayerId(Mockito.anyLong())).thenReturn(testPlayer1);
        playerStatistic1 = playerStatisticService.calculateGuessingPlayerPoints(testGuess, playerStatistic);

        assertFalse(playerStatistic1.getDuplicateClue());
        assertEquals(0, playerStatistic1.getDuplicateCluePoints());
        assertFalse(playerStatistic1.getDidNotClue());
        assertEquals(0, playerStatistic1.getNotCluePoints());
        assertFalse(playerStatistic1.getRightGuess());
        assertEquals(0, playerStatistic1.getRightGuessPoints());
        assertTrue(playerStatistic1.getWasGuessingPlayer());
        assertEquals(46, playerStatistic1.getDuration());
        assertEquals(0, playerStatistic1.getDurationPoints());
        assertEquals(playerStatistic1.getRightGuessPoints()+
                playerStatistic1.getDurationPoints(),playerStatistic1.getTotalPoints());
        assertEquals(0, playerStatistic1.getTotalPoints());
    }

    /***
     * tests that computeGameRoundStatistic() returns a list of playerStatistic objects for every player,
     * starting with the guessingPlayer at index 0, followed by all cluingPlayers
     * Therefore it asserts, that the list has size 3
     * Additionally, checks that the values of the playerStatistic are correct
     */
    @Test
    void computeGameRoundStatisticTest(){
        //given
        Mockito.when(playerRepository.findByPlayerId(2L)).thenReturn(testPlayer);
        Mockito.when(playerRepository.findByPlayerId(3L)).thenReturn(testPlayer1);
        Mockito.when(playerRepository.findByPlayerId(4L)).thenReturn(testPlayer2);
        Mockito.when(playerStatisticRepository.save(playerStatistic1)).thenReturn(playerStatistic);


        GameRound gameRound = new GameRound();
        gameRound.setGameRoundId(1L);

        testGuess.setPlayerName("anotherPlayer");
        testGuess.setDidSubmit(true);
        testGuess.setCorrectGuess(true);
        testGuess.setDuration(30);
        testGuess.setWord("hey");
        testGuess.setPlayerId(2L);
        gameRound.setGuess(testGuess);

        testClue.setPlayerName("testPlayer1");
        testClue.setDuplicate(false);
        testClue.setDidSubmit(true);
        testClue.setDuration(8);
        testClue.setWord("hey");
        testClue.setPlayerId(2L);

        testClue.setPlayerName("anotherPlayer");
        testClue.setDuplicate(false);
        testClue.setDidSubmit(true);
        testClue.setDuration(8);
        testClue.setWord("hey");
        testClue.setPlayerId(3L);

        List<Clue> submissions = new ArrayList<>();
        submissions.add(testClue);
        submissions.add(testClue1);
        gameRound.setSubmissions(submissions);

        List<PlayerStatistic> playerStatistics = playerStatisticService.computeGameRoundStatistic(gameRound);

        assertEquals(3, playerStatistics.size());

        assertEquals(gameRound.getGameRoundId(), playerStatistics.get(0).getGameRoundId());
        assertEquals(testPlayer.getPlayerId(), playerStatistics.get(0).getPlayerId());
        assertEquals(testPlayer.getPlayerName(), playerStatistics.get(0).getPlayerName());
        assertEquals(1.7, playerStatistics.get(0).getTotalPoints());
        assertTrue(playerStatistics.get(0).getWasGuessingPlayer());
        assertEquals(30.0, playerStatistics.get(0).getDuration());
        assertEquals(testGuess.getWord(), playerStatistics.get(0).getGuess());
        assertFalse(playerStatistics.get(0).getDidNotClue());
        assertFalse(playerStatistics.get(0).getDuplicateClue());
        assertEquals(0.0, playerStatistics.get(0).getDuplicateCluePoints());
        assertEquals(0.0, playerStatistics.get(0).getNotCluePoints());
        assertEquals(1.5, playerStatistics.get(0).getRightGuessPoints());
        assertTrue(playerStatistics.get(0).getRightGuess());
    }
}



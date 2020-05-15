package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;


public class PlayerStatisticServiceTest {

    @Mock
    PlayerRepository playerRepository;
    @InjectMocks
    PlayerStatisticService playerStatisticService;
    @InjectMocks
    private PhysicalPlayer testPlayer;
    @InjectMocks
    private PhysicalPlayer testPlayer1;
    @InjectMocks
    private Clue testClue;
    @InjectMocks
    private Guess testGuess;
    @InjectMocks
    PlayerStatistic playerStatistic;
    @InjectMocks
    PlayerStatistic playerStatistic1;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        testPlayer.setPlayerId(2L);
        testPlayer.setUserId(1L);
        testPlayer.setPlayerName("testPlayer");
        testPlayer1.setPlayerId(3L);
        testPlayer1.setUserId(2L);
        testPlayer1.setPlayerName("anotherPlayer");

    }

    /**
     * tests that calculateClueingPlayerPoints(Clue clue, boolean rightGuess, PlayerStatistic playerStatistic) returns a
     * playerStatistic with the right amount of points. Here +1 for right guess +0.3 for duration and no deductions = 1.3.
     */
    @Test
    public void testCalculateClueingPlayerPoints(){

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
    public void testCalculateClueingPlayerPoints2(){

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
    public void testCalculateClueingPlayerPoints4(){

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
    public void testCalculateClueingPlayerPoints3(){

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
    public void testCalculateGuessingPlayerPoints(){

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
    public void testCalculateGuessingPlayerPoints2(){

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
    public void testCalculateGuessingPlayerPoints1(){

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
    public void testCalculateGuessingPlayerPoints3(){

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
    public void testCalculateGuessingPlayerPoints4(){

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
}



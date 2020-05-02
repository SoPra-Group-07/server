package ch.uzh.ifi.seal.soprafs20.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import org.mockito.MockitoAnnotations;

import java.io.IOException;


import static org.junit.jupiter.api.Assertions.*;

public class FriendlyBot_and_MaliciousBot_Test {



    @InjectMocks
    private MaliciousBot testMaliciousBot;
    @InjectMocks
    private FriendlyBot testFriendlyBot;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);


        testFriendlyBot.setPlayerId(1L);
        testFriendlyBot.setGameId(1L);

    }

    /**
     * tests that a synonym and an antonym is found for Mouse
     */
    @Test
    public void find_syn_and_ant_for_Mouse() throws IOException {
    String friendlyWord = testFriendlyBot.giveClue("Mouse");
    assertNotEquals("mouse", friendlyWord);
    assertFalse(friendlyWord.contains(" "));


    String maliciousWord = testMaliciousBot.giveClue("Mouse");
    assertNotEquals("mouse", maliciousWord);
    assertFalse(maliciousWord.contains(" "));

    System.out.println(friendlyWord);
    System.out.println(maliciousWord);

    }
    /**
     * tests that a synonym and an antonym is found for Elvis
     */
    @Test
    public void find_syn_and_ant_for_Elvis() throws IOException {
        String friendlyWord = testFriendlyBot.giveClue("Elvis");
        assertNotEquals("elvis", friendlyWord);
        assertFalse(friendlyWord.contains(" "));


        String maliciousWord = testMaliciousBot.giveClue("Elvis");
        assertNotEquals("elvis", maliciousWord);
        assertFalse(maliciousWord.contains(" "));


        System.out.println(friendlyWord);
        System.out.println(maliciousWord);

    }

    /**
     * tests that a synonym and an antonym is found for Churchill
     */
    @Test
    public void find_syn_and_ant_for_Churchill() throws IOException {
        String friendlyWord = testFriendlyBot.giveClue("Churchill");
        assertNotEquals("churchill", friendlyWord);
        assertFalse(friendlyWord.contains(" "));


        String maliciousWord = testMaliciousBot.giveClue("Churchill");
        assertNotEquals("churchill", maliciousWord);
        assertFalse(maliciousWord.contains(" "));


        System.out.println(friendlyWord);
        System.out.println(maliciousWord);


    }
    /**
     * tests that a synonym and an antonym is found for Dish
     */
    @Test
    public void find_syn_and_ant_for_Dish() throws IOException {
        String friendlyWord = testFriendlyBot.giveClue("Dish");
        assertNotEquals("dish", friendlyWord);
        assertFalse(friendlyWord.contains(" "));


        String maliciousWord = testMaliciousBot.giveClue("Dish");
        assertNotEquals("dish", maliciousWord);
        assertFalse(maliciousWord.contains(" "));


        System.out.println(friendlyWord);
        System.out.println(maliciousWord);

    }
    /**
     * tests that a synonym and an antonym is found for Diamond
     */
    @Test
    public void find_syn_and_ant_for_Diamond() throws IOException{
        String friendlyWord = testFriendlyBot.giveClue("Diamond");
        assertNotEquals("diamond", friendlyWord);
        assertFalse(friendlyWord.contains(" "));


        String maliciousWord = testMaliciousBot.giveClue("Diamond");
        assertNotEquals("diamond", maliciousWord);
        assertFalse(maliciousWord.contains(" "));


        System.out.println(friendlyWord);
        System.out.println(maliciousWord);


    }
}

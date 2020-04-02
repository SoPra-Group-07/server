package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Internal GameRound Representation
 * This class composes the internal representation of the gameRound and defines how the gameRound is stored in the database.
 * Every variable will be mapped into a database field with the @Column annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes the primary key
 */
@Entity
@Table(name = "GAMEROUND")
public class GameRound implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long gameRoundId;

    @Column(nullable = false, unique = true)
    private long gameId;

    @Column()
    private long guessingPlayerId;

    @Column()
    private int currentCardIndex;

    @Column()
    private String mysteryWord;

    @Column()
    private ArrayList<Clue> clues;

    @Column()
    private ArrayList<Player> players;

    @Column()
    private ArrayList<PlayerStatistic> playerStatistic;



    public Long getGameRoundId() {
        return gameRoundId;
    }

    public void setGameRoundId(Long gameRoundId) {
        this.gameRoundId = gameRoundId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getGuessingPlayerId() {
        return guessingPlayerId;
    }

    public void setGuessingPlayerId(long guessingPlayerId) {
        this.guessingPlayerId = guessingPlayerId;
    }

    public int getCurrentCardIndex() {
        return currentCardIndex;
    }

    public void setCurrentCardIndex(int currentCardIndex) {
        this.currentCardIndex = currentCardIndex;
    }

    public String getMysteryWord() {
        return mysteryWord;
    }

    public void setMysteryWord(String mysteryWord) {
        this.mysteryWord = mysteryWord;
    }

    public ArrayList<Clue> getClues() {
        return clues;
    }

    public void setClues(ArrayList<Clue> clues) {
        this.clues = clues;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players){
        this.players = players;
    }

    public ArrayList<PlayerStatistic> getPlayerStatistic() {
        return playerStatistic;
    }

    public void setPlayerStatistic(ArrayList<PlayerStatistic> playerStatistic) {
        this.playerStatistic = playerStatistic;
    }
}





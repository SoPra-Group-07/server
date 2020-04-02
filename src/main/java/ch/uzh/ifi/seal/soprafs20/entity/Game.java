package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * Internal Game Representation
 * This class composes the internal representation of the game and defines how the game is stored in the database.
 * Every variable will be mapped into a database field with the @Column annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes the primary key
 */
@Entity
@Table(name = "GAME")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long gameId;

    @Column(nullable = false, unique = true)
    private String gameName;

    @Column()
    private GameStatus gameStatus;

    @Column()
    private ArrayList<Player> players;

    @Column()
    private int actualGameRoundIndex;

    // do we need this?
    @Column
    private ArrayList<Card> cards;

    @Column
    private ArrayList<GameRound> gameRounds;


    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public GameStatus getGameStatus() {return gameStatus; }

    public void setGameStatus(GameStatus gameStatus) {this.gameStatus = gameStatus; }

    public ArrayList<Player> getPlayers() { return players;}

    public void setPlayers(ArrayList<Player> players) { this.players = players;}

    public int getActualGameRoundIndex() {return actualGameRoundIndex;}

    public void setActualGameRoundIndex(int actualGameRoundIndex){this.actualGameRoundIndex = actualGameRoundIndex;}

    public ArrayList<Card> getCards(){return cards;}

    public void setCards(ArrayList<Card> cards){this.cards = cards;}

    public ArrayList<GameRound> getGameRounds(){return gameRounds;}

    public void setGameRounds(ArrayList<GameRound> gameRounds){this.gameRounds = gameRounds;}
}

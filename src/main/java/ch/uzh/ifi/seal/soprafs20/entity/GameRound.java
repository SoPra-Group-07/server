package ch.uzh.ifi.seal.soprafs20.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    @Column(name="game_round_id")
    private Long gameRoundId;

    @Column(nullable = false, unique = true)
    private long gameId;

    @Column()
    private long guessingPlayerId;

    @Column()
    private String mysteryWord;

    @OneToOne
    private Card card;

    @Column
    private boolean everyoneSubmitted;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "game_round_id", referencedColumnName = "game_round_id" )
    private Submission guess = new Guess();

    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "game_round_id", referencedColumnName = "game_round_id" )
    private List<Submission> duplicates = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "game_round_id", referencedColumnName = "game_round_id" )
    private List<Submission> validClues = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "game_round_id", referencedColumnName = "game_round_id" )
    private List<Submission> submissions = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "game_round_id", referencedColumnName = "game_round_id" )
    private List<PlayerStatistic> playerStatistic = new ArrayList<>();

    //private List<Player> players;

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

    public String getMysteryWord() {
        return mysteryWord;
    }

    public void setMysteryWord(String mysteryWord) {
        this.mysteryWord = mysteryWord;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }


    public List<PlayerStatistic> getPlayerStatistic() {
        return playerStatistic;
    }

    public void setPlayerStatistic(List<PlayerStatistic> playerStatistic) {
        this.playerStatistic = playerStatistic;
    }

    public List<Submission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<Submission> submissions) {
        this.submissions = submissions;
    }

    public boolean isEveryoneSubmitted() {
        return everyoneSubmitted;
    }

    public void setEveryoneSubmitted(boolean everyoneSubmitted) {
        this.everyoneSubmitted = everyoneSubmitted;
    }

    public List<Submission> getDuplicates() {
        return duplicates;
    }

    public void setDuplicates(List<Submission> duplicates) {
        this.duplicates = duplicates;
    }
}





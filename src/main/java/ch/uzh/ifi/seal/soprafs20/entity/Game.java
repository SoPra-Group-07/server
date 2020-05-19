package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

    public Game(){/* empty constructor */}

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long gameId;

    @Column(nullable = false)
    private String gameName;

    @Column
    private GameStatus gameStatus;

    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "game_id", referencedColumnName = "game_id" )
    private List<Player> players = new ArrayList<>();

    @Column
    private int numberOfPlayers;

    @Column
    private int actualGameRoundIndex;

    @ElementCollection
    private Set<Integer> cardIds = new LinkedHashSet<>();

    @Column
    private boolean hasBot;

    @Column
    private boolean friendlyBot;

    @Column
    private Long adminPlayerId;

    @Column
    private String adminPlayerName;

    @Column
    private int randomStartPosition;

    @Column
    private int actualGameRound;

    @Column
    private int totalGameRounds;

    @Column
    private boolean isDemoGame;

    public boolean getFriendlyBot() {
        return friendlyBot;
    }

    public void setFriendlyBot(boolean friendlybot) {
        this.friendlyBot = friendlybot;
    }

    public boolean getIsDemoGame() {
        return isDemoGame;
    }

    public void setIsDemoGame(boolean demoGame) {
        isDemoGame = demoGame;
    }

    public String getAdminPlayerName() {
        return adminPlayerName;
    }

    public void setAdminPlayerName(String adminPlayerName) {
        this.adminPlayerName = adminPlayerName;
    }

    public int getActualGameRound() {
        return actualGameRound;
    }

    public void setActualGameRound(int actualGameRound) {
        this.actualGameRound = actualGameRound;
    }

    public int getTotalGameRounds() {
        return totalGameRounds;
    }

    public void setTotalGameRounds(int totalGameRounds) {
        this.totalGameRounds = totalGameRounds;
    }

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

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getNumberOfPlayers() {
        numberOfPlayers = getPlayers().size();
        return numberOfPlayers; }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getActualGameRoundIndex() {
        return actualGameRoundIndex;
    }

    public void setActualGameRoundIndex(int actualGameRoundIndex) {
        this.actualGameRoundIndex = actualGameRoundIndex;
    }

    public Set<Integer> getCardIds() {
        return cardIds;
    }

    public void setCardIds(Set<Integer> cardIds) {
        this.cardIds = cardIds;
    }

    public boolean getHasBot() {
        return hasBot;
    }

    public void setHasBot(boolean hasBot) {
        this.hasBot = hasBot;
    }

    public Long getAdminPlayerId() {
        return adminPlayerId;
    }

    public void setAdminPlayerId(Long adminPlayerId) {
        this.adminPlayerId = adminPlayerId;
    }

    public int getRandomStartPosition() {
        return randomStartPosition;
    }

    public void setRandomStartPosition(int randomStartPosition) {
        this.randomStartPosition = randomStartPosition;
    }
}

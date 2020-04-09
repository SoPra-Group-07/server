package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Player;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository) {
        this.gameRepository = gameRepository;

    }

    public List<Game> getOpenGames() { return this.gameRepository.findAllByGameStatus(GameStatus.CREATED);}

    public Game createNewGame(Game gameInput) {
        Player adminPlayer = createPlayerByUserId(gameInput.getAdminPlayerId());

        Game game = new Game();
        game.setGameName(gameInput.getGameName());
        game.setAdminPlayerId(gameInput.getAdminPlayerId());
        game.setHasBot(gameInput.getHasBot());
        game.setGameStatus(GameStatus.CREATED);
        game.setActualGameRoundIndex(0);
        game.setCards(getThirteenUniqueCards());
        addPlayerToGame(adminPlayer, game.getGameId());

        if (game.getHasBot()){
            game.setNumberOfPlayers(2);
            // Todo: abstractPlayer
            AbstractPlayer bot = new Bot();
            addPlayerToGame(bot, game.getGameId());
        }
        else { game.setNumberOfPlayers(1); }


        return game;
    }

    private void addPlayerToGame(Player playerToAdd, long gameId){
        Game game = this.gameRepository.findByGameId(gameId);

        List<Player> playerList = game.getPlayers();
        playerList.add(playerToAdd);
        game.setPlayers(playerList);
    }

    private List<Card> getThirteenUniqueCards(){
        List<Card> cards = new ArrayList<>();

        for (int nr : getRandomUniqueCardIds()) {
            Card card = cardRepository.getById(nr);
            cards.add(card);
        }
        return cards;
    }

    private Set<Integer> getRandomUniqueCardIds() {
        Random rng = new Random(); // Ideally just create one instance globally
        // Note: use LinkedHashSet to maintain insertion order
        Set<Integer> generated = new LinkedHashSet<Integer>();
        while (generated.size() < 13)
        {
            // Todo: check bound -> number of cards available
            Integer next = rng.nextInt(56) + 1;
            // As we're adding to a set, this will automatically do a containment check
            generated.add(next);
        }
        return generated;
    }
}

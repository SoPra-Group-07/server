package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.CardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Array;
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
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, @Qualifier("userRepository") UserRepository userRepository, @Qualifier("cardRepository") CardRepository cardRepository,
                       @Qualifier("playerRepository") PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.playerRepository = playerRepository;
    }

    public List<Game> getGameByGameStatus(GameStatus gameStatus) {
        return this.gameRepository.findAllByGameStatus(gameStatus);
    }

    public Game createNewGame(Game gameInput) {

        Game game = new Game();
        game.setGameName(gameInput.getGameName());
        game.setAdminPlayerId(gameInput.getAdminPlayerId());
        game.setHasBot(gameInput.getHasBot());
        game.setGameStatus(GameStatus.CREATED);
        game.setActualGameRoundIndex(0);
        game.setCards(getThirteenUniqueCards());

        game = gameRepository.save(game);
        gameRepository.flush();

        Player adminPlayer = createPlayerByUserId(gameInput.getAdminPlayerId(), game);
        addPlayerToGame(adminPlayer, game);

        if (game.getHasBot()){
            Player bot = createBot(game);
            bot = playerRepository.save(bot);
            playerRepository.flush();
            addPlayerToGame(bot, game);
        }
        game.setNumberOfPlayers(game.getPlayers().size());
        return game;

    }

    private void addPlayerToGame(Player playerToAdd, Game game){
        if (game.getNumberOfPlayers() < 8){
            game.addPlayerToGame(playerToAdd); }

        else{
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Game already full! Join another game."); }
    }

    private List<Card> getThirteenUniqueCards(){
        List<Card> cards = new ArrayList<>();

        for (int nr : getRandomUniqueCardIds()) {
            //
            Card card = cardRepository.findByCardId((long) nr);
            // Todo: exception if card == null
            //Card card = new Card();
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

    private Player createPlayerByUserId(long userId, Game game){
        User user = userRepository.findByUserId(userId);
        Player player = new PhysicalPlayer();
        player.setUser(user);
        player.setGameId(game.getGameId());
        player = playerRepository.save(player);
        playerRepository.flush();
        return player;
    }

    private Player createBot(Game game) {
        List<String> fancyNames = new ArrayList<>(Arrays.asList("Jenny","Roy","Aquaman","JanTheNeck","Bernie","Hansi","Lars","Elaine","Alex","Renato","Chat","Christiane","Patrick","Andy","Thomas","Egon","Burkhard","Michael","Alberto","Ralph"));
        Random random = new Random();
        int idx = random.nextInt(fancyNames.size()-1);
        boolean friendly = random.nextBoolean();
        if (friendly) {
            Player bot = new FriendlyBot();
            bot.setGameId(game.getGameId());
            bot.setPlayerName(fancyNames.get(idx));
            bot.setIsGuessingPlayer(false);
            bot = playerRepository.save(bot);
            playerRepository.flush();
            return bot;

        }
        else {
            Player bot = new MaliciousBot();
            bot.setPlayerName(fancyNames.get(idx));
            bot.setGameId(game.getGameId());
            bot.setIsGuessingPlayer(false);
            bot = playerRepository.save(bot);
            playerRepository.flush();
            return bot;
        }
    }
    public Game joinGame(long gameId, long userId){
        Game game = gameRepository.findByGameId(gameId);
        Player player = createPlayerByUserId(userId, game);
        addPlayerToGame(player, game);
        game.setNumberOfPlayers(game.getNumberOfPlayers()+1);
        return game;
    }

    public Game getGameByGameId(long gameId){
        return gameRepository.findByGameId(gameId);
    }

}

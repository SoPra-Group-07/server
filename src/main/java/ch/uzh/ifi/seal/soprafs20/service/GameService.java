package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

/**
 * Game Service
 * This class is the "worker" and responsible for all functionality related to the game
 * (e.g., it creates, modifies, deletes, finds, updates). The result will be passed back to the caller.
 */
@Service
@Transactional
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;
    private final GameRoundService gameRoundService;
    private Random random = SecureRandom.getInstanceStrong();

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, @Qualifier("userRepository") UserRepository userRepository, @Qualifier("cardRepository") CardRepository cardRepository,
                       @Qualifier("playerRepository") PlayerRepository playerRepository, @Qualifier("gameRoundRepository") GameRoundRepository gameRoundRepository,
                       @Qualifier("clueRepository") ClueRepository clueRepository, PlayerStatisticService playerStatisticService, @Qualifier("guessRepository") GuessRepository guessRepository) throws NoSuchAlgorithmException {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.playerRepository = playerRepository;
        this.gameRoundService = new GameRoundService(gameRoundRepository, cardRepository, gameRepository, clueRepository, guessRepository, userRepository, playerStatisticService);
    }

    public List<Game> getAllGames(){
        return this.gameRepository.findAll();
    }

    public List<Game> getGameByGameStatus(GameStatus gameStatus) {
        return this.gameRepository.findAllByGameStatus(gameStatus);
    }

    public Game createNewGame(Game gameInput) {
        if (gameRepository.findByGameName(gameInput.getGameName()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "GameName is already taken!");
        }
        Game game = new Game();
        game.setGameName(gameInput.getGameName());
        game.setAdminPlayerId(gameInput.getAdminPlayerId());
        game.setHasBot(gameInput.getHasBot());
        game.setGameStatus(GameStatus.CREATED);
        game.setActualGameRoundIndex(0);
        game.setCardIds(getRandomUniqueCardIds());
        game.setRandomStartPosition(new Random().nextInt(7));

        game = gameRepository.save(game);
        gameRepository.flush();

        Player adminPlayer = createPlayerByUserIdAndGame(gameInput.getAdminPlayerId(), game);
        addPlayerToGame(adminPlayer, game);

        if (game.getHasBot()) {
            Player bot = createBot(game);
            bot = playerRepository.save(bot);
            playerRepository.flush();
            addPlayerToGame(bot, game);
        }
        game.setNumberOfPlayers(game.getPlayers().size());
        return game;
    }

    private void addPlayerToGame(Player playerToAdd, Game game){

        if (game.getNumberOfPlayers() < 7){
            List<Player> players = game.getPlayers();
            players.add(playerToAdd);
            game.setPlayers(players);
            game.setNumberOfPlayers(game.getPlayers().size()); }

        else{
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Game already full! Join another game."); }
    }

    public Set<Integer> getRandomUniqueCardIds() {
        Set<Integer> generated = new LinkedHashSet<>();
        while (generated.size() < 13)
        {
            Integer next = this.random.nextInt(55) + 1;
            // As we're adding to a set, this will automatically do a containment check
            generated.add(next);
        }
        return generated;
    }

    private Player createPlayerByUserIdAndGame(long userId, Game game){
        User user = userRepository.findByUserId(userId);
        Player player = new PhysicalPlayer();
        player.setUserId(user.getUserId());
        player.setPlayerName(user.getUsername());
        player.setGameId(game.getGameId());
        player = playerRepository.save(player);
        playerRepository.flush();
        return player;
    }

    private Player createBot(Game game) {
        List<String> fancyNames = new ArrayList<>(Arrays.asList("Jenny","Roy","Aquaman","JanTheNeck","Bernie","Hansi","Lars","Elaine","Alex","Renato","Chat","Christiane","Patrick","Andy","Thomas","Egon","Burkhard","Michael","Alberto","Ralph"));
        int idx = this.random.nextInt(fancyNames.size()-1);
        boolean friendly = random.nextBoolean();
        if (friendly) {
            Player bot = new FriendlyBot();
            bot.setGameId(game.getGameId());
            bot.setPlayerName(fancyNames.get(idx));

            bot = playerRepository.save(bot);
            playerRepository.flush();
            return bot;

        }
        else {
            Player bot = new MaliciousBot();
            bot.setPlayerName(fancyNames.get(idx));
            bot.setGameId(game.getGameId());

            bot = playerRepository.save(bot);
            playerRepository.flush();
            return bot;
        }
    }

    public Game joinGame(long gameId, long userId){
        Game game = gameRepository.findByGameId(gameId);
        if (userRepository.findByUserId(userId).getStatus() == UserStatus.ONLINE){
            Player p = playerRepository.findByUserIdAndGameId(userId, gameId);
            if (!game.getPlayers().contains(p)) {
                Player player = createPlayerByUserIdAndGame(userId, game);
                addPlayerToGame(player, game);
                return game;
            } else { throw new ResponseStatusException(HttpStatus.NO_CONTENT, "The user is already in the game!"); }
        } else { throw new ResponseStatusException(HttpStatus.CONFLICT, "User is not logged in, cannot join a game!"); }
    }

    public Game getGameByGameId(long gameId){
        return gameRepository.findByGameId(gameId);
    }

    public GameRound startGame(long gameId){
        Game game = getGameByGameId(gameId);
        if (game.getGameStatus() != GameStatus.CREATED){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You cannot start a game that is finished or already running.");
        }
        game.setGameStatus(GameStatus.RUNNING);
        return gameRoundService.startNewGameRound(game);
    }

    public void leaveGame(Long gameId, Long userId){

        Game game = getGameByGameId(gameId);
        Player player = playerRepository.findByUserIdAndGameId(userId, gameId);
        if (game == null)
        {  throw new ResponseStatusException(HttpStatus.CONFLICT, "you can only leave a game that exists.");}

        if (player == null)
            {  throw new ResponseStatusException(HttpStatus.CONFLICT, "only players that exist can leave a game.");}

        if (!game.getPlayers().contains(player)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "you can not leave this game since you are not in it."); }

        if (game.getGameStatus() != GameStatus.CREATED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "you can not leave this game since it is already running or finished."); }

        else {
            List<Player> listOfPlayers = game.getPlayers();
            listOfPlayers.remove(player);
            game.setPlayers(listOfPlayers);
            playerRepository.delete(player);
            game.setNumberOfPlayers(game.getPlayers().size()); }

        }

    public List<Player> getPlayersByGameId(Long gameId){
        return playerRepository.findAllByGameId(gameId);
    }

}

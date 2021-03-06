package ch.uzh.ifi.seal.soprafs20.service;
import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.stemmer.Stemmer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.util.*;


/**
 * GameRound Service
 * This class is the "worker" and responsible for all functionality related to the gameRound
 * (e.g., it creates, starts). The result will be passed back to the caller.
 */
@Service
@Transactional
public class GameRoundService {

    private final GameRoundRepository gameRoundRepository;
    private final CardRepository cardRepository;
    private final GameRepository gameRepository;
    private final ClueRepository clueRepository;
    private final GuessRepository guessRepository;
    private final UserRepository userRepository;
    private Random random = SecureRandom.getInstanceStrong();
    private final PlayerStatisticService playerStatisticService;


    @Autowired
    public GameRoundService(@Qualifier("gameRoundRepository") GameRoundRepository gameRoundRepository,
                            @Qualifier("cardRepository") CardRepository cardRepository, @Qualifier("gameRepository") GameRepository gameRepository,
                            @Qualifier("clueRepository") ClueRepository clueRepository, @Qualifier("guessRepository") GuessRepository guessRepository,
                            @Qualifier("userRepository") UserRepository userRepository, PlayerStatisticService playerStatisticService) throws NoSuchAlgorithmException {
        this.gameRoundRepository = gameRoundRepository;
        this.cardRepository = cardRepository;
        this.gameRepository = gameRepository;
        this.clueRepository = clueRepository;
        this.guessRepository = guessRepository;
        this.userRepository = userRepository;
        this.playerStatisticService = playerStatisticService;
    }

    public GameRound createNewGameRound(Game game){
        if (game.getActualGameRoundIndex() > game.getTotalGameRounds() || game.getGameStatus() == GameStatus.FINISHED){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Game has finished! No more gameRounds.");
        }
        GameRound gameRound = new GameRound();
        gameRound.setGameId(game.getGameId());
        List<Integer> list = new ArrayList<>(game.getCardIds());
        gameRound.setCard(getActualCard(list.get(game.getActualGameRoundIndex())));
        gameRound = gameRoundRepository.save(gameRound);
        gameRoundRepository.flush();
        return gameRound;
    }

    public GameRound startNewGameRound(Game game){
        if (game.getActualGameRoundIndex() < game.getTotalGameRounds()) {

            if (game.getActualGameRoundIndex() == 0) {
                int randomStart = this.random.nextInt(game.getNumberOfPlayers());
                game.setRandomStartPosition(randomStart);}

            game.setActualGameRoundIndex(game.getActualGameRoundIndex() + 1);
            game.setActualGameRound(game.getActualGameRound() + 1);

            GameRound actualGameRound = createNewGameRound(game);
            actualGameRound.setTotalGameRounds(game.getTotalGameRounds());
            actualGameRound.setActualGameRound(game.getActualGameRound());
            long nextGuessingPlayerIdx = computeGuessingPlayerId(game);
            actualGameRound.setGuessingPlayerId(nextGuessingPlayerIdx);
            return actualGameRound;
        }
        else{
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Game has finished! No more gameRounds.");
        }
    }

    private long computeGuessingPlayerId(Game game){
        int numberOfPlayers = game.getNumberOfPlayers();
        int actualGameRoundIdx = game.getActualGameRoundIndex();
        int randomStartPosition = game.getRandomStartPosition();

        int nextGuessingPlayerIdx = (randomStartPosition + actualGameRoundIdx) % numberOfPlayers;
        if (game.getPlayers().get(nextGuessingPlayerIdx) instanceof PhysicalPlayer) {
            return game.getPlayers().get(nextGuessingPlayerIdx).getPlayerId();
        }
        else {
            game.setRandomStartPosition(game.getRandomStartPosition()+1);
            return game.getPlayers().get((nextGuessingPlayerIdx + 1) % numberOfPlayers).getPlayerId(); }
    }

    private Card getActualCard(int cardId){
        Card card = cardRepository.findByCardId((long) cardId);
        if ((card != null)) {
            return card;
        }
        else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CardId not found in CardRepo");
        }
    }

    public GameRound getGameRoundByRoundId(long roundId){
        return gameRoundRepository.findByGameRoundId(roundId);
    }

    public GameRound chooseMysteryWord(GameRound gameRound, int wordNumber)  {
        if (wordNumber==1) {
            gameRound.setMysteryWord(gameRound.getCard().getWord1());
        }else if (wordNumber==2) {
            gameRound.setMysteryWord(gameRound.getCard().getWord2());
        }else if (wordNumber==3) {
            gameRound.setMysteryWord(gameRound.getCard().getWord3());
        }else if (wordNumber==4) {
            gameRound.setMysteryWord(gameRound.getCard().getWord4());
        }else if (wordNumber==5) {
            gameRound.setMysteryWord(gameRound.getCard().getWord5());
        }
        else { throw new ResponseStatusException(HttpStatus.CONFLICT, "Choose a number between 1-5");}

        return gameRound;
    }


    public void createCluesAndGuesses(GameRound gameRound) throws IOException {
        Game game = gameRepository.findByGameId(gameRound.getGameId());
        for (Player player: game.getPlayers()){
            if (!player.getPlayerId().equals(gameRound.getGuessingPlayerId())) {
                Clue clue = new Clue();
                clue.setStartTime(ZonedDateTime.now().toInstant().toEpochMilli());
                clue.setPlayerId(player.getPlayerId());
                clue.setGameRoundId(gameRound.getGameRoundId());
                clue.setPlayerName(player.getPlayerName());

                Clue clue1 = clueRepository.save(clue);
                clueRepository.flush();
                if (player instanceof FriendlyBot || player instanceof MaliciousBot) {
                    String friendlyOrMaliciousClue = player.giveClue(gameRound.getMysteryWord());
                    clue1.setWord(friendlyOrMaliciousClue);
                    clue1.setEndTime(clue1.getStartTime() + 16000);
                    clue1.setDuration(((clue.getEndTime() - clue.getStartTime()) / 1000));
                    clue1.setDidSubmit(true);
                    clue1.setPlayerName(player.getPlayerName());


                }
            }
            else{
                Guess guess = new Guess();
                guess.setPlayerId(player.getPlayerId());
                guess.setGameRoundId(gameRound.getGameRoundId());
                guess.setPlayerName(player.getPlayerName());
                gameRound.setGuess(guess);
                guessRepository.save(guess);
                guessRepository.flush();
            }
        }
    }

    public Clue submitClue(GameRound gameRound, String clue, Long playerId){
        if (gameRound.getGuessingPlayerId().equals(playerId)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "a guessing player can not submit a clue!");
        }
        Clue clue1 = clueRepository.findByPlayerIdAndGameRoundId(playerId, gameRound.getGameRoundId());
        if (clue.equals("noClue")){
            clue1.setDidSubmit(false);
            clue1.setWord("noClue");
        }
        else{
            clue1.setWord(clue);
            clue1.setDidSubmit(true);
        }

        clue1.setEndTime(ZonedDateTime.now().toInstant().toEpochMilli());
        clue1.setDuration((clue1.getEndTime()-clue1.getStartTime())/1000);
        checkIfEveryoneSubmitted(gameRound);
        if (gameRound.getEveryoneSubmitted()){
            checkDuplicates(gameRound);
            Guess guess = guessRepository.findByGameRoundId(clue1.getGameRoundId());
            guess.setStartTime(ZonedDateTime.now().toInstant().toEpochMilli());
        }
        return clue1;
    }

    public GameRound submitGuess(GameRound gameRound, String guess, Long playerId){
        if (!gameRound.getGuessingPlayerId().equals(playerId)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "a clueing player can not submit a guess!");
        }
        Guess guess1 = guessRepository.findByPlayerIdAndGameRoundId(playerId, gameRound.getGameRoundId());
        if (guess.equals("noGuess")){
            guess1.setDidSubmit(false);
            guess1.setWord("noGuess");
        }
        else{
            guess1.setWord(guess);
            guess1.setDidSubmit(true);}
        guess1.setEndTime(ZonedDateTime.now().toInstant().toEpochMilli());
        guess1.setDuration((guess1.getEndTime()-guess1.getStartTime())/1000);
        checkGuess(gameRound, guess1);
        Game game = gameRepository.findByGameId(gameRound.getGameId());
        playerStatisticService.computeGameRoundStatistic(gameRound);
        gameRound.setGuess(guess1);

        if(guess1.getDidSubmit() && !guess1.getCorrectGuess()){
            game.setTotalGameRounds(game.getTotalGameRounds() - 1); }

        if (game.getActualGameRound() >= game.getTotalGameRounds()){
            finishGame(game);
            }

        return gameRound;
        }

    public void checkGuess(GameRound gameRound, Guess guess){
        Stemmer stemmer = new PorterStemmer();
        String stemmedGuess = stemmer.stem(guess.getWord().toLowerCase()).toString();
        String stemmedMysteryWord = stemmer.stem(gameRound.getMysteryWord().toLowerCase()).toString();

        if (stemmedGuess.equals(stemmedMysteryWord)){
            guess.setCorrectGuess(true); }

        else { guess.setCorrectGuess(false); }

    }
    public void checkIfEveryoneSubmitted(GameRound gameRound){
        Game game = gameRepository.findByGameId(gameRound.getGameId());
        int submissions = 0;
        for (Clue clue: gameRound.getSubmissions()){
            if (clue.getWord() != null){
                submissions++;
            }
        }
        if (submissions == game.getNumberOfPlayers()-1){
            gameRound.setEveryoneSubmitted(true);
            Guess guess = guessRepository.findByGameRoundId(gameRound.getGameRoundId());
            guess.setStartTime(ZonedDateTime.now().toInstant().toEpochMilli());
        }
    }


    public void checkDuplicates(GameRound gameRound){
        List<Clue> submission = gameRound.getSubmissions();
        Stemmer stemmer = new PorterStemmer();


        for (Clue clue: submission){
            String stemmedClue = stemmer.stem(clue.getWord()).toString().toLowerCase();
            clue.setStemmedClue(stemmedClue); }

        for (Clue clue: submission){

            for(Clue clue1: submission){
                if ((clue.getStemmedClue().equals(clue1.getStemmedClue()) && !clue.getSubmissionId().equals(clue1.getSubmissionId()))){
                    clue.setDuplicate(true);
                    clue1.setDuplicate(true); }

            }

            if (clue.getStemmedClue().equalsIgnoreCase(stemmer.stem(gameRound.getMysteryWord().toLowerCase()).toString())){
                clue.setDuplicate(true);
            }


        }


    }

    public void finishGame(Game game){
        game.setGameStatus(GameStatus.FINISHED);
        updateUsers(game);

    }


    public void updateUsers(Game game){
        for (Player player: game.getPlayers()) {
            if (player instanceof PhysicalPlayer) {
                User user = userRepository.findByUserId(player.getUserId());
                if (user.getHighScore() < player.getCurrentScore()) {
                    user.setHighScore(player.getCurrentScore());
                }
                user.setNumberOfGamesPlayed(user.getNumberOfGamesPlayed() + 1);
            }
        }
    }

    public List<GameRound> getGameRoundByGameId(long gameId){
        return gameRoundRepository.findAllByGameId(gameId);
    }

    public long computeGuessingPlayerIdTest(Game game){ return computeGuessingPlayerId(game);}
}

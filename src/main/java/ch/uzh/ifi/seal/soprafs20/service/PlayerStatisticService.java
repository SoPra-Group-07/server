package ch.uzh.ifi.seal.soprafs20.service;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;



/**
 * PlayerStatistic Service
 * This class is the "worker" and responsible for all functionality related to the playerStatistic
 * (e.g., it calculates points and updates). The result will be passed back to the caller.
 */
@Service
@Transactional
public class PlayerStatisticService {

    private final PlayerStatisticRepository playerStatisticRepository;
    private final GameRoundRepository gameRoundRepository;
    private final GameService gameService;
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerStatisticService(@Qualifier("playerStatisticRepository") PlayerStatisticRepository playerStatisticRepository, @Qualifier("gameRoundRepository") GameRoundRepository gameRoundRepository, GameService gameService,@Qualifier("playerRepository") PlayerRepository playerRepository) {
        this.playerStatisticRepository = playerStatisticRepository;
        this.gameRoundRepository = gameRoundRepository;
        this.gameService = gameService;
        this.playerRepository = playerRepository;
    }

    public List<PlayerStatistic> computeGameRoundStatistic(long roundId){
        GameRound gameRound = gameRoundRepository.findByGameRoundId(roundId);
        List<PlayerStatistic> playerStatistics = new ArrayList<>();

        Guess guess = gameRound.getGuess();
        float guessingPlayerPoints = calculateGuessingPlayerPoints(guess, gameRound);
        PlayerStatistic playerStatistic = new PlayerStatistic();
        playerStatistic.setGameRoundId(gameRound.getGameRoundId());
        playerStatistic.setPlayerId(guess.getPlayerId());
        playerStatistic.setPoints(guessingPlayerPoints);
        playerStatistics.add(playerStatistic);
        playerStatisticRepository.save(playerStatistic);
        playerStatisticRepository.flush();
        updatePlayer(playerStatistic);

        for (Clue clue : gameRound.getSubmissions()){
            boolean rightGuess = gameRound.getGuess().getCorrectGuess();
            float cluingPlayerPoints = calculateClueingPlayerPoints(clue, rightGuess);
            PlayerStatistic playerStatistic1 = new PlayerStatistic();
            playerStatistic1.setGameRoundId(gameRound.getGameRoundId());
            playerStatistic1.setPlayerId(clue.getPlayerId());
            playerStatistic1.setPoints(cluingPlayerPoints);

            playerStatisticRepository.save(playerStatistic1);
            playerStatisticRepository.flush();
            playerStatistics.add(playerStatistic1);
            updatePlayer(playerStatistic);
        }



        return playerStatistics;
    }

    private float calculateClueingPlayerPoints(Clue clue, boolean rightGuess) {
        float totalPoints = 0;

        if (rightGuess) {
            totalPoints += 1;
        }

        if (!clue.getDidSubmit()) {
            totalPoints -= 0.6;
        }
        else {
            if (clue.getIsDuplicate()) {
                totalPoints -= 0.5;
            }

            if (clue.getDuration() <= 15) {
                totalPoints += 0.3;
            }
            else if (clue.getDuration() <= 30) {
                totalPoints += 0.2;
            }
            else if (clue.getDuration() <= 45) {
                totalPoints += 0.1;
            }
        }

        return totalPoints;
    }

    private float calculateGuessingPlayerPoints(Guess guess, GameRound gameRound){
        float totalPoints = 0;
        if (!guess.getDidSubmit()){
            return totalPoints;
        }
        else{
            //right guess
            if (guess.getCorrectGuess()){
                totalPoints += 1.5;
                if (guess.getDuration() <= 15) {totalPoints += 0.3;}
                else if (guess.getDuration() <= 30) {totalPoints += 0.2;}
                else if (guess.getDuration() <= 45) {totalPoints += 0.1;}
            }
            //wrong guess, delete an additional card
            else{
                Game game = gameService.getGameByGameId(gameRound.getGameId());
                game.setActualGameRoundIndex(game.getActualGameRoundIndex() + 1);
                game.setRandomStartPosition(game.getRandomStartPosition() - 1); }
        }

        return totalPoints;
    }

    public void updatePlayer(PlayerStatistic playerStatistic){
        Player player = playerRepository.findByPlayerId(playerStatistic.getPlayerId());
        player.setCurrentScore(player.getCurrentScore()+playerStatistic.getPoints());
    }

}

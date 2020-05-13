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
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerStatisticService(@Qualifier("playerStatisticRepository") PlayerStatisticRepository playerStatisticRepository, @Qualifier("playerRepository") PlayerRepository playerRepository) {
        this.playerStatisticRepository = playerStatisticRepository;
        this.playerRepository = playerRepository;
    }

    public void computeGameRoundStatistic(GameRound gameRound){
        List<PlayerStatistic> playerStatistics = new ArrayList<>();

        Guess guess = gameRound.getGuess();
        PlayerStatistic playerStatistic = new PlayerStatistic();
        PlayerStatistic playerStatistic1 = calculateGuessingPlayerPoints(guess, playerStatistic);
        playerStatistic1.setGameRoundId(gameRound.getGameRoundId());
        playerStatistic1.setPlayerId(guess.getPlayerId());
        playerStatistics.add(playerStatistic1);
        playerStatisticRepository.save(playerStatistic1);
        playerStatisticRepository.flush();
        updatePlayer(playerStatistic1);

        for (Clue clue : gameRound.getSubmissions()){
            boolean rightGuess = gameRound.getGuess().getCorrectGuess();
            PlayerStatistic playerStatistic2 = new PlayerStatistic();
            PlayerStatistic playerStatistic3 = calculateClueingPlayerPoints(clue, rightGuess, playerStatistic2);
            playerStatistic3.setGameRoundId(gameRound.getGameRoundId());
            playerStatistic3.setPlayerId(clue.getPlayerId());
            playerStatisticRepository.save(playerStatistic3);
            playerStatisticRepository.flush();
            playerStatistics.add(playerStatistic3);
            updatePlayer(playerStatistic3);
        }



        gameRound.setPlayerStatistic(playerStatistics);
    }

    private PlayerStatistic calculateClueingPlayerPoints(Clue clue, boolean rightGuess, PlayerStatistic playerStatistic) {
        float totalPoints = 0;

        if (rightGuess) {
            totalPoints += 1;
            playerStatistic.setRightGuess(true);
            playerStatistic.setRightGuessPoints(1);
        }

        if (!clue.getDidSubmit()) {
            totalPoints -= 0.6;
            playerStatistic.setDidNotClue(true);
            playerStatistic.setNotCluePoints(-0.6);
        }
        else {
            if (clue.getIsDuplicate()) {
                totalPoints -= 0.5;
                playerStatistic.setDuplicateCluePoints(-0.5);
                playerStatistic.setDuplicateClue(true);
            }

            if (clue.getDuration() <= 15) {
                totalPoints += 0.3;
                playerStatistic.setDuration(clue.getDuration());
                playerStatistic.setDurationPoints(0.3);
            }
            else if (clue.getDuration() <= 30) {
                totalPoints += 0.2;
                playerStatistic.setDuration(clue.getDuration());
                playerStatistic.setDurationPoints(0.1);
            }
            else if (clue.getDuration() <= 45) {
                totalPoints += 0.1;
                playerStatistic.setDuration(clue.getDuration());
                playerStatistic.setDurationPoints(0.1);
            }
        }

        playerStatistic.setPlayerName(playerRepository.findByPlayerId(clue.getPlayerId()).getPlayerName());
        playerStatistic.setTotalPoints(Math.round(totalPoints * 100.0) / 100.0);
        playerStatistic.setWasGuessingPlayer(false);
        playerStatistic.setClue(clue.getWord());
        return playerStatistic;
    }

    private PlayerStatistic calculateGuessingPlayerPoints(Guess guess, PlayerStatistic playerStatistic){
        float totalPoints = 0;
        if (!guess.getDidSubmit()){
            playerStatistic.setRightGuess(false);
            playerStatistic.setRightGuessPoints(0);
        }
        else{
            if (guess.getCorrectGuess()) {
                playerStatistic.setRightGuess(true);
                playerStatistic.setRightGuessPoints(1.5);
                totalPoints += 1.5;
            }
            else{playerStatistic.setRightGuess(false);}
            if (guess.getDuration() <= 15) {
                totalPoints += 0.3;
                playerStatistic.setDurationPoints(0.3);}
            else if (guess.getDuration() <= 30) {
                totalPoints += 0.2;
                playerStatistic.setDurationPoints(0.2);
                }
            else if (guess.getDuration() <= 45) {totalPoints += 0.1;
            playerStatistic.setDurationPoints(0.1);}
            }

        playerStatistic.setDuplicateClue(false);
        playerStatistic.setDuplicateCluePoints(0);
        playerStatistic.setDuration(guess.getDuration());
        playerStatistic.setPlayerName(playerRepository.findByPlayerId(guess.getPlayerId()).getPlayerName());
        playerStatistic.setTotalPoints(Math.round(totalPoints * 100.0) / 100.0);
        playerStatistic.setWasGuessingPlayer(true);
        playerStatistic.setGuess(guess.getWord());

        return playerStatistic;

    }

    public void updatePlayer(PlayerStatistic playerStatistic){
        Player player = playerRepository.findByPlayerId(playerStatistic.getPlayerId());
        player.setCurrentScore(player.getCurrentScore() + playerStatistic.getPoints());
        if (playerStatistic.getWasGuessingPlayer()){
            if (playerStatistic.getRightGuess()){
                player.setNumberOfCorrectGuesses(player.getNumberOfCorrectGuesses() + 1);
            }
        }
        else if (playerStatistic.getDuplicateClue()){
            player.setNumberOfDuplicateClues(player.getNumberOfDuplicateClues() + 1);
        }


    }


}

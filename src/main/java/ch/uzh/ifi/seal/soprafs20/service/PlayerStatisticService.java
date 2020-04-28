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
import java.time.ZonedDateTime;
import java.util.*;



/**
 * GameRound Service
 * This class is the "worker" and responsible for all functionality related to the gameRound
 * (e.g., it creates, starts). The result will be passed back to the caller.
 */
@Service
@Transactional
public class PlayerStatisticService {

    private final PlayerStatisticRepository playerStatisticRepository;
    private final GameRoundRepository gameRoundRepository;

    @Autowired
    public PlayerStatisticService(@Qualifier("playerStatisticRepository") PlayerStatisticRepository playerStatisticRepository, @Qualifier("gameRoundRepository") GameRoundRepository gameRoundRepository) {
        this.playerStatisticRepository = playerStatisticRepository;
        this.gameRoundRepository = gameRoundRepository;
    }

    public List<PlayerStatistic> computeGameRoundStatistic(long roundId){
        GameRound gameRound = gameRoundRepository.findByGameRoundId(roundId);
        List<PlayerStatistic> playerStatistics = new ArrayList<>();

        for (Clue clue : gameRound.getSubmissions()){
            int cluingPlayerPoints = calculateCluingPlayerPoints(clue);
            PlayerStatistic playerStatistic = new PlayerStatistic();
            playerStatistic.setGameRoundId(gameRound.getGameRoundId());
            playerStatistic.setPlayerID(clue.getPlayerId());
            playerStatistic.setPoints(cluingPlayerPoints);

            playerStatisticRepository.save(playerStatistic);
            playerStatisticRepository.flush();
            playerStatistics.add(playerStatistic);
        }

        Guess guess = gameRound.getGuess();
        int guessingPlayerPoints = calculateGuessingPlayerPoints(guess);
        PlayerStatistic playerStatistic = new PlayerStatistic();
        playerStatistic.setGameRoundId(gameRound.getGameRoundId());
        playerStatistic.setPlayerID(guess.getPlayerId());
        playerStatistic.setPoints(guessingPlayerPoints);
        playerStatistics.add(playerStatistic);

        return playerStatistics;
    }

    private int calculateCluingPlayerPoints(Clue clue){
        return 0;
    }

    private int calculateGuessingPlayerPoints(Guess guess){
        return 0;
    }

}

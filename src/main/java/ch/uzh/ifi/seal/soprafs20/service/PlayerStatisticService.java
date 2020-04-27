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

    @Autowired
    public PlayerStatisticService(@Qualifier("playerStatisticRepository") PlayerStatisticRepository playerStatisticRepository) {
        this.playerStatisticRepository = playerStatisticRepository;
    }
    /*
    public GameRound createNewGameRound(Game game){
        if (game.getActualGameRoundIndex() > 12 || game.getGameStatus() == GameStatus.FINISHED){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Game has finished! No more gameRounds.");
        }
        GameRound gameRound = new GameRound();
        gameRound.setGameId(game.getGameId());
        List<Integer> list = new ArrayList<>(game.getCardIds());
        gameRound.setCard(getActualCard(list.get(game.getActualGameRoundIndex())));
        GameRound gameRound1 = gameRoundRepository.save(gameRound);
        gameRoundRepository.flush();

        return gameRound1;
    }
     */
}

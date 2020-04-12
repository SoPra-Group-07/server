package ch.uzh.ifi.seal.soprafs20.service;
import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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


    @Autowired
    public GameRoundService(@Qualifier("gameRoundRepository") GameRoundRepository gameRoundRepository,
                            @Qualifier("cardRepository") CardRepository cardRepository) {
        this.gameRoundRepository = gameRoundRepository;
        this.cardRepository = cardRepository;
    }

    public GameRound createNewGameRound(Game game){
        GameRound gameRound = new GameRound();
        gameRound.setGameId(game.getGameId());
        gameRoundRepository.save(gameRound);
        gameRoundRepository.flush();
        gameRound.setCard(getActualCard(game.getActualGameRoundIndex()));

        return gameRound;
    }

    public GameRound startNewGameRound(Game game){
        if (game.getActualGameRoundIndex() < 12) {

            if (game.getActualGameRoundIndex() == 0) {
                Random random = new Random();
                int randomStart = random.nextInt(game.getNumberOfPlayers());
                game.setRandomStartPosition(randomStart);
            }

            game.setActualGameRoundIndex(game.getActualGameRoundIndex() + 1);
            GameRound actualGameRound = createNewGameRound(game);
            // this does not work because we will have to do + 1 gameRounds (actualGameRoundIndex rises..) if a player is guessing false and check if player is a Bot
            actualGameRound.setGuessingPlayerId(game.getPlayers().get((game.getActualGameRoundIndex() + game.getRandomStartPosition()) % game.getNumberOfPlayers()).getPlayerId());
            return actualGameRound;

        }
        else{
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Game has finished! No more gameRounds.");
        }
    }

    private Card getActualCard(int cardId){
        Card card = cardRepository.findByCardId((long) cardId);
        if (!(card == null)) {
            return card;
        }
        return card;
        // Todo: uncomment as soon as cards are in system
        /*else{
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CardId not found in CardRepo");
        } */

    }




}

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
        if (game.getActualGameRoundIndex() >= 12 || game.getGameStatus() == GameStatus.FINISHED){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Game has finished! No more gameRounds.");
        }
        GameRound gameRound = new GameRound();
        gameRound.setGameId(game.getGameId());
        List<Integer> list = new ArrayList<>(game.getCardIds());
        gameRound.setCard(getActualCard(list.get(game.getActualGameRoundIndex()-1)));
        gameRoundRepository.save(gameRound);
        gameRoundRepository.flush();

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
            long nextGuessingPlayerIdx = computeGuessingPlayerId(game);
            actualGameRound.setGuessingPlayerId(nextGuessingPlayerIdx);
            return actualGameRound;
        }
        else{
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Game has finished! No more gameRounds.");
        }
    }

    private long computeGuessingPlayerId(Game game){
        int numberOfPlayers = game.getPlayers().size();
        int actualGameRoundIdx = game.getActualGameRoundIndex();
        int randomStartPosition = game.getRandomStartPosition();

        // Todo: decrease randomStartPosition
        int nextGuessingPlayerIdx = (randomStartPosition + actualGameRoundIdx) % numberOfPlayers;
        if (game.getPlayers().get(nextGuessingPlayerIdx) instanceof PhysicalPlayer) {
            return game.getPlayers().get(nextGuessingPlayerIdx).getPlayerId();
        }
        else { return game.getPlayers().get((nextGuessingPlayerIdx + 1) % numberOfPlayers).getPlayerId(); }
    }

    private Card getActualCard(int cardId){
        Card card = cardRepository.findByCardId((long) cardId);
        if (!(card == null)) {
            return card;
        }
        else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CardId not found in CardRepo");
        }
    }

    public GameRound getGameRoundByRoundId(long roundId){
        return gameRoundRepository.findByGameRoundId(roundId);
    }

    public void chooseMisteryWord(GameRound gameRound, int wordNumber){
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
    }
}

package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;

import ch.uzh.ifi.seal.soprafs20.entity.Game;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class GameRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;


    @Test
    void findByGameId_success() {
        // given
        Game game = new Game();
        game.setNumberOfPlayers(4);
        game.setHasBot(true);
        game.setGameName("Thomas");
        game.setGameStatus(GameStatus.RUNNING);
        game.setRandomStartPosition(3);
        game.setActualGameRoundIndex(1);
        game.setAdminPlayerId(33L);

        entityManager.persist(game);
        entityManager.flush();

        // when
        Game found = gameRepository.findByGameId(game.getGameId());

        // then
        assertNotNull(found.getGameId());
        assertEquals(found.getNumberOfPlayers(), game.getNumberOfPlayers());
        assertEquals(found.getHasBot(), game.getHasBot());
        assertEquals(found.getGameName(), game.getGameName());
        assertEquals(found.getGameStatus(), game.getGameStatus());
        assertEquals(found.getRandomStartPosition(), game.getRandomStartPosition());
        assertEquals(found.getActualGameRoundIndex(), game.getActualGameRoundIndex());
        assertEquals(found.getAdminPlayerId(), game.getAdminPlayerId());

    }

    @Test
    void findByGameStatus_success() {
        // given
        Game game = new Game();
        game.setNumberOfPlayers(4);
        game.setHasBot(true);
        game.setGameName("Thomas");
        game.setGameStatus(GameStatus.RUNNING);
        game.setRandomStartPosition(3);
        game.setActualGameRoundIndex(1);
        game.setAdminPlayerId(33L);

        entityManager.persist(game);
        entityManager.flush();

        Game game1 = new Game();
        game1.setNumberOfPlayers(4);
        game1.setHasBot(true);
        game1.setGameName("Q");
        game1.setGameStatus(GameStatus.CREATED);
        game1.setRandomStartPosition(3);
        game1.setActualGameRoundIndex(0);
        game1.setAdminPlayerId(33L);

        entityManager.persist(game1);
        entityManager.flush();
        // when
        List<Game> found = gameRepository.findAllByGameStatus(GameStatus.RUNNING);

        // then
        assertEquals(1,found.size());
        assertNotNull(found.get(0).getGameId());
        assertEquals(found.get(0).getNumberOfPlayers(), game.getNumberOfPlayers());
        assertEquals(found.get(0).getHasBot(), game.getHasBot());
        assertEquals(found.get(0).getGameName(), game.getGameName());
        assertEquals(found.get(0).getGameStatus(), game.getGameStatus());
        assertEquals(found.get(0).getRandomStartPosition(), game.getRandomStartPosition());
        assertEquals(found.get(0).getActualGameRoundIndex(), game.getActualGameRoundIndex());
        assertEquals(found.get(0).getAdminPlayerId(), game.getAdminPlayerId());



    }

    @Test
    void findByGameName_success() {
        // given
        Game game = new Game();
        game.setNumberOfPlayers(4);
        game.setHasBot(true);
        game.setGameName("Mac");
        game.setGameStatus(GameStatus.RUNNING);
        game.setRandomStartPosition(3);
        game.setActualGameRoundIndex(1);
        game.setAdminPlayerId(33L);

        entityManager.persist(game);
        entityManager.flush();

        Game game1 = new Game();
        game1.setNumberOfPlayers(4);
        game1.setHasBot(true);
        game1.setGameName("Lokomotive");
        game1.setGameStatus(GameStatus.CREATED);
        game1.setRandomStartPosition(3);
        game1.setActualGameRoundIndex(0);
        game1.setAdminPlayerId(33L);

        entityManager.persist(game1);
        entityManager.flush();
        // when
        Game found = gameRepository.findByGameName("Mac");

        // then
        assertNotNull(found.getGameId());
        assertEquals(found.getNumberOfPlayers(), game.getNumberOfPlayers());
        assertEquals(found.getHasBot(), game.getHasBot());
        assertEquals(found.getGameName(), game.getGameName());
        assertEquals(found.getGameStatus(), game.getGameStatus());
        assertEquals(found.getRandomStartPosition(), game.getRandomStartPosition());
        assertEquals(found.getActualGameRoundIndex(), game.getActualGameRoundIndex());
        assertEquals(found.getAdminPlayerId(), game.getAdminPlayerId());





    }





}

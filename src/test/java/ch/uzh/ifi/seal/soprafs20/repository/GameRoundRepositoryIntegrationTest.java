package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class GameRoundRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Qualifier("gameRoundRepository")
    @Autowired
    private GameRoundRepository gameRoundRepository;


    @Test
    public void findByGameRoundId_success() {
        // given
        GameRound gameRound = new GameRound();
        gameRound.setEveryoneSubmitted(true);
        gameRound.setGameId(3L);
        gameRound.setGuessingPlayerId(2L);
        gameRound.setMysteryWord("Blossom");


        entityManager.persist(gameRound);
        entityManager.flush();

        // when
        GameRound found = gameRoundRepository.findByGameRoundId(gameRound.getGameRoundId());

        // then
        assertNotNull(found.getGameRoundId());
        assertEquals(found.getEveryoneSubmitted(), gameRound.getEveryoneSubmitted());
        assertEquals(found.getGameId(), gameRound.getGameId());
        assertEquals(found.getGuessingPlayerId(), gameRound.getGuessingPlayerId());
        assertEquals(found.getMysteryWord(), gameRound.getMysteryWord());

    }

}

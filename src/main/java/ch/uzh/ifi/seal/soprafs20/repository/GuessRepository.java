package ch.uzh.ifi.seal.soprafs20.repository;
import ch.uzh.ifi.seal.soprafs20.entity.Guess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository("guessRepository")
public interface GuessRepository extends JpaRepository<Guess, Long> {
    Guess findByPlayerIdAndGameRoundId(Long playerId, Long gameRoundId);
    Guess findByGameRoundId(Long gameRoundId);


}
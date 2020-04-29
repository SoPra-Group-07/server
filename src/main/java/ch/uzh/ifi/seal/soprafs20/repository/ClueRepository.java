package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.Clue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("clueRepository")
public interface ClueRepository extends JpaRepository<Clue, Long> {
    Clue findByPlayerId(Long playerId);
    Clue findByPlayerIdAndGameRoundId(Long playerId, Long gameRoundId);


}
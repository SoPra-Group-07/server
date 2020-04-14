package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Card;
import ch.uzh.ifi.seal.soprafs20.entity.Clue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("clueRepository")
public interface ClueRepository extends JpaRepository<Clue, Long> {         //Methods that are used to interact and work with DB
    Clue findBySubmissionId(Long submissionId);
    Clue findByPlayerId(Long playerId);


}
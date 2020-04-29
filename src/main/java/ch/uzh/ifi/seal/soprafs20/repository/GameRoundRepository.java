package ch.uzh.ifi.seal.soprafs20.repository;


import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("gameRoundRepository")
public interface GameRoundRepository extends JpaRepository<GameRound, Long> {
    GameRound findByGameRoundId(long gameRoundId);
    List<GameRound> findAllByGameId(long gameId);
}

   

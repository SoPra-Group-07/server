package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.entity.PlayerStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository("playerStatisticRepository")
public interface PlayerStatisticRepository extends JpaRepository<PlayerStatistic, Long> {         //Methods that are used to interact and work with DB
    PlayerStatistic findByPlayerStatisticId(long gameRoundId);
}



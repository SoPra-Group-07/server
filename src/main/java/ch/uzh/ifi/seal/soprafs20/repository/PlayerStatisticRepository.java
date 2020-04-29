package ch.uzh.ifi.seal.soprafs20.repository;


import ch.uzh.ifi.seal.soprafs20.entity.PlayerStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository("playerStatisticRepository")
public interface PlayerStatisticRepository extends JpaRepository<PlayerStatistic, Long> {
    PlayerStatistic findByPlayerStatisticId(long gameRoundId);
}



package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository("playerRepository")
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByUserId(long userId);
    Player findByPlayerId(long playerId);
    List<Player> findAllByGameId(long gameId);
    Player findByUserIdAndGameId(long userId, long gameId);
}
package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository("playerRepository")
public interface PlayerRepository extends JpaRepository<Player, Long> {         //Methods that are used to interact and work with DB
    Player findByUserId(long userId);
}
package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("cardRepository")
public interface CardRepository extends JpaRepository<Card, Long> {         //Methods that are used to interact and work with DB
    Card findByCardId(long cardId);
}
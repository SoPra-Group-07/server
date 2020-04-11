package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.Entity;

@Entity
public class MaliciousBot extends Player {
    @Override
    public Clue giveClue(String word) {
        return null;
    }
}

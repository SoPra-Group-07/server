package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class MaliciousBot extends Player implements Serializable {

    private static final long serialVersionUID = 1L;

    public MaliciousBot() {


        // this.setPlayerId(user.getUserId());
    }
    @Override
    public Clue giveClue(String word) {
        return null;
    }
}

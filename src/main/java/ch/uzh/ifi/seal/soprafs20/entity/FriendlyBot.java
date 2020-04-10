package ch.uzh.ifi.seal.soprafs20.entity;


import javax.persistence.Entity;

@Entity
public class FriendlyBot extends Player {

    public FriendlyBot() {

        // this.setPlayerId(user.getUserId());
    }
    @Override
    public Clue giveClue(String word) {
        return null;
    }
}

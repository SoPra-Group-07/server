package ch.uzh.ifi.seal.soprafs20.entity;


import javax.persistence.Entity;

@Entity
public class FriendlyBot extends Player {

    public FriendlyBot() {}

    @Override
    public Clue giveClue(String word) {
        return null;
    }
}

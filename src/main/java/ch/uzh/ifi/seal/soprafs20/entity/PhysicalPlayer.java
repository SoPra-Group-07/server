package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class PhysicalPlayer extends Player{
    /*
    // Todo: evt constructor for ID from User
    public PhysicalPlayer(User user) {
        this.user = user;
    }
     */

    @Column
    private User user;



    @Override
    public Clue giveClue(String word) {
        return null;
    }



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

package ch.uzh.ifi.seal.soprafs20.entity;


import javax.persistence.Entity;
import java.io.Serializable;
/**
 *Internal PhysicalPlayer Representation extends Player
 *This class composes the internal representation of the physicalPlayer and defines how the physicalPlayer is stored in the database.
 */
@Entity
public class PhysicalPlayer extends Player implements Serializable{

    private static final long serialVersionUID = 1L;

    public PhysicalPlayer() { /* empty constructor*/  }

    @Override
    public String giveClue(String myWord) {
        return myWord;
    }
}

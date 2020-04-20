package ch.uzh.ifi.seal.soprafs20.entity;


import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class PhysicalPlayer extends Player implements Serializable{

    private static final long serialVersionUID = 1L;

    public PhysicalPlayer() { /* empty constructor*/  }

    @Override
    public String giveClue(String myWord) {
        return myWord;
    }
}

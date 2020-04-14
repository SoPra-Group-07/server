package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 */
@Entity
public class Clue extends Submission {

    //private static final long serialVersionUID = 1L;
    public Clue(){}

    @Column()
    private boolean isDuplicate;

}
package ch.uzh.ifi.seal.soprafs20.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Internal Card Representation
 * This class composes the internal representation of the card and defines how the card is stored in the database.
 * Every variable will be mapped into a database field with the @Column annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes the primary key
 */
@Entity
@Table(name = "CARD")
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long cardId;

    // Todo: save arrayList as attribute?
    @Column
    private ArrayList<String> words;


    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }
}

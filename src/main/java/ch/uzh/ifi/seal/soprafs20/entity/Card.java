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

    @Column
    private String word1;
    @Column
    private String word2;
    @Column
    private String word3;
    @Column
    private String word4;
    @Column
    private String word5;


    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public String getWord1() {
        return word1;
    }

    public void setWord1(String word1) {
        this.word1 = word1;
    }

    public String getWord2() {
        return word2;
    }

    public void setWord2(String word2) {
        this.word2 = word2;
    }

    public String getWord3() {
        return word3;
    }

    public void setWord3(String word3) {
        this.word3 = word3;
    }

    public String getWord4() {
        return word4;
    }

    public void setWord4(String word4) {
        this.word4 = word4;
    }

    public String getWord5() {
        return word5;
    }

    public void setWord5(String word5) {
        this.word5 = word5;
    }
}

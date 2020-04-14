package ch.uzh.ifi.seal.soprafs20.entity;


import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.Column;

@Entity
public class PhysicalPlayer extends Player implements Serializable{

    private static final long serialVersionUID = 1L;
    // Todo: evt constructor for ID from User
    public PhysicalPlayer() {
        // super()
        /*this.setUser(user);
        this.setGameId(game.getGameId());
        this.setPlayerName(user.getUsername());
*/
       // this.setPlayerId(user.getUserId());
    }

    @Override
    public String giveClue(String myWord) {
        return myWord;
    }
}

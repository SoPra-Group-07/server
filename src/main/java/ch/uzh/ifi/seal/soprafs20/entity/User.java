package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Comparator;

//testee
/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes the primary key
 */
@Entity
@Table(name = "USER")
public class User implements Serializable{

    private static final long serialVersionUID = 1L;
    public User(){};

	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
    @Column(name = "user_id")
	private Long userId;
	
	@Column(nullable = false) 
	private String password;
	
	@Column(nullable = false, unique = true) 
	private String username;
	
	@Column(unique = true)
	private String token;

	@Column(nullable = false)
    private UserStatus status;

    @Column
    private LocalDate creationDate;

    @Column
    private String dateOfBirth;

    @Column
    private double highScore;

    @Column
    private int numberOfGamesPlayed;


    public int getNumberOfGamesPlayed() {
        return numberOfGamesPlayed;
    }

    public void setNumberOfGamesPlayed(int numberOfGamesPlayed) {
        this.numberOfGamesPlayed = numberOfGamesPlayed;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public double getHighScore() {
        return highScore;
    }

    public void setHighScore(double highScore) {
        this.highScore = highScore;
    }

    public Long getId() {
		return userId;
	}

	public void setId(Long id) {
		this.userId = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return creationDate;
    }

    public void setDate(LocalDate date) {
        this.creationDate = date;
    }

    public String getBirth() {
        return dateOfBirth;
    }

    public void setBirth(String birth) {
        this.dateOfBirth = birth;
    }


    public static Comparator<User> userHighscoreComparator = new Comparator<User>() {
        @Override
        public int compare(User userOne, User userTwo) {
            if (userOne.getHighScore() > userTwo.getHighScore()){
                return -1; }
            else if (userOne.getHighScore() < userTwo.getHighScore()){
                return 1;}
            return 0; }
    };



}

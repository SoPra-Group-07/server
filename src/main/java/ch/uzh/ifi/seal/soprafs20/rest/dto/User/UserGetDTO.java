package ch.uzh.ifi.seal.soprafs20.rest.dto.User;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;

import java.time.LocalDate;

public class UserGetDTO {

    private Long id;
    private String password;
    private String username;
    private UserStatus status;
    private LocalDate date;
    private String birth;
    private String token;
    private double highScore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public double getHighScore() {
        return highScore;
    }

    public void setHighScore(double highScore) {
        this.highScore = highScore;
    }
}

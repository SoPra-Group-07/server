package ch.uzh.ifi.seal.soprafs20.rest.dto.User;

public class UserEditDTO {

    private Long id;
    private String password;
    private String birth;

    public Long getId() {
        return this.id;
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

    public String getBirth() { return birth; }
    public void setBirth(String birth) { this.birth = birth; }
}
package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;
    

    @Test
    public void findByPassword_success() {
        // given
        User user = new User();
        user.setPassword("abc123");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setToken("1");


        entityManager.persist(user);
        entityManager.flush();

        // when
        User found = userRepository.findByPassword(user.getPassword());

        // then
        assertNotNull(found.getUserId());
        assertEquals(found.getPassword(), user.getPassword());
        assertEquals(found.getUsername(), user.getUsername());
        assertEquals(found.getToken(), user.getToken());
        assertEquals(found.getStatus(), user.getStatus());

    }



    @Test
    public void findByUsername_success() {
        // given
        User user = new User();
        user.setPassword("abc123");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setToken("1");

        entityManager.persist(user);
        entityManager.flush();

        // when
        User found = userRepository.findByUsername(user.getUsername());

        // then
        assertNotNull(found.getUserId());
        assertEquals(found.getPassword(), user.getPassword());
        assertEquals(found.getUsername(), user.getUsername());
        assertEquals(found.getToken(), user.getToken());
        assertEquals(found.getStatus(), user.getStatus());
        assertEquals(found.getBirth(), user.getBirth());
        assertEquals(found.getDate(), user.getDate());

    }

    @Test
    public void findByToken_success() {
        // given
        User user = new User();
        user.setPassword("abc123");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setToken("7");

        entityManager.persist(user);
        entityManager.flush();

        // when
        User found = userRepository.findByToken(user.getToken());

        // then
        assertNotNull(found.getUserId());
        assertEquals(found.getPassword(), user.getPassword());
        assertEquals(found.getUsername(), user.getUsername());
        assertEquals(found.getToken(), user.getToken());
        assertEquals(found.getStatus(), user.getStatus());
        assertEquals(found.getBirth(), user.getBirth());
        assertEquals(found.getDate(), user.getDate());

    }







}

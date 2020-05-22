package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
class UserServiceIntegrationTest {

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("playerRepository")
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setup() {
        playerRepository.deleteAll();
        gameRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createUser_validInputs_success() {                        //----------------->User registers with success
        // given
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setPassword("testPassword");
        testUser.setUsername("testUsername");

        // when
        User createdUser = userService.createUser(testUser);

        // the
        assertEquals(testUser.getUserId(), createdUser.getUserId());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertNull(createdUser.getToken());
        assertEquals(UserStatus.OFFLINE, createdUser.getStatus());
    }

    @Test
    void createUser_duplicateUsername_throwsException() {          //----------------------------------------------> Status code 409 test - "Username already exists!"
        assertNull(userRepository.findByUsername("testUsername"));        //                                                when you want to register with an already existing username

        User testUser = new User();
        testUser.setPassword("abc123");
        testUser.setUsername("testUsername");
        User createdUser = userService.createUser(testUser);

        User testUser2 = new User();

        testUser2.setPassword("123abc");
        testUser2.setUsername("testUsername");


        String exceptionMessage = "Username already exists!";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser2), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());       // --> getReason for ResponseStatusException instead of getMessage
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }



    @Test
    void UserDoesNotExist_soYouCannotViewProfile_throwsException() {          //----------------------------------------------> Status code 404 test - "User not found"
        assertNull(userRepository.findByUsername("testUsername"));                   //                                                When you want to view the users profile
                                                                                     //                                                but it does not exist.
        User testUser = null;

        String exceptionMessage = "User not found";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.userEqualsNull(testUser), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }



    @Test
    void UserDoesNotExist_soYouCannotEdit_throwsException() {

        assertNull(userRepository.findByUsername("testUsername"));            //                                                when you want to edit the user
                                                                              //                                                but it does not exist

        String exceptionMessage = "User not found";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.userEqualsNull(null), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }




}

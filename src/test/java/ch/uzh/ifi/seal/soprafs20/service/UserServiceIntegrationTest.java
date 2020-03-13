package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
public class UserServiceIntegrationTest {

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    public void createUser_validInputs_success() {                        //----------------->User registers with success
        // given
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setPassword("testPassword");
        testUser.setUsername("testUsername");

        // when
        User createdUser = userService.createUser(testUser);

        // then
        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertNull(createdUser.getToken());
        assertEquals(UserStatus.OFFLINE, createdUser.getStatus());
    }

    @Test
    public void createUser_duplicateUsername_throwsException() {          //----------------------------------------------> Status code 409 test - "Username already exists!"
        assertNull(userRepository.findByUsername("testUsername"));        //                                                when you want to register with an already existing username

        User testUser = new User();
        testUser.setPassword("abc123");
        testUser.setUsername("testUsername");
        User createdUser = userService.createUser(testUser);

        // attempt to create second user with same username
        User testUser2 = new User();

        // change the name but forget about the username
        testUser2.setPassword("123abc");
        testUser2.setUsername("testUsername");

        // check that an error is thrown
        String exceptionMessage = "Username already exists!";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser2), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());       // --> getReason for ResponseStatusException instead of getMessage
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }



    @Test
    public void UserDoesNotExist_soYouCannotViewProfile_throwsException() {          //----------------------------------------------> Status code 404 test - "User not found"
        assertNull(userRepository.findByUsername("testUsername"));                   //                                                When you want to view the users profile
                                                                                     //                                                but it does not exist.
        User testUser = null;

        // check that an error is thrown
        String exceptionMessage = "User not found";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.userEqualsNull(testUser), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }



    @Test
    public void UserDoesNotExist_soYouCannotEdit_throwsException() {          //----------------------------------------------> Status code 404 test - "User not found"
        assertNull(userRepository.findByUsername("testUsername"));            //                                                when you want to edit the user
                                                                              //                                                but it does not exist
        User testUser = null;

        // check that an error is thrown
        String exceptionMessage = "User not found";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.userEqualsNull(testUser), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }


}

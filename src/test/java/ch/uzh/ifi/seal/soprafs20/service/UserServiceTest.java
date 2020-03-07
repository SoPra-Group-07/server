package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // given
        testUser = new User();
        testUser.setId(1L);
        testUser.setPassword("testPassword");
        testUser.setUsername("testUsername");

        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
    }

    @Test
    public void createUser_validInputs_success() {
        // when -> any object is being save in the userRepository -> return the dummy testUser
        User createdUser = userService.createUser(testUser);

        // then
       // Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());                              <--- Qui funziona anche senza

        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertNull(createdUser.getToken());
        assertEquals(UserStatus.OFFLINE, createdUser.getStatus());
    }




/*
    @Test                                                                              //Commented out because duplicate passwords are allowed
    public void createUser_duplicatePassword_throwsException() {
        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        //Mockito.when(userRepository.findByPassword(Mockito.any())).thenReturn(testUser);
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

        // then -> attempt to create second user with same user -> check that an error is thrown
        String exceptionMessage = "Password already exists!";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.createUser(null), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());
    }

*/



    @Test
    public void createUser_duplicateInputs_throwsException() {
        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        //Mockito.when(userRepository.findByPassword(Mockito.any())).thenReturn(testUser);         Duplicate passwords are allowed
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        // then -> attempt to create second user with same user -> check that an error is thrown
        String exceptionMessage = "Username already exists!";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());
    }


}

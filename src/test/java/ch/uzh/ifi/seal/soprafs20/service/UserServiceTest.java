package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    private UserService userService;
    @InjectMocks
    private User testUser;
    @InjectMocks
    private User testUser1;
    @InjectMocks
    private User editUser;
    @InjectMocks
    private User editUser1;
    @InjectMocks
    private User notExistingUser;


    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);

        // given
        testUser.setUserId(1L);
        testUser.setPassword("testPassword");
        testUser.setUsername("testUsername");


        testUser1.setUserId(2L);
        testUser1.setPassword("testPassword1");
        testUser1.setUsername("tesUsername1");

    }

    /**
     * tests that createUser() creates a user with the given parameters
     */
    @Test
    void createUser_validInputs_success() {
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);

        User createdUser = userService.createUser(testUser);


        assertEquals(testUser.getUserId(), createdUser.getUserId());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertNull(createdUser.getToken());
        assertEquals(UserStatus.OFFLINE, createdUser.getStatus());
    }

    /**
     * tests that the method checkIfUserExists() returns true if the user exists in the userRepository
     */
    @Test
    void test_checkIfUserExists(){
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
        Mockito.when(userRepository.existsUserByUsername(Mockito.any())).thenReturn(true);

        boolean userNameExists = userService.checkUsername(testUser);
        assertTrue(userNameExists);
    }

    /**
     * tests that the method acceptLogin() returns true if the password is matching to the username
     * and false otherwise.
     */
    @Test
    void test_acceptLogin(){
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        assertTrue(userService.acceptLogin(testUser.getUsername(), testUser.getPassword()));

        assertFalse(userService.acceptLogin(testUser.getUsername(), "notMyPassword"));
    }

    /**
     * tests that the method isAlreadyLoggedIn returns true if a user is already logged in (UserStatus.ONLINE)
     * and false (UserStauts.OFFLINE) otherwise
     */
    @Test
    void test_isAlreadyLoggedIn(){

        testUser.setStatus(UserStatus.ONLINE);
        testUser1.setStatus(UserStatus.OFFLINE);
        Mockito.when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);
        Mockito.when(userRepository.findByUsername(testUser1.getUsername())).thenReturn(testUser1);
        assertTrue(userService.isAlreadyLoggedIn(testUser.getUsername()));
        assertFalse(userService.isAlreadyLoggedIn(testUser1.getUsername()));
    }

    /**
     * tests that the methods login() loggs in a user and changes it's userStatus to ONLINE
     */
    @Test
    void test_login_success(){

        testUser.setStatus(UserStatus.OFFLINE);
        testUser1.setStatus(UserStatus.OFFLINE);
        Mockito.when(userRepository.findByUsername(testUser.getUsername())).thenReturn(testUser);
        Mockito.when(userRepository.findByUsername(testUser1.getUsername())).thenReturn(testUser1);
        userService.login(testUser);

        assertEquals(testUser.getStatus(), UserStatus.ONLINE);
        assertEquals(UserStatus.OFFLINE, testUser1.getStatus());
    }


    /**
     * tests that the methods login() loggs in a user and changes it's userStatus to ONLINE
     */
    @Test
    void test_logout_success(){
        testUser.setToken("token");

        testUser.setStatus(UserStatus.ONLINE);
        Mockito.when(userRepository.findByToken(testUser.getToken())).thenReturn(testUser);
        userService.logout(testUser);

        assertEquals(testUser.getStatus(), UserStatus.OFFLINE);

    }

    /**
     * tests that the method edit() edits the user if new credentials
     * (username, birthdate) were given
     */
    @Test
    void test_edit_with_new_credentials(){

        testUser1.setDateOfBirth("22-03-1997");
        notExistingUser.setPassword("notHere");
        notExistingUser.setUsername("NoName");

        editUser.setUserId(1L);
        editUser.setPassword("xy");
        editUser.setUsername("changedUsername");
        editUser.setDateOfBirth("06-08-1996");

        editUser1.setUserId(2L);
        editUser1.setPassword("xy");
        editUser1.setUsername(null);
        editUser1.setDateOfBirth(null);

        testUser.setStatus(UserStatus.ONLINE);
        testUser1.setStatus(UserStatus.ONLINE);
        Mockito.when(userRepository.getOne(editUser.getUserId())).thenReturn(testUser);
        Mockito.when(userRepository.getOne(testUser1.getUserId())).thenReturn(testUser1);

        userService.edit(editUser);
        assertNotEquals(editUser.getUsername(), testUser.getUsername());
        assertEquals(editUser.getPassword(), testUser.getPassword());
        assertEquals(editUser.getDateOfBirth(), testUser.getDateOfBirth());

    }

    /**
     * test that edit does not edit the user if no new credentials were given
     */
    @Test
    void test_edit_without_new_credentials(){

        testUser1.setDateOfBirth("22-03-1997");

        editUser1.setUserId(2L);
        editUser1.setPassword("xy");
        editUser1.setUsername(null);
        editUser1.setDateOfBirth(null);


        testUser1.setStatus(UserStatus.ONLINE);
        Mockito.when(userRepository.getOne(testUser1.getUserId())).thenReturn(testUser1);

        userService.edit(editUser1);
        assertNotNull(testUser1.getDateOfBirth());
        assertNotNull(testUser1.getUsername());

    }

    /**
     * tests that an ResponseStatusException is thrown by the method edit()
     * if the user could not be find in the repository
     */
    @Test
    void test_edit_user_not_found(){

        notExistingUser.setPassword("notHere");
        notExistingUser.setUsername("NoName");

        Mockito.when(userRepository.getOne(notExistingUser.getUserId())).thenReturn(null);


        String exceptionMessage = "User not found";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.edit(notExistingUser), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }


    /**
     * tests that the method createUser() throws an Responsestatusexcepition if one tries to
     * create an User with an username that already exists.
     */
    @Test
    void createUser_duplicateInputs_throwsException() {
        userService.createUser(testUser);


        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        String exceptionMessage = "Username already exists!";
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser), exceptionMessage);
        assertEquals(exceptionMessage, exception.getReason());
    }


}

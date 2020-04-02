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

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LeaderboardServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LeaderboardService leaderboardService;
    private UserService userService;
    private User user1;
    private User user2;
    private User user3;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // given
        User user1 = new User();
        user1.setPassword("testPassword");
        user1.setUsername("User1");
        user1.setStatus(UserStatus.OFFLINE);
        user1.setId(1L);
        user1.setToken("2dfc-g59k");
        user1.setDate(LocalDate.now());
        user1.setBirth("00-00-0000");
        user1.setHighScore(5.3);

        User user2 = new User();
        user2.setPassword("testPassword");
        user2.setUsername("User2");
        user2.setStatus(UserStatus.OFFLINE);
        user2.setId(1L);
        user2.setToken("2dfc-g59k");
        user2.setDate(LocalDate.now());
        user2.setBirth("00-00-0000");
        user2.setHighScore(8.1);

        User user3 = new User();
        user3.setPassword("testPassword");
        user3.setUsername("User3");
        user3.setStatus(UserStatus.OFFLINE);
        user3.setId(1L);
        user3.setToken("2dfc-g59k");
        user3.setDate(LocalDate.now());
        user3.setBirth("00-00-0000");
        user3.setHighScore(3.1);
    }

    @Test
    public void TestSortByHighScore() {
        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);


        leaderboardService.fillUsers();
        leaderboardService.sortByHighScore();

        ArrayList<User> users = new ArrayList<>();
        users.add(user2);
        users.add(user1);
        users.add(user3);

        assertEquals(leaderboardService.getUsers(), users);
    }




/*
    @Test                                                                                                      //Commented out because duplicate passwords are allowed
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

}


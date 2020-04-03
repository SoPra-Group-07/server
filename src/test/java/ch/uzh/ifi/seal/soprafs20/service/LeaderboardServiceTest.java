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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class LeaderboardServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    User user1;
    @InjectMocks
    User user2;
    @InjectMocks
    User user3;
    @InjectMocks
    Leaderboard leaderboard;
    @InjectMocks
    LeaderboardService leaderboardService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        leaderboard = Leaderboard.getInstance();
        leaderboardService = new LeaderboardService(userRepository);

        // given
        user1.setPassword("testPassword");
        user1.setUsername("User1");
        user1.setStatus(UserStatus.OFFLINE);
        user1.setId(1L);
        user1.setToken("2dfc-g59k");
        user1.setDate(LocalDate.now());
        user1.setBirth("00-00-0000");
        user1.setHighScore(5.3);


        user2.setPassword("testPassword");
        user2.setUsername("User2");
        user2.setStatus(UserStatus.OFFLINE);
        user2.setId(1L);
        user2.setToken("2dfc-g59k");
        user2.setDate(LocalDate.now());
        user2.setBirth("00-00-0000");
        user2.setHighScore(8.1);

        user3.setPassword("testPassword");
        user3.setUsername("User3");
        user3.setStatus(UserStatus.OFFLINE);
        user3.setId(1L);
        user3.setToken("2dfc-g59k");
        user3.setDate(LocalDate.now());
        user3.setBirth("00-00-0000");
        user3.setHighScore(3.1);

        List<User> allUsers = new LinkedList<User>();
        allUsers.add(user1);
        allUsers.add(user2);
        allUsers.add(user3);

        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(userRepository.findAll()).thenReturn(allUsers);
    }

    @Test
    public void testSortByHighscore(){
        leaderboardService.fillUsers();
        leaderboardService.sortByHighScore();

        ArrayList<User> users = leaderboard.getUsers();
        assertEquals(users.get(0), user2);
        assertEquals(users.get(1), user1);
        assertEquals(users.get(2), user3);

        assertTrue(users.get(0).getHighScore() >= users.get(1).getHighScore());
        assertTrue(users.get(0).getHighScore() >= users.get(2).getHighScore());
        assertTrue(users.get(1).getHighScore() >= users.get(2).getHighScore());

    }

}

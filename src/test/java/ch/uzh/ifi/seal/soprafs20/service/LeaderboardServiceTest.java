package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Leaderboard;
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


class LeaderboardServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    User user1;
    @InjectMocks
    User user2;
    @InjectMocks
    User user3;
    @InjectMocks
    User user4;
    @InjectMocks
    Leaderboard leaderboard;
    @InjectMocks
    LeaderboardService leaderboardService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);

        leaderboard = Leaderboard.getInstance();
        leaderboardService = new LeaderboardService(userRepository);


        user1.setPassword("testPassword");
        user1.setUsername("User1");
        user1.setStatus(UserStatus.OFFLINE);
        user1.setUserId(1L);
        user1.setToken("2dfc-g59k");
        user1.setCreationDate(LocalDate.now());
        user1.setDateOfBirth("00-00-0000");
        user1.setHighScore((float) 5.3);
        user1.setNumberOfGamesPlayed(1);


        user2.setPassword("testPassword");
        user2.setUsername("User2");
        user2.setStatus(UserStatus.OFFLINE);
        user2.setUserId(1L);
        user2.setToken("2dfc-g59k");
        user2.setCreationDate(LocalDate.now());
        user2.setDateOfBirth("00-00-0000");
        user2.setHighScore((float) 8.1);
        user2.setNumberOfGamesPlayed(10);

        user3.setPassword("testPassword");
        user3.setUsername("User3");
        user3.setStatus(UserStatus.OFFLINE);
        user3.setUserId(1L);
        user3.setToken("2dfc-g59k");
        user3.setCreationDate(LocalDate.now());
        user3.setDateOfBirth("00-00-0000");
        user3.setHighScore((float) 3.1);
        user3.setNumberOfGamesPlayed(5);

        user4.setPassword("testPassword");
        user4.setUsername("User4");
        user4.setStatus(UserStatus.OFFLINE);
        user4.setUserId(1L);
        user4.setNumberOfGamesPlayed(0);


        LinkedList<User> allUsers = new LinkedList<User>();
        allUsers.add(user1);
        allUsers.add(user2);
        allUsers.add(user3);
        allUsers.add(user4);

        Mockito.when(userRepository.findAll()).thenReturn(allUsers);
    }

    @Test
    /***
     * this test makes sure the SortByHighsore method sorts the users array in the
     * Leaderboard class in DESCENDING order
     *
     */
    void testSortByHighscore(){
        leaderboardService.fillUsers();
        leaderboardService.sortByHighScore();

        List<User> users = leaderboard.getUsers();
        assertEquals(user2, users.get(0));
        assertEquals(user1, users.get(1));

        assertEquals(3, users.size());
        assertFalse(users.contains(user4));

        assertTrue(users.get(0).getHighScore() >= users.get(1).getHighScore());
        assertTrue(users.get(0).getHighScore() >= users.get(2).getHighScore());
        assertTrue(users.get(1).getHighScore() >= users.get(2).getHighScore());
    }

    @Test
    /***
     * this makes sure the method fillUsers only adds Users with attribute
     * numberOfGamesPlayed > 0 to the users ArrayList in the Leaderboard class
     */
    void TestfillUsers(){
        leaderboardService.fillUsers();

        List<User> users = leaderboard.getUsers();
        assertEquals(3, users.size());
        assertFalse(users.contains(user4));
    }

    @Test
    /***
     *  Test getUsers()
     */
    void testGetUsers() {
        List<User> allUsers = new ArrayList<>();
        allUsers.add(user1);
        allUsers.add(user2);
        allUsers.add(user3);

        leaderboardService.fillUsers();

        assertEquals(allUsers, leaderboardService.getUsers());
    }
}

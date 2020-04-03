package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.rest.dto.User.UserEditDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.User.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.service.Leaderboard;
import ch.uzh.ifi.seal.soprafs20.service.LeaderboardService;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(LeaderboardController.class)
public class LeaderboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LeaderboardService leaderboardService;

    @Test
    public void givenUsers_whenGetLeaderboard_thenReturnJsonArray() throws Exception {        //  --------------------------------------------------->   GET "/users" test
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

        ArrayList<User> allUsers = new ArrayList<User>();
        allUsers.add(user1);
        allUsers.add(user2);
        allUsers.add(user3);


        // this mocks the LeaderboardService -> we define above what the userService should return when getUsers() is called
        given(leaderboardService.getUsers()).willReturn(allUsers);

        // when
        MockHttpServletRequestBuilder getRequest = get("/leaderboards").contentType(MediaType.APPLICATION_JSON);    //=give it to me as JSON

        /*
        MvcResult result = mockMvc.perform(getRequest).andReturn();
        MockHttpServletResponse response = result.getResponse();
        //assertEquals(HttpStatus.OK.value(), response.getStatus());
        response.getErrorMessage();
        System.out.println(response.getContentAsString());
        System.out.println(result);
         */

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))                    // <-- Content-Type accepted?
                .andExpect(jsonPath("$[0].username", is(user1.getUsername())))         // is("testPassword") would work as well
                .andExpect(jsonPath("$[0].highScore", is(user1.getHighScore())))
                .andExpect(jsonPath("$[1].username", is(user2.getUsername())))         // is("testPassword") would work as well
                .andExpect(jsonPath("$[1].highScore", is(user2.getHighScore())))
                .andExpect(jsonPath("$[2].username", is(user3.getUsername())))         // is("testPassword") would work as well
                .andExpect(jsonPath("$[2].highScore", is(user3.getHighScore())));
    }



    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new SopraServiceException(String.format("The request body could not be created.%s", e.toString()));
        }
    }
}
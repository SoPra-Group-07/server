package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.rest.dto.User.UserEditDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.User.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {        //  --------------------------------------------------->   GET "/users" test
        // given
        User user = new User();
        user.setPassword("testPassword");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setUserId(1L);
        user.setToken("2dfc-g59k");
        user.setDate(LocalDate.now());
        user.setBirth("00-00-0000");

        List<User> allUsers = Collections.singletonList(user);

        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(userService.getUsers()).willReturn(allUsers);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON);    //=give it to me as JSON

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))                    // <-- Content-Type accepted?
                .andExpect(jsonPath("$[0].password", is(user.getPassword())))         // is("testPassword") would work as well
                .andExpect(jsonPath("$[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$[0].status", is(user.getStatus().toString())))
                .andExpect(jsonPath("$[0].birth", is(user.getBirth())))
                .andExpect(jsonPath("$[0].token", is(user.getToken())))
                .andExpect(jsonPath("$[0].id", is(user.getUserId().intValue())))          // or is(1) works as well
                .andExpect(jsonPath("$[0].date", is(user.getCreationDate().toString())));
    }

    @Test
    public void createUser_validInput_userCreated() throws Exception {             //  --------------------------------------------------->    POST "/users" test
        // given
        User user = new User();
        user.setUserId(1L);
        user.setPassword("testPassword");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.OFFLINE);
        user.setDate(LocalDate.now());
        user.setBirth("00-00-0000");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("Test User");
        userPostDTO.setUsername("testUsername");

        given(userService.createUser(Mockito.any())).willReturn(user);         //with 'given' you can mock (=imitate) the createUser() method. It then returns the user

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));


        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(user.getUserId().intValue())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.status", is(user.getStatus().toString())))
                .andExpect(jsonPath("$.birth", is("00-00-0000")))            //alternative to user.getBirth()
                .andExpect(jsonPath("$.date", is(user.getCreationDate().toString())))
                .andExpect(jsonPath("$.token", is(user.getToken())));
    }

    @Test
    public void loginUser_validInput_userLoggedIn() throws Exception {             //  --------------------------------------------------->   PUT "/login" test
        // given
        User user = new User();
        user.setUserId(1L);
        user.setPassword("testPassword");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);
        user.setDate(LocalDate.now());
        user.setBirth("00-00-0000");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("testPassword");
        userPostDTO.setUsername("testUsername");


        given(userService.checkUsername(Mockito.any())).willReturn(true);                     //Mocking the 'checkUsername(..)' method with return-value true.
        given(userService.acceptLogin(Mockito.any(),Mockito.any())).willReturn(true);         //Mocking the 'acceptLogin(..)' method with return-value true.
        given(userService.isAlreadyLoggedIn(Mockito.any())).willReturn(false);                //Mocking the 'isAlreadyLoggedIn(..)' method with return-value true.
        given(userService.login(Mockito.any())).willReturn(user);                               //Mocking the 'login(..)' method in order to have as the returned value the user
                                                                                                //created above.
        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));                       // When we perform the PUT request the body (userPostDto) gets converted to Json and sent to the '/login' url

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getUserId().intValue())))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.password", is(user.getPassword())))           //E.g., '$.password' is an expression to select the wanted information from a Json object
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.status", is(user.getStatus().toString())))
                .andExpect(jsonPath("$.birth", is("00-00-0000")))
                .andExpect(jsonPath("$.date", is(user.getCreationDate().toString())))
                .andExpect(jsonPath("$.token", is(user.getToken())));
    }


    @Test
    public void userProfiles_whenGetProfile_returnUserProfile() throws Exception {             //  --------------------------------------------------->    GET "/users/{userId}" test
        // given
        User user = new User();
        user.setUserId(0L);
        user.setPassword("testPassword");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);
        user.setDate(LocalDate.now());
        user.setBirth("00-00-0000");


        given(userService.getUserById(user.getUserId())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/users/{userId}",0).contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getUserId().intValue())))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.status", is(user.getStatus().toString())))
                .andExpect(jsonPath("$.birth", is("00-00-0000")))
                .andExpect(jsonPath("$.date", is(user.getCreationDate().toString())))
                .andExpect(jsonPath("$.token", is(user.getToken())));
    }


    @Test
    public void editUser_validInput_userEdited() throws Exception {             //  --------------------------------------------------->   PUT "/users/{userId}" test
        // given
        //user
        User user = new User();
        user.setUserId(0L);
        user.setPassword("123");
        user.setUsername("user0");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);
        user.setDate(LocalDate.now());
        user.setBirth("00-00-0000");

        //user after edit
        User edited_user = new User();
        edited_user.setUserId(0L);
        edited_user.setPassword("123");

        edited_user.setToken("1");
        edited_user.setStatus(UserStatus.ONLINE);
        edited_user.setDate(LocalDate.now());
        edited_user.setBirth("18-01-1999");


        UserEditDTO userEditDTO = new UserEditDTO();
        userEditDTO.setId(0L);

        userEditDTO.setBirth("18-01-1999");


        given(userService.edit(Mockito.any())).willReturn(edited_user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/users/{userId}",0)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userEditDTO));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.id", is(edited_user.getUserId().intValue())))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))                           //We need it to check whether the content-type is 'APPLICATION_JSON'
                .andExpect(jsonPath("$.password", is(edited_user.getPassword())))
                .andExpect(jsonPath("$.username", is(edited_user.getUsername())))
                .andExpect(jsonPath("$.status", is(edited_user.getStatus().toString())))
                .andExpect(jsonPath("$.birth", is(edited_user.getBirth())))
                .andExpect(jsonPath("$.date", is(edited_user.getCreationDate().toString())))
                .andExpect(jsonPath("$.token", is(edited_user.getToken())));
    }


    @Test
    public void userIsAlreadyLoggedIn_Test() throws Exception {             //  --------------------------------------------------->   Status code 204 test - "No Content"
        // given
        User user = new User();
        user.setUserId(1L);
        user.setPassword("testPassword");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);
        user.setDate(LocalDate.now());
        user.setBirth("00-00-0000");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("testPassword");
        userPostDTO.setUsername("testUsername");


        given(userService.checkUsername(Mockito.any())).willReturn(true);
        given(userService.acceptLogin(Mockito.any(),Mockito.any())).willReturn(true);
        given(userService.isAlreadyLoggedIn(Mockito.any())).willReturn(true);


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());

    }

    @Test
    public void usernameDoesNotExistWhileLoggingIn_Test() throws Exception {             //  --------------------------------------------------->   Status code 401 test - "Unauthorized"
        // given
        User user = new User();
        user.setUserId(1L);
        user.setPassword("testPassword");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);
        user.setDate(LocalDate.now());
        user.setBirth("00-00-0000");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("testPassword");
        userPostDTO.setUsername("testUsername");


        given(userService.checkUsername(Mockito.any())).willReturn(false);


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isUnauthorized());

    }


    @Test
    public void PasswordIsWrongWhileLoggingIn_Test() throws Exception {             //  --------------------------------------------------->   Status code 401 test - "Unauthorized"
        // given
        User user = new User();
        user.setUserId(1L);
        user.setPassword("testPassword");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);
        user.setDate(LocalDate.now());
        user.setBirth("00-00-0000");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("testPassword");
        userPostDTO.setUsername("testUsername");


        given(userService.checkUsername(Mockito.any())).willReturn(true);
        given(userService.acceptLogin(Mockito.any(),Mockito.any())).willReturn(false);


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isUnauthorized());

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
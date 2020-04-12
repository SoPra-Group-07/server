package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.User.UserEditDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.User.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.User.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.seal.soprafs20.rest.dto.User.UserTokenDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)                                                  // Status code 200 ->  if everything went well
    @ResponseBody
    public List<UserGetDTO> getAllUsers() {
        // fetch all users in the internal representation
        List<User> users = userService.getUsers();                                  //creates list with all users in internal representation
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)                                               //It is the status code 201
    @ResponseBody
    public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {              //DTO is a Data Transfer Object. It carries Data between processes -> Encapsulation
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        User createdUser = userService.createUser(userInput);                         //User status is set to OFFLINE, the birth date is set to "00-00-0000" and the creation date
                                                                                      //is set corresponding to the local date. Everything is saved in the database and the created user
        // convert internal representation of user back to API                        //is returned.
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }

    @PutMapping("/login")
    @ResponseStatus(HttpStatus.OK)                                                                 // Status code 200 ->  if everything went well
    @ResponseBody
    public UserGetDTO login(@RequestBody UserPostDTO userPostDTO) {                                //RequestBody converts from JSON-body from request into UserPostDTO instance
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        if (userService.checkUsername(userInput)) {                                                //Checks if a user with that username exists
            if (userService.acceptLogin(userInput.getUsername(), userInput.getPassword())) {       //checks if the password is correct
                if(userService.isAlreadyLoggedIn(userInput.getUsername())){                        //Checks if the user is already ONLINE (maybe from another device) and if so
                    throw new ResponseStatusException(HttpStatus.NO_CONTENT);                      // it sends back the status code 204
                } else {
                    User updatedUser = userService.login(userInput);                               //updates user-object, e.g., UserStatus is set to ONLINE, etc...
                    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(updatedUser);
                }

            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credentials are wrong");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credentials are wrong");
        }
    }

    @GetMapping("users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getUser(@PathVariable String userId) {
        long id;
        id = Long.parseLong(userId);
        User user = userService.getUserById(id);
        if (!userService.userEqualsNull(user)) {
            return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @PutMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserGetDTO editUser(@RequestBody UserEditDTO editUser) {
        User userToEdit = DTOMapper.INSTANCE.convertUserEditDTOtoEntity(editUser);

        User editedUser = userService.edit(userToEdit);

        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(editedUser);
    }


    @PutMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO logout(@RequestBody UserTokenDTO tokenDTO){
        User userInput = DTOMapper.INSTANCE.convertUserTokenDTOtoEntity(tokenDTO);

        User loggedOutUser = userService.logout(userInput);
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(loggedOutUser);
    }
}

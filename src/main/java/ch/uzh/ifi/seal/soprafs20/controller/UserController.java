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
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers() {
        List<User> users = userService.getUsers();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        User createdUser = userService.createUser(userInput);
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }

    @PutMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO login(@RequestBody UserPostDTO userPostDTO) {
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        if (Boolean.TRUE.equals(userService.checkUsername(userInput))) {
            if (Boolean.TRUE.equals(userService.acceptLogin(userInput.getUsername(), userInput.getPassword()))) {
                if (Boolean.TRUE.equals(userService.isAlreadyLoggedIn(userInput.getUsername()))){
                    User loggedInUser = userService.getUserByUsername(userInput.getUsername());
                    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(loggedInUser);
                }
                else {
                    User updatedUser = userService.login(userInput);
                    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(updatedUser);
                }

            }
            else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credentials are wrong");
            }
        }
        else {
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
        boolean userNotNull = !userService.userEqualsNull(user);
        if (Boolean.TRUE.equals(userNotNull)) {
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

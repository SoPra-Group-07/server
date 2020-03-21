package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {                   // <-- This method gets called at 'public List<UserGetDTO> getAllUsers()'. It returns a list of all users found in the database.
        return this.userRepository.findAll();
    }




    public User createUser(User newUser) {                    // <-- This method gets called at 'public UserGetDTO createUser(user)'. It sets the Status to OFFLINE,
                                                              //     it sets the creation date and it also sets a default birthday e.g., "00-00-0000"
        newUser.setStatus(UserStatus.OFFLINE);                //     Finally, the new user is returned.
        LocalDate currentDate = LocalDate.now();
        newUser.setDate(currentDate);

        newUser.setBirth("00-00-0000");

        checkIfUserNameExists(newUser);

        // saves the given entity but data is only persisted in the database once flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }


    private void checkIfUserNameExists(User userToBeCreated) {                                // <-- If a user is found by its username then an exception is thrown.
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());   //     Else, nothing happens.

        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists!");           //Throws status code 409
        }
    }






    public Boolean checkUsername(User userToCheck) {                               // <-- This method gets called in 'public UserGetDTO login(@RequestBody UserPostDTO userPostDTO)'.
        return userRepository.existsUserByUsername(userToCheck.getUsername());     //     If a user exists with this username then true is returned. Else, false.
    }



    //public Boolean existsUserByUsername (String userName){
    //    return this.userRepository.existsUserByUsername(userName);
    //}



    public Boolean acceptLogin(String username, String password) {                          // <-- This method gets called in 'public UserGetDTO login(@RequestBody UserPostDTO userPostDTO)'.
        return userRepository.findByUsername(username).getPassword().equals(password);      //     If the entered password equals the password of the specific username then true is returned.
    }                                                                                       //     Else, false.





    public Boolean isAlreadyLoggedIn(String username){                                                // <-- This method gets called in 'public UserGetDTO login(@RequestBody UserPostDTO userPostDTO)'.
        return this.userRepository.findByUsername(username).getStatus().equals(UserStatus.ONLINE);    // <-- If a user is already online then true is returned. Else, false.

    }

    public User login(User user) {                                 // <-- This method gets called in 'public UserGetDTO login(@RequestBody UserPostDTO userPostDTO)'.
        User loggedUser = getUserByUsername(user.getUsername());   //
        loggedUser.setToken(UUID.randomUUID().toString());         //     random Token is generated and given to user ( = saved in DB)
                                                                   //
        loggedUser.setStatus(UserStatus.ONLINE);                   //     User status is set to ONLINE.
        return loggedUser;                                         //     Finally, the logged user returned.
    }

    public User getUserByUsername(String username) {              // <-- returns User-Object with with corresponding username
        return this.userRepository.findByUsername(username);
    }



    public User getUserById(long id) {                       // <-- returns user found through its Id.
        User userById = userRepository.findById(id);
        return userById;
    }

    public Boolean userEqualsNull(User user){                                             // <-- Used in both cases, when you want to get a user and when you want
                                                                                          //     and when you want to edit a user.
        if(user==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return false;
    }


    public User edit(User userToEdit) {                                            // <--  This method gets called in 'public UserGetDTO editUser(@RequestBody UserEditDTO editUser)'.
        //System.out.println(userToEdit.getId());                                  //      If the user is found, the the username and birth can be edited. Finally the
        User userById = userRepository.getOne(userToEdit.getId());                 //      edited user is returned.


        if (!userEqualsNull(userToEdit)){
            if(userToEdit.getUsername() != null) {
                userById.setUsername(userToEdit.getUsername());
            }
            if(userToEdit.getBirth() != null) {
                userById.setBirth(userToEdit.getBirth());
            }
            return userById;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");       //status code 404
    }



    public User logout(User userToLogOut){
        User userByToken = userRepository.findByToken(userToLogOut.getToken());      // <--  This method gets called in 'public UserGetDTO logout(@RequestBody UserTokenDTO tokenDTO)'.
                                                                                     //      When the user logs out the status is set to OFFLINE and the token is set to null
        userByToken.setStatus(UserStatus.OFFLINE);                                   //      in the database.
        userByToken.setToken(null);

        return userByToken;
    } ////////
}

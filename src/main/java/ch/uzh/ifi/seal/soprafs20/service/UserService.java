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

    public List<User> getUsers() {                   //-
        return this.userRepository.findAll();
    }

    public User createUser(User newUser) {
        //newUser.setToken(UUID.randomUUID().toString());  // <------------------------------- redundant
        //System.out.println("Should be the token r: " + newUser.getToken());
        newUser.setStatus(UserStatus.OFFLINE);
        LocalDate currentDate = LocalDate.now();                             //LocalDate imported!!
        newUser.setDate(currentDate);
        //System.out.println("Current date:" + currentDate);
        newUser.setBirth("00-00-0000");

        checkIfUserNameExists(newUser);

        // saves the given entity but data is only persisted in the database once flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }


    private void checkIfUserNameExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());   //findByUsername(..) is a method for interacting with the database

        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists!");           //Throws status code 409
        }
    }






    public Boolean checkUsername(User userToCheck) {
        return userRepository.existsUserByUsername(userToCheck.getUsername());     //-
    }



    public Boolean existsUserByUsername (String userName){                        //-
        return this.userRepository.existsUserByUsername(userName);
    }



    public Boolean acceptLogin(String username, String password) {
        return userRepository.findByUsername(username).getPassword().equals(password);       // findByUsername(..) returns User-Object with corresponding username
    }

    public Boolean isAlreadyLoggedIn(String username){
        return this.userRepository.findByUsername(username).getStatus().equals(UserStatus.ONLINE);

    }

    public User login(User user) {
        User loggedUser = getUserByUsername(user.getUsername());
        loggedUser.setToken(UUID.randomUUID().toString());           //random Token generated and given to user ( = saved in DB)
        // System.out.println("Should be the token l:" + loggedUser.getToken());                <-   Testing Token
        loggedUser.setStatus(UserStatus.ONLINE);
        return loggedUser;
    }

    public User getUserByUsername(String username) {              //returns User-Object with with corresponding username
        return this.userRepository.findByUsername(username);
    }



    public User getUserById(long id) {                       //-
        User userById = userRepository.findById(id);
        return userById;
    }

    public Boolean userEqualsNull(User user){

        if(user==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return false;
    }


    public User edit(User userToEdit) {
        //System.out.println(userToEdit.getId());
        User userById = userRepository.getOne(userToEdit.getId());


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
        User userByToken = userRepository.findByToken(userToLogOut.getToken());      //*

        userByToken.setStatus(UserStatus.OFFLINE);
        userByToken.setToken(null);

        return userByToken;
    }
}

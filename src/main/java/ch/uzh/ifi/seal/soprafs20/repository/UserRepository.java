package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {         //Methods that are used to interact and work with DB have (?) to be declared in the Repository Interface
	User findByPassword(String password);
	User findByUsername(String username);
	User findByToken(String token);
    User findById(long id);
    Boolean existsUserByUsername(String username);         //example of customized method

}

package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.User.UserEditDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.User.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.User.UserPostDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation works.
 */
public class DTOMapperTest {
    @Test
    public void testCreateUser_fromUserPostDTO_toUser_success() {                      //--------> Conversion from UserPostDTO to User is tested
        // create UserPostDTO
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("name");
        userPostDTO.setUsername("username");

        // MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // check content
        assertEquals(userPostDTO.getPassword(), user.getPassword());
        assertEquals(userPostDTO.getUsername(), user.getUsername());
    }

    @Test
    public void testConversionFromUserToUserGetDTO_success() {                         //--------> Conversion from User to UserGetDTO is tested
        // create User
        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setToken("1");

        // MAP -> Create UserGetDTO
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        // check content
        assertEquals(user.getId(), userGetDTO.getId());
        assertEquals(user.getPassword(), userGetDTO.getPassword());
        assertEquals(user.getUsername(), userGetDTO.getUsername());
        assertEquals(user.getStatus(), userGetDTO.getStatus());
    }

    @Test
    public void testConversionFromUserEditDTO_toUser_success() {                       //--------> Conversion from UserEditDTO to User is tested
        // create UserPostDTO
        UserEditDTO userEditDTO = new UserEditDTO();
        userEditDTO.setBirth("18. 01. 1999");
        userEditDTO.setUsername("username");
        userEditDTO.setId(1L);

        // MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserEditDTOtoEntity(userEditDTO);

        // check content
        assertEquals(userEditDTO.getBirth(), user.getBirth());
        assertEquals(userEditDTO.getUsername(), user.getUsername());
        assertEquals(userEditDTO.getId(), user.getId());
    }


}

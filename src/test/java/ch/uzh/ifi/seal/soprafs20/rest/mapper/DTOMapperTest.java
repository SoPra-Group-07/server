package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.Clue;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.gameround.GameRoundClueDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.user.UserEditDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.user.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.user.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.user.UserTokenDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation works.
 */
class DTOMapperTest {
    @Test
    void testCreateUser_fromUserPostDTO_toUser_success() {                      //--------> Conversion from UserPostDTO to User is tested
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("name");
        userPostDTO.setUsername("username");

        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        assertEquals(userPostDTO.getPassword(), user.getPassword());
        assertEquals(userPostDTO.getUsername(), user.getUsername());
    }

    @Test
    void testConversionFromUserToUserGetDTO_success() {                         //--------> Conversion from User to UserGetDTO is tested
        User user = new User();
        user.setPassword("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setToken("1");

        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        assertEquals(user.getUserId(), userGetDTO.getId());
        assertEquals(user.getPassword(), userGetDTO.getPassword());
        assertEquals(user.getUsername(), userGetDTO.getUsername());
        assertEquals(user.getStatus(), userGetDTO.getStatus());
    }

    @Test
    void testConversionFromUserEditDTO_toUser_success() {                       //--------> Conversion from UserEditDTO to User is tested
        UserEditDTO userEditDTO = new UserEditDTO();
        userEditDTO.setDateOfBirth("18. 01. 1999");

        userEditDTO.setId(1L);

        User user = DTOMapper.INSTANCE.convertUserEditDTOtoEntity(userEditDTO);

        assertEquals(userEditDTO.getDateOfBirth(), user.getDateOfBirth());

        assertEquals(userEditDTO.getId(), user.getUserId());
    }

    @Test
    void testConversionFrom_GameRoundClueDTO_to_Clue(){

        GameRoundClueDTO gameRoundClueDTO = new GameRoundClueDTO();
        gameRoundClueDTO.setClue("myClue");
        gameRoundClueDTO.setGameRoundId(1L);
        gameRoundClueDTO.setPlayerId(2L);

        Clue clue = DTOMapper.INSTANCE.convertGameRoundClueDTOtoEntity(gameRoundClueDTO);
        assertEquals(clue.getGameRoundId(), gameRoundClueDTO.getGameRoundId());
        assertEquals(clue.getWord(), gameRoundClueDTO.getClue());
        assertEquals(clue.getPlayerId(), gameRoundClueDTO.getPlayerId());

    }
    @Test
    void testConversionFrom_userTokenDTO_to_UserGetDTO(){
        UserTokenDTO userTokenDTO = new UserTokenDTO();
        userTokenDTO.setToken("myToken123444");

        User user = DTOMapper.INSTANCE.convertUserTokenDTOtoEntity(userTokenDTO);

        assertEquals(user.getToken(), userTokenDTO.getToken());

    }

}

package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Game.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Leaderboard.LeaderboardDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.User.UserEditDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.User.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.User.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.User.UserTokenDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g., UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    // POST
    @Mapping(source = "password", target = "password")
    @Mapping(source = "username", target = "username")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    // GET
    @Mapping(source = "id", target = "id")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "date", target = "date")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "birth", target = "birth")
    @Mapping(source = "highScore", target = "highScore")
    UserGetDTO convertEntityToUserGetDTO(User user);

    //Token
    @Mapping(source = "token", target = "token")
    User convertUserTokenDTOtoEntity(UserTokenDTO userTokenDTO);

    //Edit
    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "birth", target = "birth")
    User convertUserEditDTOtoEntity(UserEditDTO userEditDTO);

    //LEADERBOARD!!!
    @Mapping(source = "username", target = "username")
    @Mapping(source = "highScore", target = "highScore")
    LeaderboardDTO convertEntityToLeaderboardDTO(User user);

    //GAME!!!
    @Mapping(source = "gameId", target = "gameId")
    @Mapping(source = "gameName", target = "gameName")
    @Mapping(source = "numberOfPlayers", target = "numberOfPlayers")
    GameGetOpenDTO convertEntityToGameGetOpenDTO(Game game);

    @Mapping(source = "hasBot", target = "hasBot")
    @Mapping(source = "gameName", target = "gameName")
    @Mapping(source = "adminPlayerId", target = "adminPlayerId")
    Game convertGamePostDTOToEntity(GamePostDTO gamePostDTO);


    @Mapping(source = "gameId", target = "gameId")
    @Mapping(source = "userId", target = "adminPlayerId")
    Game convertGamePutDTOToEntity(GamePutDTO gamePutDTO);

    @Mapping(source = "hasBot", target = "hasBot")
    @Mapping(source = "gameName", target = "gameName")
    @Mapping(source = "adminPlayerId", target = "adminPlayerId")
    @Mapping(source = "gameId", target = "gameId")
    @Mapping(source = "gameStatus", target = "gameStatus")
    @Mapping(source = "players", target = "players")
    @Mapping(source = "numberOfPlayers", target = "numberOfPlayers")
    @Mapping(source = "actualGameRoundIndex", target = "actualGameRoundIndex")
    @Mapping(source = "cardIds", target = "cardIds")
    GameDTO convertEntityToGameDTO(Game game);


    @Mapping(source = "gameId", target = "gameId")
    @Mapping(source = "gameName", target = "gameName")
    @Mapping(source = "numberOfPlayers", target = "numberOfPlayers")
    @Mapping(source = "players", target = "players")
    LobbyDTO convertEntityToLobbyDTO(Game game);

    @Mapping(source = "gameRoundId", target = "gameRoundId")
    @Mapping(source = "gameId", target = "gameId")
    @Mapping(source = "guessingPlayerId", target = "guessingPlayerId")
    @Mapping(source = "mysteryWord", target = "mysteryWord")
    @Mapping(source = "card", target = "card")
    GameRoundDTO convertEntityToGameRoundDTO(GameRound gameRound);

    @Mapping(source = "gameId", target = "gameId")
    Game convertGameStartDTOToEntity(GameStartPutDTO gameStartPutDTO);
}

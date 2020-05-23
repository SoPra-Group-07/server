package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.game.*;
import ch.uzh.ifi.seal.soprafs20.rest.dto.gameround.GameRoundClueDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.gameround.GameRoundDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.gameround.GameRoundGuessDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.gameround.GameRoundPutDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.leaderboard.LeaderboardDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.playerstatistic.PlayerStatisticDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.user.UserEditDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.user.UserGetDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.user.UserPostDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.user.UserTokenDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g., UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for creating information (POST).
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    // POST
    @Mapping(source = "password", target = "password")
    @Mapping(source = "username", target = "username")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    // GET
    @Mapping(source = "userId", target = "id")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "creationDate", target = "date")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "dateOfBirth", target = "birth")
    @Mapping(source = "highScore", target = "highScore")
    UserGetDTO convertEntityToUserGetDTO(User user);

    //Token
    @Mapping(source = "token", target = "token")
    User convertUserTokenDTOtoEntity(UserTokenDTO userTokenDTO);

    //Edit
    @Mapping(source = "id", target = "userId")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth")
    User convertUserEditDTOtoEntity(UserEditDTO userEditDTO);

    //LEADERBOARD
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
    @Mapping(source = "isDemoGame", target = "isDemoGame")
    Game convertGamePostDTOToEntity(GamePostDTO gamePostDTO);


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
    @Mapping(source = "adminPlayerId", target = "adminPlayerId")
    @Mapping(source = "gameStatus", target = "gameStatus")
    LobbyDTO convertEntityToLobbyDTO(Game game);

    @Mapping(source = "gameRoundId", target = "gameRoundId")
    @Mapping(source = "gameId", target = "gameId")
    @Mapping(source = "guessingPlayerId", target = "guessingPlayerId")
    @Mapping(source = "mysteryWord", target = "mysteryWord")
    @Mapping(source = "card", target = "card")
    @Mapping(source = "submissions", target = "submissions")
    @Mapping(source = "guess", target = "guess")
    @Mapping(source = "totalGameRounds", target = "totalGameRounds")
    @Mapping(source = "actualGameRound", target = "actualGameRound")
    GameRoundDTO convertEntityToGameRoundDTO(GameRound gameRound);

    @Mapping(source = "gameId", target = "gameId")
    Game convertGameStartDTOToEntity(GameStartPutDTO gameStartPutDTO);

    @Mapping(source = "gameRoundId", target = "gameRoundId")
    @Mapping(source = "wordNumber", target = "wordNumber")
    GameRound convertGameRoundPutDTOtoEntity(GameRoundPutDTO gameRoundPutDTO);

    @Mapping(source = "gameRoundId", target = "gameRoundId")
    @Mapping(source = "playerId", target = "playerId")
    @Mapping(source = "clue", target = "word")
    Clue convertGameRoundClueDTOtoEntity(GameRoundClueDTO gameRoundClueDTO);

    @Mapping(source = "gameRoundId", target = "gameRoundId")
    @Mapping(source = "playerId", target = "playerId")
    @Mapping(source = "guess", target = "word")
    Guess convertGameRoundGuessDTOtoEntity(GameRoundGuessDTO gameRoundGuessTO);

    @Mapping(source = "playerStatisticId", target = "playerStatisticId")
    @Mapping(source = "gameRoundId", target = "gameRoundId")
    @Mapping(source = "playerId", target = "playerId")
    @Mapping(source = "playerName", target = "playerName")
    @Mapping(source = "totalPoints", target = "totalPoints")
    @Mapping(source = "wasGuessingPlayer", target = "wasGuessingPlayer")
    @Mapping(source = "duration", target = "duration")
    @Mapping(source = "durationPoints", target = "durationPoints")
    @Mapping(source = "duplicateClue", target = "duplicateClue")
    @Mapping(source = "duplicateCluePoints", target = "duplicateCluePoints")
    @Mapping(source = "didNotClue", target = "didNotClue")
    @Mapping(source = "notCluePoints", target = "notCluePoints")
    @Mapping(source = "rightGuess", target = "rightGuess")
    @Mapping(source = "rightGuessPoints", target = "rightGuessPoints")
    @Mapping(source = "guess", target = "guess")
    @Mapping(source = "clue", target = "clue")
    PlayerStatisticDTO convertEntityToPlayerStatisticDTO(PlayerStatistic playerStatistic);

    @Mapping(source = "playerId", target = "playerId")
    @Mapping(source = "playerName", target = "playerName")
    @Mapping(source = "currentScore", target = "totalPoints")
    @Mapping(source = "numberOfCorrectGuesses", target = "numberOfCorrectGuesses")
    @Mapping(source = "numberOfDuplicateClues", target = "numberOfDuplicateClues")
    GameStatisticDTO convertEntityToGameStatisticDTO(Player player);
}

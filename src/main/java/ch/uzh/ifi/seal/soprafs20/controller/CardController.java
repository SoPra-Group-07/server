package ch.uzh.ifi.seal.soprafs20.controller;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.GameRound;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Game.GameRoundDTO;
import ch.uzh.ifi.seal.soprafs20.rest.dto.Game.GameStartPutDTO;
import ch.uzh.ifi.seal.soprafs20.rest.mapper.DTOMapper;
import ch.uzh.ifi.seal.soprafs20.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService){ this.cardService = cardService; }

    @PutMapping("cards")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void fillCardTable() {
        cardService.fillCardTable();
    }
}

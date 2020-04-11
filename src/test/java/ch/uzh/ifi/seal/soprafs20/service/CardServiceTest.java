package ch.uzh.ifi.seal.soprafs20.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;

public class CardServiceTest {

    @InjectMocks
    private CardService cardService;

    @Test
    public void testLoadCard(){
        MockitoAnnotations.initMocks(this);
        //assertEquals((cardService.loadCards().get(1)), "hALLO");
    }
}

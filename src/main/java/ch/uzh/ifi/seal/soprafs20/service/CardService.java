package ch.uzh.ifi.seal.soprafs20.service;

import ch.uzh.ifi.seal.soprafs20.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs20.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs20.entity.*;
import ch.uzh.ifi.seal.soprafs20.exceptions.SopraServiceException;
import ch.uzh.ifi.seal.soprafs20.repository.CardRepository;
import ch.uzh.ifi.seal.soprafs20.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs20.repository.PlayerRepository;
import ch.uzh.ifi.seal.soprafs20.repository.UserRepository;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class CardService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final CardRepository cardRepository;

    @Autowired
    public CardService(@Qualifier("cardRepository") CardRepository cardRepository) {

        this.cardRepository = cardRepository;

    }

    public void fillCardTable(){
        try {
            FileReader fr = new FileReader(System.getProperty("user.dir")+"/src/main/java/ch/uzh/ifi/seal/soprafs20/cards-EN.txt");   //reads the file
            BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream
            String word1;
            while((word1 = br.readLine())!=null) {
                Card card = new Card();
                card.setWord1(word1);
                card.setWord2(br.readLine());
                card.setWord3(br.readLine());
                card.setWord4(br.readLine());
                card.setWord5(br.readLine());
                br.readLine();
                cardRepository.save(card);
            }
            fr.close();    //closes the stream and release the resources
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
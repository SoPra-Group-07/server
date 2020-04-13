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

import java.io.*;
import java.time.LocalDate;
import java.util.*;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */

public class CardService {

    public static void main(String[] args){
            try {
                Reader cardsEN = new File(System.getProperty("user.dir")+"/src/main/java/ch/uzh/ifi/seal/soprafs20/cards-EN.txt");
                File dataSQL = new File(System.getProperty("user.dir")+"/src/main/data.sql");
                BufferedReader reader = new BufferedReader(cardsEN);
                FileWriter fw = new FileWriter(dataSQL);
                try {

                    String word = reader.readLine();
                    while (word!=null){

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    //close resources
                    try {
                        fw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                fr.close();    //closes the stream and release the resources
            } catch (IOException e){
                e.printStackTrace();
            }

}
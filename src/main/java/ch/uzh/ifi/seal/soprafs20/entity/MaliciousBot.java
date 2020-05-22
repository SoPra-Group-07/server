package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.rest.dto.gameround.AptReq;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.Entity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 *Internal MaliciousBot Representation
 *This class composes the internal representation of the maliciousBot and defines how the maliciousBot is stored in the database.
 */
@Entity
public class MaliciousBot extends Player implements Serializable {

    private static final long serialVersionUID = 1L;

    public MaliciousBot() {/* empty constructor*/ }

    @Override
    public String giveClue(String word) throws IOException {
        String wordLower = word.toLowerCase();
        String ant = getWordByUrl(getAntonymUrl(wordLower), wordLower);
        if (ant.equalsIgnoreCase(word) || ant.contains(word) || word.contains(ant)) {
            String adj = getWordByUrl(getAdjectiveUrl(wordLower), wordLower);
            String adjAnt = getWordByUrl(getAntonymUrl(adj), adj);
            if (adjAnt.equalsIgnoreCase(word) || adjAnt.contains(wordLower) || wordLower.contains(adjAnt)) {
                return word;
            }
            else {
                return adjAnt;
            }
        }
        else{ return ant;}
    }

    private static String getAdjectiveUrl(String word) {

        return "https://api.datamuse.com/words?rel_jjb=" + word;
    }


    private static String getAntonymUrl(String word) {

        return "https://api.datamuse.com/words?rel_ant=" + word;
    }



    private static String getWordByUrl(String url, String word) throws IOException {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        // ordering the response
        StringBuilder response;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        ObjectMapper mapper = new ObjectMapper();

        try {
            // converting JSON array to ArrayList of words
            ArrayList<AptReq> words = mapper.readValue(
                    response.toString(),
                    mapper.getTypeFactory().constructCollectionType(ArrayList.class, AptReq.class));

            if (!words.isEmpty()) {
                return words.get(0).getWord().replaceAll("\\s","").toLowerCase();
            }
            else {

                return word;
            }
        }
        catch (IOException e) { e.getMessage();}
        return "notWorking";
    }




}

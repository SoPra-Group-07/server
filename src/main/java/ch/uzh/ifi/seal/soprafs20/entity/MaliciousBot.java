package ch.uzh.ifi.seal.soprafs20.entity;

import ch.uzh.ifi.seal.soprafs20.rest.dto.GameRound.aptReq;
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
        String ant = get_word_by_url(get_antonym_url(word.toLowerCase()), word.toLowerCase());
        if (ant.equals(word.toLowerCase())){
        String adj = get_word_by_url(get_adjective_url(word.toLowerCase()), word.toLowerCase());
        return get_word_by_url(get_antonym_url(adj), adj);
        }
        return ant;
    }

    public static String get_adjective_url(String word) throws IOException {

        String url = "https://api.datamuse.com/words?rel_jjb=" + word;

        return url;
    }


    public static String get_antonym_url(String word) throws IOException {

        return "https://api.datamuse.com/words?rel_ant=" + word;
    }



    public static String get_word_by_url(String url, String word) throws IOException {

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
            ArrayList<aptReq> words = mapper.readValue(
                    response.toString(),
                    mapper.getTypeFactory().constructCollectionType(ArrayList.class, aptReq.class));

            if (!words.isEmpty()) {
                return words.get(0).getWord().replaceAll("\\s","");
            }
            else {

                return word;
            }
        }
        catch (IOException e) { e.getMessage();}
        return "notWorking";
    }




}

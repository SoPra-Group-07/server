package ch.uzh.ifi.seal.soprafs20.entity;
import ch.uzh.ifi.seal.soprafs20.rest.dto.GameRound.aptReq;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.Entity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 *Internal FriendlyBot Representation
 *This class composes the internal representation of the friendlyBot and defines how the friendlyBot is stored in the database.
 */
@Entity
public class FriendlyBot extends Player {

    public FriendlyBot() {/* empty constructor */}

    @Override
    public String giveClue(String word) throws IOException {
        String clue = get_word_by_url(get_synonyme_url(word.toLowerCase()), word.toLowerCase());
        if (clue.toLowerCase().equals(word.toLowerCase())){
            return get_word_by_url(get_trigger_url(word.toLowerCase()), word.toLowerCase());
        }
        return clue;
    }


    public static String get_synonyme_url(String word) throws IOException {

        return "https://api.datamuse.com/words?rel_syn=" + word;}

    public static String get_trigger_url(String word) throws IOException {

        return "https://api.datamuse.com/words?rel_trg=" + word;
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


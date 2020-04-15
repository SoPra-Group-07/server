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
import java.util.List;

@Entity
public class FriendlyBot extends Player {

    public FriendlyBot() {/* empty constructor */}

    @Override
    public String giveClue(String word) throws IOException {

        return getSynonyme(word);
    }

    public static String getSynonyme(String word) throws IOException {

        String url = "https://api.datamuse.com/words?rel_syn=" + word;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = con.getResponseCode();

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

            if (words.size() > 0) {
                return words.get(0).getWord();
            }
            else {
                return "surprise";
            }
        }
        catch (IOException e) { e.getMessage();}
        return "notWorking";
    }

}

package stockmark.stockmark.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import stockmark.stockmark.model.Exceptions.*;

//import stockmark.stockmark.model.Exceptions.*;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
//import java.util.UUID;

public class PrivateGameManager {
    private static String privateGamesFile = "./src/main/resources/privateGames.json";
    private static HashMap<String, PrivateGame> games;

    public static void Initialize() {
        if (games != null)
            throw new RuntimeException("PrivateGameManager should not be initialized more than once!");
        try {
            File fileObj = new File(privateGamesFile);
            PrivateGame[] privateGamesArray = new ObjectMapper().readValue(fileObj, PrivateGame[].class);

            games = new HashMap<>();
            for (PrivateGame game : privateGamesArray) {
                games.put(game.getGameName(), game);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    public static void createGame(PrivateGame game) throws GameExistsException{

        if (games.containsKey(game.getGameName())) {
            throw new GameExistsException();
        } 
        
        games.put(game.getGameName(), game);
        syncToDisk();
    }

    public static PrivateGame getGame(String gameName){
        return games.get(gameName);
    }

    public static Collection<PrivateGame> getPrivateGames() {
        return games.values();
    }

    // persist data to disk
    public static void syncToDisk() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(
                    new File(privateGamesFile),
                    games.values());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving games.");
        }
    }
}
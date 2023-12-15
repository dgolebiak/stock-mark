package stockmark.stockmark.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import stockmark.stockmark.model.Exceptions.PlayerAlreadyInGameException;
import java.util.ArrayList;

//Class for Private Games.

@JsonIgnoreProperties(ignoreUnknown = true)
public class PrivateGame {
    //private String owner;
    private String gameName;
    private double gameBudget;
    private long startDate;
    private long endDate;
    private ArrayList<Player> players;


    // required by jackson
    PrivateGame() {}

    public PrivateGame(String gameName, Account owner, double budget, long startDate, long endDate) {
       // this.owner = owner.getName();
        this.gameName = gameName;
        this.gameBudget = budget;
        this.startDate = startDate;
        this.endDate = endDate;
        this.players = new ArrayList<Player>();
    }

    //Create a player with the users name and add to the game. Throws exception if a player with that name already exists.
    public void addPlayer(String playerName) throws PlayerAlreadyInGameException{
        if(doesPlayerExist(playerName)){
            throw new PlayerAlreadyInGameException();
        }
        this.players.add(new Player(playerName, this.gameBudget));

        PrivateGameManager.syncToDisk();
    }

    //Remove a player with given name from the game.
    public void removePlayer(String playerName){
        for (Player temp : this.players){
            if(temp.getName().equals(playerName)){
                this.players.remove(temp);
                break;
            }
        }

        PrivateGameManager.syncToDisk();
        
    }

    //Checks if a player with the given name is in the game. Returns boolean.
    public boolean doesPlayerExist(String playerName) {
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                return true;  // Player with the specified name found
            }
        }
        return false;  // Player with the specified name not found
    }

    //Returns list of all players in game. Returns ArrayList<Player>
    public ArrayList<Player> getPlayers(){
        return this.players;
    }

    //Returns player in game with given name. Returns Player.
    public Player getPlayer(String name){
        for (Player player : this.players){
            if(player.getName().equals(name)){
                return player;
            }
        }
        return null;
    }

    //Returns game name. Returns String.
    public String getGameName(){
        return this.gameName;
    }

    //Returns the starting budget of the game. Returns double.
    public double getGameBudget(){
        return this.gameBudget;
    }

    //Returns the starting date of the game as a unixtimestamp. Returns long.
    public long getStartDate(){
        return this.startDate;
    }

    //Returns the end date of the game as a unixtimestamp. Returns long.
    public long getEndDate(){
        return this.endDate;
    }

}
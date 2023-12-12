package stockmark.stockmark.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import stockmark.stockmark.model.Exceptions.PlayerAlreadyInGameException;
import stockmark.stockmark.model.Types.Transaction;

import java.util.ArrayList;
import java.util.HashMap;


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

    public void addPlayer(String playerName) throws PlayerAlreadyInGameException{
        if(doesPlayerExist(playerName)){
            throw new PlayerAlreadyInGameException();
        }
        this.players.add(new Player(playerName, this.gameBudget));

        PrivateGameManager.syncToDisk();
    }

    public void removePlayer(String playerName){
        for (Player temp : this.players){
            if(temp.getName() == playerName){
                this.players.remove(temp);
                break;
            }
        }
        
    }

    public boolean doesPlayerExist(String playerName) {
        for (Player player : players) {
            if (player.getName().equals(playerName)) {
                return true;  // Player with the specified name found
            }
        }
        return false;  // Player with the specified name not found
    }

    public ArrayList<Player> getPlayers(){
        return this.players;
    }

    public Player getPlayer(String name){
        for (Player player : this.players){
            if(player.getName().equals(name)){
                return player;
            }
        }
        return null;
    }

    public String getGameName(){
        return this.gameName;
    }

    public double getGameBudget(){
        return this.gameBudget;
    }

    public long getStartDate(){
        return this.startDate;
    }

    public long getEndDate(){
        return this.endDate;
    }

}
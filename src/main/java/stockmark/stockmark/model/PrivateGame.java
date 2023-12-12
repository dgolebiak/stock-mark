package stockmark.stockmark.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import stockmark.stockmark.model.Types.Transaction;

import java.util.ArrayList;
import java.util.HashMap;


@JsonIgnoreProperties(ignoreUnknown = true)
public class PrivateGame {
    //private String owner;
    private String gameName;
    private double budget;
    private long startDate;
    private long endDate;
    private ArrayList<String> players;


    // required by jackson
    PrivateGame() {}

    public PrivateGame(String gameName, Account owner, double budget, long startDate, long endDate) {
       // this.owner = owner.getName();
        this.gameName = gameName;
        this.budget = budget;
        this.startDate = startDate;
        this.endDate = endDate;
        this.players = new ArrayList<String>();
    }

    public void addPlayer(Account player){

        this.players.add(player.getName());

        PrivateGameManager.syncToDisk();
    }

    public ArrayList<String> getPlayers(){
        return this.players;
    }

    public String getGameName(){
        return this.gameName;
    }

    public double getGameBudget(){
        return this.budget;
    }

    public long getStartDate(){
        return this.startDate;
    }

    public long getEndDate(){
        return this.endDate;
    }

}
package stockmark.stockmark.model;

import java.util.HashMap;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import stockmark.stockmark.model.Exceptions.BalanceTooLowException;
import stockmark.stockmark.model.Exceptions.NonExistentTickerException;
import stockmark.stockmark.model.Exceptions.NotEnoughAssetsException;
import stockmark.stockmark.model.Types.Share;

//Class for players in Private Games.

@JsonIgnoreProperties(ignoreUnknown = true)
public class Player {
    private String name;
    private double balance;
    private double portfolioWorth;
    private HashMap<String, Share> assets;


    // required by jackson
    Player() {}

    public Player(String name, double budget) {
        this.name = name;
        this.balance = budget;
        this.portfolioWorth = 0;
        this.assets = new HashMap<String, Share>();
    }

    //Returns player name. Returns String.
    public String getName(){
        return this.name;
    }

    //Returns balance. Returns double.
    public double getBalance(){
        return this.balance;
    }

    //Returns assets/owned stocks. Returns HashMap<String, Share>
    public HashMap<String, Share> getAssets(){
        return this.assets;
    }

    //Buys a stock in a private game. Throws exception if not enough balannce or invalid stock symbol.
    public void buyAsset(String ticker, int buyAmount) throws BalanceTooLowException, NonExistentTickerException {
        double stockPrice = Market.getInstance().getPrice(ticker);
        double assumedCost = stockPrice * buyAmount;
        if (assumedCost > balance)
            throw new BalanceTooLowException();

        Share oldShare = assets.get(ticker);
        double averagePrice;
        int newAmount;

        // update existing share or else create new
        if (oldShare != null) {
            newAmount = oldShare.amount() + buyAmount;
            averagePrice = ((oldShare.buyPrice() * oldShare.amount()) + (assumedCost))
                    / (oldShare.amount() + buyAmount);
        } else {
            newAmount = buyAmount;
            averagePrice = stockPrice;
        }

        Share newShare = new Share(newAmount, averagePrice);
        assets.put(ticker, newShare);

        this.balance -= assumedCost;

        PrivateGameManager.syncToDisk();
    }

    //Sells a stock in a private game. Throws exception if not enough stocks are owned or invalid stock symbol.
    public void sellAsset(String ticker, int sellAmount) throws NotEnoughAssetsException, NonExistentTickerException {
        double stockPrice = Market.getInstance().getPrice(ticker);
        Share share = assets.get(ticker);

        // calm down fella, you ain't that rich
        if (sellAmount > share.amount())
            throw new NotEnoughAssetsException();

        int leftoverAmount = share.amount() - sellAmount;

        // if some shares still left
        if (leftoverAmount > 0) {
            Share newShare = new Share(leftoverAmount, share.buyPrice());
            assets.put(ticker, newShare);
        } else
            assets.remove(ticker);

        // profit
        balance += stockPrice * sellAmount;
        
        PrivateGameManager.syncToDisk();
    }
}
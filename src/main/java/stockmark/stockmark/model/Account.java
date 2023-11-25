package stockmark.stockmark.model;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import stockmark.stockmark.model.Exceptions.*;


// Single account for one user
public class Account {
    private String name;
    private String email;
    private String password;
    private double balance;
    private HashMap<Boolean, List<Shares>> history; // Boolean is true for buy and false for sell
    private HashMap<String, Shares> assets;
    private Market market = Market.getInstance();

    // required by jackson
    Account(){}

    public Account(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.history = new HashMap<>();
        this.assets = new HashMap<>();
    }

    public void buyAssets(String stockName, int amount) {
        try {
            double stockPrice = market.getPrice(stockName);
            if (stockPrice * amount <= balance) {
                String timestamp = LocalDate.now().toString();
                Shares oldShare = assets.get(stockName);
                double averagePrice;
                int newAmount;
                
                if (oldShare != null) {
                    newAmount = oldShare.amount() + amount;
                    averagePrice = ((oldShare.buyPrice() * oldShare.amount()) + (stockPrice * amount)) / (oldShare.amount() + amount);
                } else {
                    newAmount = amount;
                    averagePrice = stockPrice;
                }

                Shares newShare = new Shares(stockName, newAmount, averagePrice, timestamp);
                assets.replace(stockName, newShare);  

                balance -= stockPrice * amount;
                history.get(true).add(new Shares(stockName, amount, stockPrice, timestamp));
            } else {
                throw new BalanceTooLowException();
            }
            AccountManager.saveAccountsJson();
        } catch (NonExistentTickerException e) {
            e.printStackTrace();
            System.out.println("Ticker doesn't exist.");
        } catch (BalanceTooLowException e) {
            e.printStackTrace();
            System.out.println("Accounts balance is too low");
        }
    }

    public void sellAssets(String stockName, int amount) {
        try {
            double stockPrice = market.getPrice(stockName);
            String timestamp = LocalDate.now().toString();
            Shares oldShare = assets.get(stockName);
            if (amount < oldShare.amount()) {
                int newAmount = oldShare.amount() - amount;

                Shares newShare = new Shares(stockName, newAmount, oldShare.buyPrice(), timestamp);
                assets.replace(stockName, newShare);

                balance += stockPrice*amount;
                history.get(false).add(new Shares(stockName, amount, stockPrice, timestamp));
            } else if (amount == oldShare.amount()) {
                assets.remove(stockName);

                balance += stockPrice*amount;
                history.get(false).add(new Shares(stockName, amount, stockPrice, timestamp));
            } else {
                throw new NotEnoughAssetsException();
            }
            AccountManager.saveAccountsJson();
        } catch (NonExistentTickerException e) {
            e.printStackTrace();
            System.out.println("Ticker doesn't exist.");
        } catch (NotEnoughAssetsException e) {
            e.printStackTrace();
            System.out.println("Not enough assets to sell");
        }
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public Map<String, Shares> getAssets() {
        return assets;
    }

    public Map<Boolean, List<Shares>> getHistory() {
        return history;
    }
}
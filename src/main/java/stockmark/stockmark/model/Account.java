package stockmark.stockmark.model;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import stockmark.stockmark.model.Exceptions.*;
import stockmark.stockmark.model.Types.ChangeOverTime;

// NOTES: Problems with:
// private HashMap<Boolean, List<Shares>> history;
// We need a history from start to finish, sort of like transactions in the order that it happened
// Storing it like this will require further processing to get this into a presentable state to a user.

// Single account for one user
public class Account {
    private String name;
    private String email;
    private String password;
    private double balance;
    private double deposited;
    private HashMap<String, Share> assets;
    private ArrayList<Transaction> history;

    // required by jackson
    Account() {
    }

    public Account(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.assets = new HashMap<String, Share>();
        this.history = new ArrayList<Transaction>();
    }

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

        balance -= assumedCost;
        String timestamp = LocalDate.now().toString();
        history.add(new Transaction("buy", ticker, buyAmount, stockPrice, timestamp));

        AccountManager.syncToDisk();
    }

    public void sellAsset(String ticker, int sellAmount) throws NotEnoughAssetsException, NonExistentTickerException {
        double stockPrice = Market.getInstance().getPrice(ticker);
        String timestamp = LocalDate.now().toString();
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

        // add to history
        history.add(new Transaction("sell", ticker, sellAmount, stockPrice, timestamp));
        AccountManager.syncToDisk();
    }

    public void deposit(double amount) {
        deposited += amount;
        balance += amount;
        AccountManager.syncToDisk();
    }

    public ChangeOverTime calcMostProfitableOverall() {
        String mostProfitableName = null;
        double mostProfit = -1000000;

        double worth = 0;
        double oldWorth = 0;

        for (String ticker : assets.keySet()) {
            try {
                double currentPrice = Market.getInstance().getPrice(ticker);
                Share myShare = assets.get(ticker);
                double myShareWorthToday = (double) myShare.amount() * currentPrice;
                double howMuchIpaidForIt = (double) myShare.amount() * myShare.buyPrice();
                double profit = myShareWorthToday - howMuchIpaidForIt;

                // calc most profitable overall
                if (profit > mostProfit) {
                    mostProfit = profit;
                    mostProfitableName = ticker;
                    worth = myShareWorthToday;
                    oldWorth = howMuchIpaidForIt;
                }
            } catch (NonExistentTickerException e) {
                System.out.println("Non existent ticker, but data came from database so internal error...");
            }
        }

        if (mostProfitableName == null)
            return null;
        return new ChangeOverTime(mostProfitableName, worth, oldWorth);
    }

    public ChangeOverTime calcLeastProfitableOverall() {
        String leastProfitableName = null;
        double leastProfit = 1000000;

        double worth = 0;
        double oldWorth = 0;

        for (String ticker : assets.keySet()) {
            try {
                double currentPrice = Market.getInstance().getPrice(ticker);
                Share myShare = assets.get(ticker);
                double myShareWorthToday = (double) myShare.amount() * currentPrice;
                double howMuchIpaidForIt = (double) myShare.amount() * myShare.buyPrice();
                double profit = myShareWorthToday - howMuchIpaidForIt;

                // calc least profitable overall
                if (profit < leastProfit) {
                    leastProfit = profit;
                    leastProfitableName = ticker;
                    worth = myShareWorthToday;
                    oldWorth = howMuchIpaidForIt;
                }
            } catch (NonExistentTickerException e) {
                System.out.println("Non existent ticker, but data came from database so internal error...");
            }
        }

        if (leastProfitableName == null)
            return null;
        return new ChangeOverTime(leastProfitableName, worth, oldWorth);
    }

    public ChangeOverTime calcMostProfitableToday() {
        String mostProfitableName = null;
        double mostProfit = -1000000;

        double worth = 0;
        double oldWorth = 0;

        for (String ticker : assets.keySet()) {
            try {
                double priceToday = Market.getInstance().getPrice(ticker);
                double pcChangeToday = Market.getInstance().getPercentChangeToday(ticker);
                double priceYesterday = (100.0/(pcChangeToday+100.0) * priceToday);

                Share myShare = assets.get(ticker);
                double myShareWorthToday = (double) myShare.amount() * priceToday;
                double myShareWorthYesterday = (double) myShare.amount() * priceYesterday;
                double profit = myShareWorthToday - myShareWorthYesterday;

                // calc most profitable today
                if (profit > mostProfit) {
                    mostProfit = profit;
                    mostProfitableName = ticker;
                    worth = myShareWorthToday;
                    oldWorth = myShareWorthYesterday;
                }
            } catch (NonExistentTickerException e) {
                System.out.println("Non existent ticker, but data came from database so internal error...");
            }
        }

        if (mostProfitableName == null)
            return null;
        return new ChangeOverTime(mostProfitableName, worth, oldWorth);
    }

    public ChangeOverTime calcLeastProfitableToday() {
        String leastProfitableName = null;
        double leastProfit = 1000000;

        double worth = 0;
        double oldWorth = 0;

        for (String ticker : assets.keySet()) {
            try {
                double priceToday = Market.getInstance().getPrice(ticker);
                double pcChangeToday = Market.getInstance().getPercentChangeToday(ticker);
                double priceYesterday = (100.0/(pcChangeToday+100.0) * priceToday);

                Share myShare = assets.get(ticker);
                double myShareWorthToday = (double) myShare.amount() * priceToday;
                double myShareWorthYesterday = (double) myShare.amount() * priceYesterday;
                double profit = myShareWorthToday - myShareWorthYesterday;

                // calc least profitable today
                if (profit < leastProfit) {
                    leastProfit = profit;
                    leastProfitableName = ticker;
                    worth = myShareWorthToday;
                    oldWorth = myShareWorthYesterday;
                }
            } catch (NonExistentTickerException e) {
                System.out.println("Non existent ticker, but data came from database so internal error...");
            }
        }

        if (leastProfitableName == null)
            return null;
        return new ChangeOverTime(leastProfitableName, worth, oldWorth);
    }

    public ChangeOverTime calcValueChangeOverall() {
        double start = getDeposited();
        double after = getBalance();

        // add assets value to after
        for (String ticker : assets.keySet()) {
            try {
                double currentPrice = Market.getInstance().getPrice(ticker);
                Share myShare = assets.get(ticker);
                double myShareWorthToday = (double) myShare.amount() * currentPrice;
                after += myShareWorthToday;
            } catch (NonExistentTickerException e) {
                System.out.println("Non existent ticker, but data came from database so internal error...");
            }
        }

        return new ChangeOverTime(null, after, start);
    }

    public ChangeOverTime calcValueChangeToday() {
        double yesterdayValue = 0;
        double todayValue = 0;

        // add assets value to value
        for (String ticker : assets.keySet()) {
            try {
                double priceToday = Market.getInstance().getPrice(ticker);
                double pcChangeToday = Market.getInstance().getPercentChangeToday(ticker);
                double priceYesterday = (100.0/(pcChangeToday+100.0) * priceToday);

                Share myShare = assets.get(ticker);
                double myShareWorthToday = (double) myShare.amount() * priceToday;
                todayValue += myShareWorthToday;

                double myShareWorthYesterday = (double) myShare.amount() * priceYesterday;
                yesterdayValue += myShareWorthYesterday;
            } catch (NonExistentTickerException e) {
                System.out.println("Non existent ticker, but data came from database so internal error...");
            }
        }
        return new ChangeOverTime(null, todayValue, yesterdayValue);
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

    public double getDeposited() {
        return deposited;
    }

    public Map<String, Share> getAssets() {
        return assets;
    }

    public List<Transaction> getHistory() {
        return history;
    }
}
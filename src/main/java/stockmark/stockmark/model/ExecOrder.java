package stockmark.stockmark.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import stockmark.stockmark.model.Exceptions.BalanceTooLowException;
import stockmark.stockmark.model.Exceptions.NonExistentTickerException;
import stockmark.stockmark.model.Exceptions.NotEnoughAssetsException;
import stockmark.stockmark.model.Types.Share;
import stockmark.stockmark.model.Types.Transaction;

public class ExecOrder {
    public void buyAsset(double balance, ArrayList<Transaction> history,HashMap<String, Share> assets, String ticker, int buyAmount) throws BalanceTooLowException, NonExistentTickerException {
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
        history.add(0, new Transaction("buy", ticker, buyAmount, stockPrice, timestamp));
    }

    public void sellAsset(double balance, ArrayList<Transaction> history, HashMap<String, Share> assets, String ticker, int sellAmount) throws NotEnoughAssetsException, NonExistentTickerException {
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
        history.add(0, new Transaction("sell", ticker, sellAmount, stockPrice, timestamp));
    }

    public void deposit(double deposited, double balance, double amount) {
        deposited += amount;
        balance += amount;
    }
}

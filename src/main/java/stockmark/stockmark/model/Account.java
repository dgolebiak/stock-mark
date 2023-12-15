package stockmark.stockmark.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import stockmark.stockmark.model.Exceptions.*;
import stockmark.stockmark.model.Types.ChangeOverTime;
import stockmark.stockmark.model.Types.Share;
import stockmark.stockmark.model.Types.Transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {
    private String name;
    private String email;
    private String password;
    private double balance;
    private double deposited;
    private HashMap<String, Share> assets;
    private ArrayList<Transaction> history;
    private ArrayList<String> privateGames;

    // required by jackson so ignore
    Account() {}

    public Account(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.assets = new HashMap<String, Share>();
        this.history = new ArrayList<Transaction>();
        this.privateGames = new ArrayList<String>();
    }

    // compare old & new password
    public boolean isValidPassword(String oldPassword) {
        if (this.password.equals(oldPassword)) {
            return true;
        } else {
            return false;
        }
    }

    // update password
    public void changePassword(String oldPassword, String newPassword, String confNewPassword)
            throws IncorrectPasswordException {
        if (isValidPassword(oldPassword) && newPassword.equals(confNewPassword)) {
            this.password = newPassword;
            AccountManager.syncToDisk();
        } else {
            throw new IncorrectPasswordException();
        }
    }

    // to buy a stock
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
        history.add(0, new Transaction("buy", ticker, buyAmount, stockPrice, timestamp));

        // save
        AccountManager.syncToDisk();
    }

    // to sell a stock
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
        history.add(0, new Transaction("sell", ticker, sellAmount, stockPrice, timestamp));
        AccountManager.syncToDisk();
    }

    // simple add money functionality
    public void deposit(double amount) {
        if (amount > 0) {
            deposited += amount;
            balance += amount;
            AccountManager.syncToDisk();
        }
    }

    public void joinGame(String gameName) throws PlayerAlreadyInGameException {
        for (String game : privateGames) {
            if (game.equals(gameName)) {
                throw new PlayerAlreadyInGameException();
            }
        }
        privateGames.add(gameName);
        AccountManager.syncToDisk();
    }

    public void leaveGame(String gameName) {
        privateGames.remove(gameName);
        AccountManager.syncToDisk();
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

    public ArrayList<String> getPrivateGames() {
        return privateGames;
    }

    public Page<Transaction> getHistoryPage(Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), history.size());

        List<Transaction> pageContent = history.subList(start, end);

        return new PageImpl<>(pageContent, pageable, history.size());
    }

    public String sendExcelHistoryString() {
        return ExcelFileCreator.createExcelString(history);
    }

    public String sendExcelHistoryFile() {
        return ExcelFileCreator.createExcelFile(history);
    }
}

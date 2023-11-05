package stockmark.stockmark.model;

import java.util.ArrayList;
import java.util.List;

// Stock("TSLA", 4)
record Stock(String stockTicker, int amount){}

// Single account for one user
public class Account {

    private String email;
    private String password; // we don't need encryption, store as plain text. fix if TA complains
    private double balance; // amount of liquid money in the account
    private ArrayList<Double> assets;

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public double getBalance() {
        return balance;
    }
    public List<Double> getAssets() {
        return assets;
    }
}
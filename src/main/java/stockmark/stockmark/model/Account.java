package stockmark.stockmark.model;

import java.util.ArrayList;
import java.util.List;

// Stock("TSLA", 4)
record Stock(String stockTicker, int amount){}

// Single account for one user
public class Account {

    private String fname;
    private String lname;
    private String email;
    private String password; // we don't need encryption, store as plain text. fix if TA complains
    private double balance; // amount of liquid money in the account
    private ArrayList<Double> assets;

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
        this.balance = 10000;
    }

    public void setMail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setAssets(ArrayList<Double> assets) {
        this.assets = assets;
    }

    public void setFname(String fname){
        this.fname = fname;
    }

    public void setLname(String lname){
        this.lname = lname;
    }

    public String getFname(){
        return fname;
    }

    public String getLname(){
        return lname;
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
    public List<Double> getAssets() {
        return assets;
    }
}
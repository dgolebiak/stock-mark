package stockmark.stockmark.model;

import java.util.ArrayList;
import java.util.List;

// Single account for one user
public class Account {
    private String name;
    private String email;
    private String password;
    private double balance;
    private ArrayList<Double> assets;

    public Account(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    Account(){}

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
    public List<Double> getAssets() {
        return assets;
    }
}
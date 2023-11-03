package stockmark.stockmark.logic;

import java.util.List;

record Stock(int id, int amount){}

// Single account for one user
public class Account {

    private String email;
    private String password; // we don't need encryption, store as plain text. fix if TA complains
    private double balance; // amount of liquid money in the account
    private List<Stock> assets;

    // return json object as string of this instance
    // for example "{"email": "hk.32@outlook.com", "password": "12345", "balance": 22160 , "assets": []}"
    // then store these in a txt file, one user per row. hence we won't need a database
    public String serialize() {
        return "{\"email\": \"" + email + "\", \"password\": \"" + password + "\", \"balance\": " + balance + ", \"assets\": []}";
    }
}
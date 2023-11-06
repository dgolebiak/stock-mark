package stockmark.stockmark.model;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import stockmark.stockmark.model.Account;
import java.util.ArrayList;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AccountManager {
    
    private ArrayList<Account> accounts; // Will change to a hashmap in the future.

    public AccountManager() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            accounts = objectMapper.readValue(new File("src/main/resources/accounts.json"), new TypeReference<ArrayList<Account>>(){});
        } catch (FileNotFoundException e) {
            System.out.println("No accounts file found.");
        } catch (EOFException e) {
            System.out.println("Accounts file is empty.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading accounts file.");
        }
    }

    public void createAccount(String email, String password) {
        Account account = new Account(email, password);
        accounts.add(account);
        saveAccountsJson();
    }

    public void saveAccountsJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("src/main/resources/accounts.json"), accounts);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving accounts.");
        }
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

}

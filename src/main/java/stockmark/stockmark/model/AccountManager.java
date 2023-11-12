package stockmark.stockmark.model;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class AccountManager {
    
    private HashMap<String, Account> accounts;

    public AccountManager() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Account> accountsList = objectMapper.readValue(
                new File("src/main/resources/accounts.json"), 
                new TypeReference<List<Account>>(){}
            );
            accounts = new HashMap<>();
            for (Account account : accountsList) {
                accounts.put(account.getEmail(), account);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No accounts file found.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading accounts file.");
        }
    }

    public void createAccount(String email, String password) {
        if (accounts.containsKey(email)) {
            System.out.println("Account with email already exists.");
            return;
        }
        Account account = new Account(email, password);
        accounts.put(email, account);
        saveAccountsJson();
        //return html
    }

    public void loginAccount(String email, String password) {
        if (!accounts.containsKey(email)) {
            System.out.println("Account with this email does not exist.");
            return;
        }
        Account account = accounts.get(email);
    
        if (account.getPassword().equals(password)) {
            System.out.println("Login successful!");
            return; //return html
        } else {
            System.out.println("Incorrect password."); //raise exception
        }
    }

    public void saveAccountsJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(
                new File("src/main/resources/accounts.json"), 
                accounts.values()
            );
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving accounts.");
        }
    }

    public HashMap<String, Account> getAccounts() {
        return accounts;
    }
}
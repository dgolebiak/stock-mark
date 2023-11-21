package stockmark.stockmark.model;
import com.fasterxml.jackson.databind.ObjectMapper;

import stockmark.stockmark.model.Exceptions.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class AccountManager {
    private static String accountsFile = "./src/main/resources/accounts.json";
    private static HashMap<String, Account> accounts;

    public static void Initialize() {
        if (accounts != null) return;
        try {
            File fileObj = new File(accountsFile);
            Account[] accountsArray = new ObjectMapper().readValue(fileObj, Account[].class);

            accounts = new HashMap<>();
            for (Account account : accountsArray) {
                accounts.put(account.getEmail(), account);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }

    public static void registerAccount(Account acc) throws AccountExistsException {
        if (accounts.containsKey(acc.getEmail())) {
            throw new AccountExistsException();
        }
        accounts.put(acc.getEmail(), acc);
        saveAccountsJson();
    }

    public static Account loginAccount(String email, String password) throws AccountNotFoundException, IncorrectPasswordException {
        if (!accounts.containsKey(email)) throw new AccountNotFoundException();

        Account account = accounts.get(email);
        if (!account.getPassword().equals(password)) throw new IncorrectPasswordException();
        return account;
    }

    private static void saveAccountsJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(
                new File(accountsFile), 
                accounts.values()
            );
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving accounts.");
        }
    }
}
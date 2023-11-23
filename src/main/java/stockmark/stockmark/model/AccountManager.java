package stockmark.stockmark.model;
import com.fasterxml.jackson.databind.ObjectMapper;

import stockmark.stockmark.model.Exceptions.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class AccountManager {
    private static String accountsFile = "./src/main/resources/accounts.json";
    private static HashMap<String, Account> accounts;
    private static HashMap<UUID, String> loggedIn = new HashMap<UUID, String>();

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

    public static UUID registerAccount(Account acc) throws AccountExistsException {
        if (accounts.containsKey(acc.getEmail())) {
            throw new AccountExistsException();
        }
        accounts.put(acc.getEmail(), acc);
        saveAccountsJson();
        UUID id = java.util.UUID.randomUUID();
        loggedIn.put(id, acc.getEmail());
        return id;
    }

    public static UUID loginAccount(String email, String password) throws AccountNotFoundException, IncorrectPasswordException {
        if (!accounts.containsKey(email)) throw new AccountNotFoundException();

        Account account = accounts.get(email);
        if (!account.getPassword().equals(password)) throw new IncorrectPasswordException();
        UUID id = java.util.UUID.randomUUID();
        loggedIn.put(id, email);
        return id;
    }

    public static boolean isLoggedIn(UUID id) {
        return loggedIn.containsKey(id);
    }

    public static Account getFromUUID(UUID id) {
        return accounts.get(loggedIn.get(id));
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
package stockmark.stockmark.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import stockmark.stockmark.model.Exceptions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AccountManager {
    private static String accountsFile = "./src/main/resources/accounts.json";
    private static HashMap<String, Account> accounts;
    private static HashMap<UUID, String> loggedIn = new HashMap<UUID, String>();

    // Only required to call once at the start of the application
    public static void Initialize() {
        if (accounts != null)
            throw new RuntimeException("AccountManager should not be initialized more than once!");
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

    // UUID is uniquely generated id that is valid for the current login session.
    // registerAccount throws an error if unsuccessful or returns a valid session
    // identifier
    public static UUID registerAccount(Account acc) throws AccountExistsException {
        if (accounts.containsKey(acc.getEmail())) {
            throw new AccountExistsException();
        }
        accounts.put(acc.getEmail(), acc);
        syncToDisk();
        UUID id = java.util.UUID.randomUUID();
        loggedIn.put(id, acc.getEmail());
        return id;
    }

    // UUID is uniquely generated id that is valid for the current login session.
    // loginAccount throws an error if unsuccessful or returns a valid session
    // identifier
    public static UUID loginAccount(String email, String password)
            throws AccountNotFoundException, IncorrectPasswordException {
        if (!accounts.containsKey(email))
            throw new AccountNotFoundException();

        Account account = accounts.get(email);
        if (!account.getPassword().equals(password))
            throw new IncorrectPasswordException();
        UUID id = java.util.UUID.randomUUID();
        loggedIn.put(id, email);
        return id;
    }

    // checks if the provided UUID is one that is linked to an Account at this
    // moment in time
    public static boolean isLoggedIn(UUID id) {
        return loggedIn.containsKey(id);
    }

    // unlinks the provided UUID from its currently linked Account
    public static void logoutAccount(UUID id) {
        if (loggedIn.containsKey(id))
            loggedIn.remove(id);
    }

    // gets the linked Account of the provided UUID, can be null if not found so use
    // `isLoggedIn` to guarantee a non null return value
    public static Account getFromUUID(UUID id) {
        return accounts.get(loggedIn.get(id));
    }

    // persist data to disk
    public static void syncToDisk() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(
                    new File(accountsFile),
                    accounts.values());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving accounts.");
        }
    }

    public static List<Account> getAccounts() {
        return new ArrayList<>(accounts.values());
    }
}
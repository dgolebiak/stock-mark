package stockmark.stockmark.console;

import stockmark.stockmark.model.Account;
import stockmark.stockmark.model.AccountManager;
import stockmark.stockmark.model.Market;
import stockmark.stockmark.model.Exceptions.*;

public class Main {
    public static void main(String[] args) {
        //System.out.println("STONKSSS!");
        //Market.Initialize();
        AccountManager.Initialize();

        /* try {
            AccountManager.registerAccount(new Account("Sander", "sander@gmail.com", "123"));
        } catch (AccountExistsException e) {
            System.out.println(e);
        } */

        try {
            AccountManager.loginAccount("sander@gmail.com", "123");
            System.out.println("Successful");
        } catch (AccountNotFoundException e) {
            System.out.println(e);
        } catch (IncorrectPasswordException e) {
            System.out.println(e);
        }

        /* try {
            System.out.println(Market.getPrice("TSLA"));
        } catch (NonExistentTickerException e) {
            System.out.println("Ticker not found!");
        } */
    }
}

package stockmark.stockmark.console;

import java.util.Arrays;

import stockmark.stockmark.model.Account;
import stockmark.stockmark.model.AccountManager;
import stockmark.stockmark.model.Market;
import stockmark.stockmark.model.Shares;
import stockmark.stockmark.model.Exceptions.*;

public class Main {
    public static void main(String[] args) {
        //System.out.println("STONKSSS!");
        Market.Initialize();
        var market = Market.getInstance();

        try {
            System.out.println(market.getPrice("TSLA"));
        } catch (NonExistentTickerException e) {
            System.out.println("Ticker not found!");
        }

        System.exit(0);
    }
}

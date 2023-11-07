package stockmark.stockmark.console;

import stockmark.stockmark.model.Market;
import stockmark.stockmark.model.NonExistentTickerException;

public class Main {
    public static void main(String[] args) {
        System.out.println("STONKSSS!");
        Market.Initialize();

        try {
            System.out.println(Market.getPrice("TSLA"));
        } catch (NonExistentTickerException e) {
            System.out.println("Ticker not found!");
        }
    }
}

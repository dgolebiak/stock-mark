package stockmark.stockmark.console;

import java.util.Arrays;

import stockmark.stockmark.model.Market;

public class Main {
    public static void main(String[] args) {
        System.out.println("STOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOONKSSS!");

        Market.Initialize();
        System.out.println(Arrays.toString(Market.getSupportedTickers()[0].daysHistory()));
    }
}

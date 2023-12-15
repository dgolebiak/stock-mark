package stockmark.stockmark.model;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import stockmark.stockmark.model.Exceptions.NonExistentTickerException;
import stockmark.stockmark.model.Types.StockObserver;
import stockmark.stockmark.model.Types.Ticker;

record LocalStockInfo(double price, double pcChange){};

public class Market implements StockObserver {
    private static final String tickersFile = "./src/main/resources/tickers.json";
    private static Market instance;
    private Ticker[] tickers;
    private int updates;
    private final int numTickers;

    private Market() {
        try {
            // load supported tickers from file
            File fileObj = new File(tickersFile);
            tickers = new ObjectMapper().readValue(fileObj, Ticker[].class);

            for (Ticker ticker : tickers) {
                new StockUpdater(ticker, this);
            }

            numTickers = tickers.length;
            updates = 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Only required to call once at the start of the application
    public static void Initialize() {
        if (instance != null)
            throw new RuntimeException("Market should not be initialized more than once!");
        instance = new Market();
    }

    public static Market getInstance() {
        if (instance == null)
            throw new RuntimeException("Initialize Market first!");
        return instance;
    }

    // called by Stock instances
    public void updatePrice(Ticker ticker, double price, double pcChange) {
        updates++;
        for (int i = 0; i < tickers.length; i++) {
            if (tickers[i].name() == ticker.name()) {
                tickers[i] = new Ticker(ticker.company(), ticker.name(), price, pcChange);
                break;
            }
        }

        if (updates >= numTickers) {
            updates = 0;
            syncToDisk();
        }
    }

    public Ticker[] getSupportedTickers() {
        return tickers;
    }

    public boolean isSupportedTicker(Ticker ticker) {
        for (Ticker t : tickers) {
            if (t.equals(ticker))
                return true;
        }
        return false;
    }

    public boolean isSupportedTicker(String name) {
        for (Ticker t : tickers) {
            if (t.name().equals(name))
                return true;
        }
        return false;
    }

    public double getPrice(String ticker) throws NonExistentTickerException {
        for (Ticker t : tickers) {
            if (t.name().equals(ticker))
                return t.priceToday();
        }
        throw new NonExistentTickerException();
    }

    public double getPercentChangeToday(String ticker) throws NonExistentTickerException {
        for (Ticker t : tickers) {
            if (t.name().equals(ticker))
                return t.pcChangeToday();
        }
        throw new NonExistentTickerException();
    }

    // persist data to disk
    private void syncToDisk() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(
                    new File(tickersFile),
                    tickers);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving tickers.");
        }
    }
}

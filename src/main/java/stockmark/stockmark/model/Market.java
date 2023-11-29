package stockmark.stockmark.model;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.databind.ObjectMapper;

import stockmark.stockmark.model.Exceptions.NonExistentTickerException;
import stockmark.stockmark.model.Types.StockObserver;
import stockmark.stockmark.model.Types.Ticker;

record LocalStockInfo(double price, double pcChange){};

public class Market implements StockObserver {
    private static final String tickersFile = "./src/main/resources/tickers.json";
    private static Market instance;

    private ConcurrentHashMap<String, LocalStockInfo> priceMap = new ConcurrentHashMap<String, LocalStockInfo>();
    private Ticker[] tickers;
    private AtomicBoolean allLoaded;

    private Market() {
        try {
            // load supported tickers from file
            File fileObj = new File(tickersFile);
            tickers = new ObjectMapper().readValue(fileObj, Ticker[].class);

            allLoaded = new AtomicBoolean();
            for (Ticker ticker : tickers) {
                new StockUpdater(ticker, this);
            }

            // wait till all is loaded
            while (!allLoaded.get()) {
            }
            System.out.println("Initial prices have been loaded!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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
        priceMap.put(ticker.name(), new LocalStockInfo(price, pcChange));
        if (!allLoaded.get()) {
            if (priceMap.size() == tickers.length)
                allLoaded.set(true);
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
        if (isSupportedTicker(ticker))
            return priceMap.get(ticker).price();
        throw new NonExistentTickerException();
    }

    public double getPercentChangeToday(String ticker) throws NonExistentTickerException {
        if (isSupportedTicker(ticker))
            return priceMap.get(ticker).pcChange();
        throw new NonExistentTickerException();
    }
}

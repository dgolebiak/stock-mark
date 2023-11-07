package stockmark.stockmark.model;

import java.io.File;

// This class holds the information about stock prices locally in memory
// And provides methods to lookup stock tickers such as "TSLA" or "AAPL"

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Market {
    private static String tickersFile = "./src/main/resources/tickers.json";
    private static Ticker[] tickers;
    private static ConcurrentHashMap<String, Double> priceMap = new ConcurrentHashMap<String, Double>();

    private Market() {}

    // call only once at the start of the application in StockmarkApplication.java
    public static void Initialize() {
        if (tickers != null)
            return;
        try {
            // load supported tickers from file
            File myObj = new File(tickersFile);
            tickers = new ObjectMapper().readValue(myObj, Ticker[].class);
        } catch (Exception e) {
            System.out.println("Exception:" + e);
            e.printStackTrace();
            return;
        }

        // load prices before letting the application continue
        updateAllPrices();
        System.out.println("Initial prices have been loaded!");

        // start fetcher in the background
        startFetcher();
    }

    private static void updateAllPrices() {
        ArrayList<Thread> threads = new ArrayList<Thread>();

        // initial load prices
        for (Ticker ticker : tickers) {
            Thread th = new Thread(() -> {
                String priceStr = inquirePriceFromAPI(ticker.stockTicker());
                priceMap.put(ticker.stockTicker(), Double.parseDouble(priceStr));
            });

            threads.add(th);
            th.start();
        }

        // join all threads
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Exception:" + e);
                e.printStackTrace();
                return;
            }
        }
    }

    private static void startFetcher() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(120000);
                } catch (InterruptedException e) {
                    System.out.println("Exception:" + e);
                    e.printStackTrace();
                    return;
                }
    
                updateAllPrices();
                System.out.println("Prices have been updated!");
            }
        }).start();
    }

    public static Ticker[] getSupportedTickers() {
        if (tickers == null)
            throw new RuntimeException("Market -> No Tickers found! Maybe Initialize first?");
        return tickers;
    }

    public static double getPrice(String ticker) throws NonExistentTickerException {
        if (priceMap.containsKey(ticker))
            return priceMap.get(ticker);
        throw new NonExistentTickerException();
    }

    // Just for testing purposes right now
    public static String inquirePriceFromAPI(String ticker) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://realstonks.p.rapidapi.com/" + ticker))
                .header("X-RapidAPI-Key", "bffff99a21msh3201484be4f30c1p1ab348jsn39aebb9ede1c")
                .header("X-RapidAPI-Host", "realstonks.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());
            HashMap<String, Object> result = new ObjectMapper().readValue(response.body(), HashMap.class);

            return result.get("price").toString();
        } catch (Exception e) {
            return e.toString();
        }
    }
}

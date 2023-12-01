package stockmark.stockmark.web;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import stockmark.stockmark.model.Account;
import stockmark.stockmark.model.AccountManager;
import stockmark.stockmark.model.Market;
import stockmark.stockmark.model.Exceptions.NonExistentTickerException;
import stockmark.stockmark.model.Types.ChangeOverTime;
import stockmark.stockmark.model.Types.Ticker;

@Controller
public class TradeController {
    @GetMapping("/trade")
    String onGetTrade(@CookieValue(value = "uuid", defaultValue = "") String uuid, Model model) {

        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
        decimalFormatSymbols.setDecimalSeparator('.');
        var dc = new DecimalFormat("0.00", decimalFormatSymbols);


        /* We should probably have some kind of parent class that has this or similar code because it will be similar between Controllers 
         * Then we might be able to call this by doing: parent.something
        */
        if (uuid.equals("") || !AccountManager.isLoggedIn(java.util.UUID.fromString(uuid)))
            return "redirect:/";
        Account acc = AccountManager.getFromUUID(java.util.UUID.fromString(uuid));
        // not complete
        var assets = acc.getAssets();
        // to be sent to html template

        Market market = Market.getInstance();

        Ticker[] tickers = market.getSupportedTickers();
        String[] stocks = new String[tickers.length];
        
        

        int i = 0;
        for (Ticker ticker : tickers){
             try {
<<<<<<< HEAD
                int amount = 0;
                double worth = 0;
                double priceChangeDollar = 0;
                double priceChangePercent = 0; 
                if (assets.containsKey(ticker.name())){
                amount = assets.get(ticker.name()).amount();
                worth = amount * Market.getInstance().getPrice(ticker.name());
                double oldWorth =  amount * assets.get(ticker.name()).buyPrice();
                ChangeOverTime priceChange = new ChangeOverTime(ticker.name(), worth, oldWorth);
                priceChangeDollar = priceChange.current() - priceChange.old();
                priceChangePercent = 100 * (priceChange.current() / priceChange.old());        
                }

                stocks[i++] = String.format("{ name: '%s', symbol: '%s', price: '%s', pcChange: '%s', amount: %d, worth: '%s', ownedPriceChangeDollar: '%s', ownedPriceChangePercent: '%s' }", 
                ticker.company(),
                ticker.name(), 
                dc.format(Market.getInstance().getPrice(ticker.name())), 
                Market.getInstance().getPercentChangeToday(ticker.name()) + "%", 
                amount, 
                dc.format(worth), 
                dc.format(priceChangeDollar), 
                dc.format(priceChangePercent)
    ); 
                
                



=======
                double price = market.getPrice(ticker.name());
                double pcChange = market.getPercentChangeToday(ticker.name());

                stocks[i++] = String.format("{ name: '%s', price: %f, pcChange: %f }", 
                    ticker.company() , price, pcChange);
>>>>>>> 106d37921a665a43d3a2eecf767c0d6d815d6866

            } catch (NonExistentTickerException e) {
                e.printStackTrace();
            }
        }

        Arrays.sort(stocks, Comparator.comparingDouble(s -> {
            String[] stockDetails = s.split(",");
            return Double.parseDouble(stockDetails[2].replaceAll("[^\\d.-]", "").trim());
        }));

        String[] worstStocks = Arrays.copyOf(stocks, stocks.length);
        Collections.reverse(Arrays.asList(worstStocks));

        model.addAttribute("assets", acc.getAssets());

        model.addAttribute("pricedStocksWorstPerforming", stocks);
        model.addAttribute("pricedStocksBestPerforming", worstStocks);
        return "trade";
    }
    @GetMapping("/transaction")
    public String onTransaction(@CookieValue(value = "uuid", defaultValue = "") String uuid, @RequestParam String action,
            @RequestParam String ticker,
            @RequestParam String quantity, Model model) {
        // if not logged in; redirect to login
        System.out.println("Initiate transaction");
        if (uuid.equals("") || !AccountManager.isLoggedIn(java.util.UUID.fromString(uuid)))
            return "redirect:/";

        Account acc = AccountManager.getFromUUID(java.util.UUID.fromString(uuid));
        int amountInt = Integer.parseInt(quantity);

        if (action.equals("buy")) {
            try {
                acc.buyAsset(ticker, amountInt);
            } catch (Exception e) {
                // Handle properly later... frontend dis-allows erroneus inputs anyways.
                System.out.println(e);
            }
        } else if (action.equals("sell")) {
            try {
                acc.sellAsset(ticker, amountInt);
            } catch (Exception e) {
                // Handle properly later... frontend dis-allows erroneus inputs anyways.
                System.out.println(e);
            }
        }
        return "redirect:/trade";
    }
}

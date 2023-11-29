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

import stockmark.stockmark.model.Account;
import stockmark.stockmark.model.AccountManager;
import stockmark.stockmark.model.Market;
import stockmark.stockmark.model.Ticker;
import stockmark.stockmark.model.Exceptions.NonExistentTickerException;

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
        model.addAttribute("assets", acc.getAssets());

        Ticker[] tickers = Market.getInstance().getSupportedTickers();
        String[] stocks = new String[tickers.length];
        
        

        int i = 0;
        for (Ticker ticker : tickers){
             try {
                stocks[i++] = String.format("{ name: '%s', price: '%s', pcChange: '%s' }", ticker.company() , 
                dc.format(Market.getInstance().getPrice(ticker.name())), dc.format(Market.getInstance().getPercentChangeToday(ticker.name())));
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

        model.addAttribute("pricedStocksWorstPerforming", stocks);
        model.addAttribute("pricedStocksBestPerforming", worstStocks);
        return "trade";
    }
}

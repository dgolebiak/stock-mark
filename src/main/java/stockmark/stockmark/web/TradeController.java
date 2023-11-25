package stockmark.stockmark.web;

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
import stockmark.stockmark.model.PricedStock;

@Controller
public class TradeController {
    @GetMapping("/trade")
    String onGetTrade(@CookieValue(value = "uuid", defaultValue = "") String uuid, Model model) {


        /* We should probably have some kind of parent class that has this or similar code because it will be similar between Controllers 
         * Then we might be able to call this by doing: parent.something
        */
        if (uuid.equals("") || !AccountManager.isLoggedIn(java.util.UUID.fromString(uuid)))
            return "redirect:/";
        Account acc = AccountManager.getFromUUID(java.util.UUID.fromString(uuid));
        // not complete
        model.addAttribute("assets", acc.getAssets());


        PricedStock[] stocks = Market.getInstance().getPricedStocks();
        
        Arrays.sort(stocks, Comparator.comparing(PricedStock::percentage));
        
        PricedStock[] worstStocks = Arrays.copyOf(stocks, stocks.length);
        Collections.reverse(Arrays.asList(worstStocks));

        model.addAttribute("pricedStocksWorstPerforming", stocks);
        model.addAttribute("pricedStocksBestPerforming", worstStocks);
        return "trade";
    }
}

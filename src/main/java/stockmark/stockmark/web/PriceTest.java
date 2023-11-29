package stockmark.stockmark.web;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import stockmark.stockmark.model.Account;
import stockmark.stockmark.model.AccountManager;
import stockmark.stockmark.model.Market;
import stockmark.stockmark.model.Exceptions.NonExistentTickerException;

@RestController
public class PriceTest {
    @GetMapping("/price/{ticker}")
    String getPrice(@PathVariable String ticker) {
        double price;
        try {
            price = Market.getInstance().getPrice(ticker);
        } catch (NonExistentTickerException e) {
            return "Ticker not found!";
        }

        return Double.toString(price);
    }

    @GetMapping("/balance")
    public String onGetBalance(@CookieValue(value = "uuid", defaultValue = "") String uuid) {
        // if not logged in; redirect to login
        if (uuid.equals("") || !AccountManager.isLoggedIn(java.util.UUID.fromString(uuid)))
            return "0";
            
        Account acc = AccountManager.getFromUUID(java.util.UUID.fromString(uuid));
        return acc.getBalance() + "";
    }
}

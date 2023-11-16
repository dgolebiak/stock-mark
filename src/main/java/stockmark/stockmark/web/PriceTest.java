package stockmark.stockmark.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import stockmark.stockmark.model.Market;
import stockmark.stockmark.model.Ticker;
import stockmark.stockmark.model.Exceptions.NonExistentTickerException;

@RestController
public class PriceTest {
    @GetMapping("/price/{ticker}")
    String getPrice(@PathVariable String ticker) {
        double price;
        try {
            price = Market.getPrice(ticker);
        } catch (NonExistentTickerException e) {
            return "Ticker not found!";
        }

        return Double.toString(price);
    }

    @GetMapping("/tickers")
    Ticker[] getSupportTickers() {
        return Market.getSupportedTickers();
    }
}

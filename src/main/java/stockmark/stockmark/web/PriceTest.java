package stockmark.stockmark.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import stockmark.stockmark.model.Market;
import stockmark.stockmark.model.Ticker;

@RestController
public class PriceTest {
    @GetMapping("/price/{ticker}")
    String getPrice(@PathVariable String ticker) {
        return Market.inquirePrice(ticker);
    }

    @GetMapping("/tickers")
    Ticker[] getSupportTickers() {
        return Market.getSupportedTickers();
    }
}

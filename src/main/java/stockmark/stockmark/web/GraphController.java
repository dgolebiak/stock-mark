package stockmark.stockmark.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import stockmark.stockmark.model.Types.StockPriceStamp;

import stockmark.stockmark.model.ExternalAPI;


@Controller
public class GraphController {

    @GetMapping("/stockgraph")
    @ResponseBody
    public List<StockPriceStamp> onGetGraph(@RequestParam String ticker, @RequestParam int lastNdays, @RequestParam String intervals) {  
        return ExternalAPI.inquireHistory(ticker, lastNdays, intervals);
    }

}

package stockmark.stockmark;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import stockmark.stockmark.model.ExternalAPI;
import stockmark.stockmark.model.Types.StockPriceStamp;

import java.util.*;

@SpringBootTest
class StockmarkApplicationTests {

	@Test
	void contextLoads() {
		ArrayList<StockPriceStamp> history = ExternalAPI.inquireHistory("MSFT", 365, "5d");
		for (int i = 0; i < history.size(); i++) {
			System.out.println(history.get(i));
		}
		System.out.println("done");
	}

}

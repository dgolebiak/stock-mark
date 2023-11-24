package stockmark.stockmark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import stockmark.stockmark.model.Market;
import stockmark.stockmark.model.AccountManager;

@SpringBootApplication
public class StockmarkApplication {
	public static void main(String[] args) {
		Market.Initialize();
		AccountManager.Initialize();
		SpringApplication.run(StockmarkApplication.class, args);
	}
}

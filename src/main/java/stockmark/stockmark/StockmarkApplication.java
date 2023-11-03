package stockmark.stockmark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import stockmark.stockmark.model.Market;

@SpringBootApplication
public class StockmarkApplication {
	public static void main(String[] args) {
		Market.Initialize();
		SpringApplication.run(StockmarkApplication.class, args);
	}
}

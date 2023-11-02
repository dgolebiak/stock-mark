package stockmark.stockmark.logic;

// This class holds the information about stock prices locally in memory
// And provides methods to lookup stock tickers such as "TSLA" or "AAPL"

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Market {

    // For reference
    /* class ApiResponse {
        double price;
        double change_point;
        double change_percentage;
        String total_vol;
    } */

    // Just for testing purposes right now
    public static String inquirePrice(String ticker) {
        if (ticker.equals("")) return "Marke.getPric(ticker) -> Error: Provide Ticker!";
        HttpRequest request = HttpRequest.newBuilder()
		.uri(URI.create("https://realstonks.p.rapidapi.com/"+ticker))
		.header("X-RapidAPI-Key", "bffff99a21msh3201484be4f30c1p1ab348jsn39aebb9ede1c")
		.header("X-RapidAPI-Host", "realstonks.p.rapidapi.com")
		.method("GET", HttpRequest.BodyPublishers.noBody())
		.build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            HashMap<String, Object> result = new ObjectMapper().readValue(response.body(), HashMap.class);

            return result.get("price").toString();
        } catch (Exception e) {
            return e.toString();
        }
    }
}

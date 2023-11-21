package stockmark.stockmark.model;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ExternalAPI {
    private ExternalAPI() {}

    public static String inquirePrice(String ticker) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://realstonks.p.rapidapi.com/" + ticker))
                .header("X-RapidAPI-Key", "bffff99a21msh3201484be4f30c1p1ab348jsn39aebb9ede1c")
                .header("X-RapidAPI-Host", "realstonks.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());
            HashMap<String, Object> result = new ObjectMapper().readValue(response.body(), HashMap.class);

            return result.get("price").toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void inquireHistory(String ticker, int lastNdays) {

    }
}

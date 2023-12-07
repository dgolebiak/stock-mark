package stockmark.stockmark.model;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import stockmark.stockmark.model.Types.RealStonksResponse;

public class ExternalAPI {
    private ExternalAPI() {}

    public static RealStonksResponse inquireTicker(String ticker) {
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

            double price = Double.parseDouble(result.get("price").toString());
            double pcChange = Double.parseDouble(result.get("change_percentage").toString());

            return new RealStonksResponse(price, pcChange);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static double[] inquireHistory(String ticker, int lastNdays) {

        int i = 0;
        double[] history = new double[lastNdays];

        long currentUnixTimestamp = Instant.now().getEpochSecond();
        long unixTimestampNDaysAgo = Instant.now().minusSeconds(lastNdays * 24*60*60).getEpochSecond();

        HttpRequest request = HttpRequest.newBuilder()
		.uri(URI.create("https://telescope-stocks-options-price-charts.p.rapidapi.com/price/" + ticker + "?period1=" + unixTimestampNDaysAgo + "&period2=" + currentUnixTimestamp + "&interval=1d"))
		.header("X-RapidAPI-Key", "f36b91a7bfmshea0da7f952d7754p1baea1jsnad99e9294baf")
		.header("X-RapidAPI-Host", "telescope-stocks-options-price-charts.p.rapidapi.com")
		.method("GET", HttpRequest.BodyPublishers.noBody())
		.build();
        try {
         HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
         JsonNode rootNode = new ObjectMapper().readTree(response.body());
        JsonNode jsonHistory = rootNode.path("chart").path("result").get(0).path("indicators").path("adjclose").get(0).path("adjclose");

        for (JsonNode element : jsonHistory){
            history[i] = element.asDouble();
            i++;
        }
        System.out.println(Arrays.toString(history));
        return history;
        
        }catch (Exception e) {
            System.out.println(e);
            return null;
    }
}}

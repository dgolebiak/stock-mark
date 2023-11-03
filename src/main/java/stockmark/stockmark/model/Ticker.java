package stockmark.stockmark.model;

// Ticker("Google", "GOOG", [131.4, 130.2])
public record Ticker(String companyName, String stockTicker, double[] daysHistory){}
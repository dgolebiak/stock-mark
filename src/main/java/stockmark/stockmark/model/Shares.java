package stockmark.stockmark.model;

// Shares("TSLA", 4)
public record Shares(String name, int amount, double buyPrice, String buyDate){}

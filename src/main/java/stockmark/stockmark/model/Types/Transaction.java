package stockmark.stockmark.model.Types;

// Example: Transaction("buy", "TSLA", 4, 235, "2023-11-25")
public record Transaction(String action, String ticker, int amount, double unitPrice, String time){}

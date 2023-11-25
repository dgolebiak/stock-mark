package stockmark.stockmark.model;

// PricedStock("Google", 1555, 5.4)
public record PricedStock(String name, double price, double percentage){}
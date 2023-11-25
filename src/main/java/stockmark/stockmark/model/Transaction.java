package stockmark.stockmark.model;

public record Transaction(String action, String ticker, int amount, double unitPrice, String time){}

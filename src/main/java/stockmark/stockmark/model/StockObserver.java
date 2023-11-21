package stockmark.stockmark.model;

public interface StockObserver {
    void updatePrice(Ticker ticker, double newPrice);
}

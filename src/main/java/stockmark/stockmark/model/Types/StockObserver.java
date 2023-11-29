package stockmark.stockmark.model.Types;

public interface StockObserver {
    void updatePrice(Ticker ticker, double newPrice, double newPcChange);
}

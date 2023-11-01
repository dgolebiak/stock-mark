import java.util.ArrayList;

// Single account for one user
public class Account {
    private double riksdaler;
    private String name;
    private Map<Stock, Integer> ownedStocks;

    public String getName() {
        return name;
    }
    public void deductRiksdaler(double riksdaler) {
        this.riksdaler -= riksdaler;
    }
    public void addRiksdaler(double riksdaler) {
        this.riksdaler += riksdaler;
    }
    public double getRiksdaler() {
        return riksdaler;
    }
    public void addStock(Stock stock, int quantity) {
        stocks.put(stock, quantity);
    }
    public void removeStock(Stock stock, int quantity) {
        stocks.put(stock, stocks.get(stock) - quantity);
    }
}

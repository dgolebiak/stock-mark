package stockmark.stockmark.model;

public class StockUpdater extends Thread {
    private Ticker ticker;
    private StockObserver observer;

    public StockUpdater(Ticker ticker, StockObserver ob) {
        this.ticker = ticker;
        this.observer = ob;
        this.start();
    }

    @Override
    public void run() {
        while (true) {
            String priceStr = ExternalAPI.inquirePrice(ticker.name());
            double price = Double.parseDouble(priceStr);
            if (price != 0)
                observer.updatePrice(ticker, price);

            try {
                Thread.sleep(120000);
            } catch (InterruptedException e) {
                System.out.println("Exception:" + e);
                e.printStackTrace();
                return;
            }
        }
    }
}
package stockmark.stockmark.model;

import stockmark.stockmark.model.Types.RealStonksResponse;
import stockmark.stockmark.model.Types.StockObserver;
import stockmark.stockmark.model.Types.Ticker;

public class StockUpdater extends Thread {
    private Ticker ticker;
    private StockObserver observer;
    private final long waitTime = 120000; // milliseconds

    public StockUpdater(Ticker ticker, StockObserver ob) {
        this.ticker = ticker;
        this.observer = ob;
        this.start();
    }

    // runs in the background as a thread and updates the prices once every x milliseconds
    @Override
    public void run() {
        while (true) {
            RealStonksResponse res = ExternalAPI.inquireTicker(ticker.name());
            if (res != null)
                observer.updatePrice(ticker, res.price(), res.pcChange());

            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                System.out.println("Exception:" + e);
                e.printStackTrace();
                return;
            }
        }
    }
}
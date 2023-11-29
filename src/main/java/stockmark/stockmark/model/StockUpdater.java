package stockmark.stockmark.model;

import stockmark.stockmark.model.Types.RealStonksResponse;
import stockmark.stockmark.model.Types.StockObserver;
import stockmark.stockmark.model.Types.Ticker;

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
            RealStonksResponse res = ExternalAPI.inquireTicker(ticker.name());
            if (res != null)
                observer.updatePrice(ticker, res.price(), res.pcChange());

                break;
            /* try {
                Thread.sleep(120000);
            } catch (InterruptedException e) {
                System.out.println("Exception:" + e);
                e.printStackTrace();
                return;
            } */
        }
    }
}
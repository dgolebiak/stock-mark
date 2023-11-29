package stockmark.stockmark.model;

public class Stock extends Thread {
    private Ticker ticker;
    private StockObserver observer;

    public Stock(Ticker ticker, StockObserver ob) {
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
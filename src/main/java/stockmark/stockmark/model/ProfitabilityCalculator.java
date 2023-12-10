package stockmark.stockmark.model;

import java.util.HashMap;

import stockmark.stockmark.model.Exceptions.NonExistentTickerException;
import stockmark.stockmark.model.Types.ChangeOverTime;
import stockmark.stockmark.model.Types.Share;

public class ProfitabilityCalculator {
    public ChangeOverTime calcMostProfitableOverall(HashMap<String,Share> assets) {
        String mostProfitableName = null;
        double mostProfit = -1000000;

        double worth = 0;
        double oldWorth = 0;

        for (String ticker : assets.keySet()) {
            try {
                double currentPrice = Market.getInstance().getPrice(ticker);
                Share myShare = assets.get(ticker);
                double myShareWorthToday = (double) myShare.amount() * currentPrice;
                double howMuchIpaidForIt = (double) myShare.amount() * myShare.buyPrice();
                double profit = myShareWorthToday - howMuchIpaidForIt;

                // calc most profitable overall
                if (profit > mostProfit) {
                    mostProfit = profit;
                    mostProfitableName = ticker;
                    worth = myShareWorthToday;
                    oldWorth = howMuchIpaidForIt;
                }
            } catch (NonExistentTickerException e) {
                System.out.println("Non existent ticker, but data came from database so internal error...");
            }
        }

        if (mostProfitableName == null)
            return null;
        return new ChangeOverTime(mostProfitableName, worth, oldWorth);
    }

    public ChangeOverTime calcLeastProfitableOverall(HashMap<String,Share> assets) {
        String leastProfitableName = null;
        double leastProfit = 1000000;

        double worth = 0;
        double oldWorth = 0;

        for (String ticker : assets.keySet()) {
            try {
                double currentPrice = Market.getInstance().getPrice(ticker);
                Share myShare = assets.get(ticker);
                double myShareWorthToday = (double) myShare.amount() * currentPrice;
                double howMuchIpaidForIt = (double) myShare.amount() * myShare.buyPrice();
                double profit = myShareWorthToday - howMuchIpaidForIt;

                // calc least profitable overall
                if (profit < leastProfit) {
                    leastProfit = profit;
                    leastProfitableName = ticker;
                    worth = myShareWorthToday;
                    oldWorth = howMuchIpaidForIt;
                }
            } catch (NonExistentTickerException e) {
                System.out.println("Non existent ticker, but data came from database so internal error...");
            }
        }

        if (leastProfitableName == null)
            return null;
        return new ChangeOverTime(leastProfitableName, worth, oldWorth);
    }

    public ChangeOverTime calcMostProfitableToday(HashMap<String,Share> assets) {
        String mostProfitableName = null;
        double mostProfit = -1000000;

        double worth = 0;
        double oldWorth = 0;

        for (String ticker : assets.keySet()) {
            try {
                double priceToday = Market.getInstance().getPrice(ticker);
                double pcChangeToday = Market.getInstance().getPercentChangeToday(ticker);
                double priceYesterday = (100.0/(pcChangeToday+100.0) * priceToday);

                Share myShare = assets.get(ticker);
                double myShareWorthToday = (double) myShare.amount() * priceToday;
                double myShareWorthYesterday = (double) myShare.amount() * priceYesterday;
                double profit = myShareWorthToday - myShareWorthYesterday;

                // calc most profitable today
                if (profit > mostProfit) {
                    mostProfit = profit;
                    mostProfitableName = ticker;
                    worth = myShareWorthToday;
                    oldWorth = myShareWorthYesterday;
                }
            } catch (NonExistentTickerException e) {
                System.out.println("Non existent ticker, but data came from database so internal error...");
            }
        }

        if (mostProfitableName == null)
            return null;
        return new ChangeOverTime(mostProfitableName, worth, oldWorth);
    }

    public ChangeOverTime calcLeastProfitableToday(HashMap<String,Share> assets) {
        String leastProfitableName = null;
        double leastProfit = 1000000;

        double worth = 0;
        double oldWorth = 0;

        for (String ticker : assets.keySet()) {
            try {
                double priceToday = Market.getInstance().getPrice(ticker);
                double pcChangeToday = Market.getInstance().getPercentChangeToday(ticker);
                double priceYesterday = (100.0/(pcChangeToday+100.0) * priceToday);

                Share myShare = assets.get(ticker);
                double myShareWorthToday = (double) myShare.amount() * priceToday;
                double myShareWorthYesterday = (double) myShare.amount() * priceYesterday;
                double profit = myShareWorthToday - myShareWorthYesterday;

                // calc least profitable today
                if (profit < leastProfit) {
                    leastProfit = profit;
                    leastProfitableName = ticker;
                    worth = myShareWorthToday;
                    oldWorth = myShareWorthYesterday;
                }
            } catch (NonExistentTickerException e) {
                System.out.println("Non existent ticker, but data came from database so internal error...");
            }
        }

        if (leastProfitableName == null)
            return null;
        return new ChangeOverTime(leastProfitableName, worth, oldWorth);
    }

    public ChangeOverTime calcValueChangeOverall(HashMap<String,Share> assets, double start, double after) {
        // add assets value to after
        for (String ticker : assets.keySet()) {
            try {
                double currentPrice = Market.getInstance().getPrice(ticker);
                Share myShare = assets.get(ticker);
                double myShareWorthToday = (double) myShare.amount() * currentPrice;
                after += myShareWorthToday;
            } catch (NonExistentTickerException e) {
                System.out.println("Non existent ticker, but data came from database so internal error...");
            }
        }

        return new ChangeOverTime(null, after, start);
    }

    public ChangeOverTime calcValueChangeToday(HashMap<String,Share> assets) {
        double yesterdayValue = 0;
        double todayValue = 0;

        // add assets value to value
        for (String ticker : assets.keySet()) {
            try {
                double priceToday = Market.getInstance().getPrice(ticker);
                double pcChangeToday = Market.getInstance().getPercentChangeToday(ticker);
                double priceYesterday = (100.0/(pcChangeToday+100.0) * priceToday);

                Share myShare = assets.get(ticker);
                double myShareWorthToday = (double) myShare.amount() * priceToday;
                todayValue += myShareWorthToday;

                double myShareWorthYesterday = (double) myShare.amount() * priceYesterday;
                yesterdayValue += myShareWorthYesterday;
            } catch (NonExistentTickerException e) {
                System.out.println("Non existent ticker, but data came from database so internal error...");
            }
        }
        return new ChangeOverTime(null, todayValue, yesterdayValue);
    }
}

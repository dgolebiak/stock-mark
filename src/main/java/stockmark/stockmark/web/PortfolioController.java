package stockmark.stockmark.web;

import org.springframework.ui.Model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import stockmark.stockmark.model.Account;
import stockmark.stockmark.model.AccountManager;
import stockmark.stockmark.model.Market;
import stockmark.stockmark.model.Exceptions.*;
import stockmark.stockmark.model.Types.ChangeOverTime;
import stockmark.stockmark.model.Types.Share;

record ClientAsset(String ticker, int totalValue, double pcChange) {
}

// @CookieValue(value = "uuid", defaultValue = "") String uuid
@Controller
public class PortfolioController {
    @GetMapping("/portfolio")
    public String onGetPortfolio(@CookieValue(value = "uuid", defaultValue = "") String uuid, Model model) {
        // if not logged in; redirect to login
        if (uuid.equals("") || !AccountManager.isLoggedIn(java.util.UUID.fromString(uuid)))
            return "redirect:/";

        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
        decimalFormatSymbols.setDecimalSeparator('.');
        var dc = new DecimalFormat("0.00", decimalFormatSymbols);

        Account acc = AccountManager.getFromUUID(java.util.UUID.fromString(uuid));

        var assets = acc.getAssets();
        // to be sent to html template
        String[] assetData = new String[assets.size()];

        ChangeOverTime mostProfitableOverall = acc.calcMostProfitableOverall();
        ChangeOverTime leastProfitableOverall = acc.calcLeastProfitableOverall();

        ChangeOverTime mostProfitableToday = acc.calcMostProfitableToday();
        ChangeOverTime leastProfitableToday = acc.calcLeastProfitableToday();

        ChangeOverTime valueChangeOverall = acc.calcValueChangeOverall();
        ChangeOverTime valueChangeToday = acc.calcValueChangeToday();

        int i = 0;
        for (String ticker : assets.keySet()) {
            try {
                double price = Market.getInstance().getPrice(ticker);
                double currentPcChange = Market.getInstance().getPercentChangeToday(ticker);
                Share myShare = assets.get(ticker);
                int amount = myShare.amount();
                double buyPrice = myShare.buyPrice();
                double worth = amount * price;
                ChangeOverTime priceChange = new ChangeOverTime(ticker, worth, amount * buyPrice);
                double priceChangeDollar = priceChange.current() - priceChange.old();
                double priceChangePercent = 100 * (priceChange.current() / priceChange.old());
                // prep x-data
                assetData[i] = String.format(
                        "{ name: '%s', symbol: '%s', price: '%s', pcChange: '%s', amount: '%d', worth: '%s', buyPrice: '%s', ownedPriceChangeDollar: '%s', ownedPriceChangePercent: '%s'}",
                        ticker, 
                        ticker,
                        dc.format(price),
                        dc.format(currentPcChange), 
                        amount,
                        dc.format(worth),
                        dc.format(buyPrice),
                        dc.format(priceChangeDollar), 
                        dc.format(priceChangePercent)
                    );
                i++;
            } catch (NonExistentTickerException e) {
                System.out.println("Non existent ticker, but data came from database so internal error...");
            }
        }

        if (mostProfitableOverall == null)
            mostProfitableOverall = new ChangeOverTime("...", 0, 0);
        if (leastProfitableOverall == null)
            leastProfitableOverall = new ChangeOverTime("...", 0, 0);
        if (mostProfitableToday == null)
            mostProfitableToday = new ChangeOverTime("...", 0, 0);
        if (leastProfitableToday == null)
            leastProfitableToday = new ChangeOverTime("...", 0, 0);

        // render portfolio template
        model.addAttribute("currentBalance", "$" + dc.format(acc.getBalance()));
        model.addAttribute("deposited", "$" + dc.format(acc.getDeposited()));

        model.addAttribute("totalValue", "$" + dc.format(valueChangeOverall.current()));
        double valueChange = valueChangeOverall.current() - valueChangeOverall.old();
        model.addAttribute("totalChange", "$" + dc.format(valueChange));
        model.addAttribute("isTotalChangePositive", valueChange > 0);

        double valueChangeTodayN = valueChangeToday.current() - valueChangeToday.old();
        double valueChangePercent = (valueChangeTodayN / valueChangeToday.old()) * 100;
        model.addAttribute("valueChangeToday", "$" + dc.format(valueChangeTodayN));
        model.addAttribute("valueChangeTodayPercent", dc.format(valueChangePercent) + "%");
        model.addAttribute("isValueChangeTodayPositive", valueChangeTodayN > 0);

        double mostProfitAmount = mostProfitableOverall.current() - mostProfitableOverall.old();
        double mostProfitPercent = (mostProfitAmount / mostProfitableOverall.old()) * 100;
        model.addAttribute("mostProfitableName", mostProfitableOverall.name());
        model.addAttribute("mostProfitableChange",
                "$" + dc.format(mostProfitAmount) + " or " + dc.format(mostProfitPercent) + "%");
        model.addAttribute("isMostProfitablePositive", mostProfitAmount > 0);

        double leastProfitAmount = leastProfitableOverall.current() - leastProfitableOverall.old();
        double leastProfitPercent = (leastProfitAmount / leastProfitableOverall.old()) * 100;
        model.addAttribute("leastProfitableName", leastProfitableOverall.name());
        model.addAttribute("leastProfitableChange",
                "$" + dc.format(leastProfitAmount) + " or " + dc.format(leastProfitPercent) + "%");
        model.addAttribute("isLeastProfitablePositive", leastProfitAmount > 0);

        double mostProfitAmountToday = mostProfitableToday.current() - mostProfitableToday.old();
        double mostProfitPercentToday = (mostProfitAmountToday / mostProfitableToday.old()) * 100;
        model.addAttribute("mostProfitableTodayName", mostProfitableToday.name());
        model.addAttribute("mostProfitableTodayChange",
                "$" + dc.format(mostProfitAmountToday) + " or " + dc.format(mostProfitPercentToday) + "%");
        model.addAttribute("isMostProfitableTodayPositive", mostProfitAmountToday > 0);

        double leastProfitAmountToday = leastProfitableToday.current() - leastProfitableToday.old();
        double leastProfitPercentToday = (leastProfitAmountToday / leastProfitableToday.old()) * 100;
        model.addAttribute("leastProfitableTodayName", leastProfitableToday.name());
        model.addAttribute("leastProfitableTodayChange",
                "$" + dc.format(leastProfitAmountToday) + " or " + dc.format(leastProfitPercentToday) + "%");
        model.addAttribute("isLeastProfitableTodayPositive", leastProfitPercentToday > 0);

        // display assets info
        model.addAttribute("assets", assetData);
        model.addAttribute("globals",
                String.format("{ balance: %s }", dc.format(acc.getBalance())));
        return "portfolio";
    }

    @GetMapping("/deposit")
    public String onDeposit(@CookieValue(value = "uuid", defaultValue = "") String uuid, @RequestParam String amount,
            Model model) {
        // if not logged in; redirect to login
        if (uuid.equals("") || !AccountManager.isLoggedIn(java.util.UUID.fromString(uuid)))
            return "redirect:/";

        double depositAmount = Double.parseDouble(amount);
        Account acc = AccountManager.getFromUUID(java.util.UUID.fromString(uuid));
        acc.deposit(depositAmount);

        return "redirect:/portfolio";
    }

    @GetMapping("/quick")
    public String onQuickAct(@CookieValue(value = "uuid", defaultValue = "") String uuid, @RequestParam String action,
            @RequestParam String ticker,
            @RequestParam String amount, Model model) {
        // if not logged in; redirect to login
        if (uuid.equals("") || !AccountManager.isLoggedIn(java.util.UUID.fromString(uuid)))
            return "redirect:/";

        Account acc = AccountManager.getFromUUID(java.util.UUID.fromString(uuid));
        int amountInt = Integer.parseInt(amount);

        if (action.equals("buy")) {
            try {
                acc.buyAsset(ticker, amountInt);
            } catch (Exception e) {
                // Handle properly later... frontend dis-allows erroneus inputs anyways.
                System.out.println(e);
            }
        } else if (action.equals("sell")) {
            try {
                acc.sellAsset(ticker, amountInt);
            } catch (Exception e) {
                // Handle properly later... frontend dis-allows erroneus inputs anyways.
                System.out.println(e);
            }
        }

        return "redirect:/portfolio";
    }
}

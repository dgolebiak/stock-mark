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
import stockmark.stockmark.model.Share;
import stockmark.stockmark.model.Exceptions.*;

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

        double totalValue = acc.getBalance();

        String mostProfitableName = "...";
        double mostProfitableChange = Double.MIN_VALUE;

        String leastProfitableName = "...";
        double leastProfitableChange = Double.MAX_VALUE;

        int i = 0;
        for (String ticker : assets.keySet()) {
            try {
                double currentPrice = Market.getInstance().getPrice(ticker);
                double currentPcChange = Market.getInstance().getPercentChangeToday(ticker);
                Share myShare = assets.get(ticker);
                double myShareWorthToday = (double) myShare.amount() * currentPrice;
                double howMuchIpaidForIt = (double) myShare.amount() * myShare.buyPrice();
                totalValue += myShareWorthToday;

                // calc most profitable
                if (myShareWorthToday - howMuchIpaidForIt > mostProfitableChange) {
                    mostProfitableChange = myShareWorthToday - howMuchIpaidForIt;
                    mostProfitableName = ticker;
                }

                // calc least profitable
                if (myShareWorthToday - howMuchIpaidForIt < leastProfitableChange) {
                    leastProfitableChange = myShareWorthToday - howMuchIpaidForIt;
                    leastProfitableName = ticker;
                }

                // prep x-data
                // example: x-data="{ ticker: 'TSLA', amount: 4, totalValue: 640, pcChange: 4.2
                assetData[i] = String.format("{ ticker: '%s', amount: %d, totalValue: %d, pcChange: %s }", ticker,
                        myShare.amount(), (int) myShareWorthToday, dc.format(currentPcChange));
                i++;
            } catch (NonExistentTickerException e) {
                System.out.println("Non existent ticker, but data came from database so internal error...");
            }
        }

        double totalChange = totalValue - acc.getDeposited();

        // if they are empty then reset the change values
        if (mostProfitableName.equals("..."))
            mostProfitableChange = 0;
        if (leastProfitableName.equals("..."))
            leastProfitableChange = 0;

        // render portfolio template
        model.addAttribute("currentBalance", "$" + (int) acc.getBalance());
        model.addAttribute("deposited", "$" + (int) acc.getDeposited());

        model.addAttribute("assets", acc.getAssets());
        model.addAttribute("totalValue", "$" + (int) totalValue);
        model.addAttribute("totalChange", "$" + Math.abs((int) totalChange));
        model.addAttribute("isTotalChangePositive", totalChange > 0);

        model.addAttribute("mostProfitableName", mostProfitableName);
        model.addAttribute("mostProfitableChange", "$" + Math.abs((int) mostProfitableChange));
        model.addAttribute("isMostProfitablePositive", mostProfitableChange > 0);

        model.addAttribute("leastProfitableName", leastProfitableName);
        model.addAttribute("leastProfitableChange", "$" + Math.abs((int) leastProfitableChange));
        model.addAttribute("isLeastProfitablePositive", leastProfitableChange > 0);

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

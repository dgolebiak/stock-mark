package stockmark.stockmark.web;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import stockmark.stockmark.model.Account;
import stockmark.stockmark.model.AccountManager;
import stockmark.stockmark.model.Market;
import stockmark.stockmark.model.Share;
import stockmark.stockmark.model.Exceptions.*;

// @CookieValue(value = "uuid", defaultValue = "") String uuid
@Controller
public class PortfolioController {
    @GetMapping("/portfolio")
    public String onGetPortfolio(@CookieValue(value = "uuid", defaultValue = "") String uuid, Model model) {
        // if not logged in; redirect to login
        if (uuid.equals("") || !AccountManager.isLoggedIn(java.util.UUID.fromString(uuid)))
            return "redirect:/";

        return renderPortfolio(uuid, model);
    }

    public String renderPortfolio(String uuid, Model model) {
        Account acc = AccountManager.getFromUUID(java.util.UUID.fromString(uuid));

        var assets = acc.getAssets();

        double totalValue = acc.getBalance();

        String mostProfitableName = "...";
        double mostProfitableChange = Double.MIN_VALUE;

        String leastProfitableName = "...";
        double leastProfitableChange = Double.MAX_VALUE;
        for (String ticker : assets.keySet()) {
            try {
                double currentPrice = Market.getInstance().getPrice(ticker);
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
        model.addAttribute("assets", acc.getAssets());
        model.addAttribute("totalValue", "$" + (int) totalValue);
        model.addAttribute("totalChange", "$" + Math.abs((int) totalChange));
        model.addAttribute("isTotalChangePositive", totalValue > 0);

        model.addAttribute("mostProfitableName", mostProfitableName);
        model.addAttribute("mostProfitableChange", "$" + Math.abs((int) mostProfitableChange));
        model.addAttribute("isMostProfitablePositive", mostProfitableChange > 0);

        model.addAttribute("leastProfitableName", leastProfitableName);
        model.addAttribute("leastProfitableChange", "$" + Math.abs((int) leastProfitableChange));
        model.addAttribute("isLeastProfitablePositive", leastProfitableChange > 0);
        return "portfolio";
    }

    @GetMapping("/deposit")
    public String onDeposit(@CookieValue(value = "uuid", defaultValue = "") String uuid, @RequestParam String amount,
            Model model) {
        // if not logged in; redirect to login
        if (uuid.equals("") || !AccountManager.isLoggedIn(java.util.UUID.fromString(uuid)))
            return "redirect:/";

        try {
            double depositAmount = Double.parseDouble(amount);
            Account acc = AccountManager.getFromUUID(java.util.UUID.fromString(uuid));
            acc.deposit(depositAmount);
        } catch (Exception e) {
            // do nothing
        }

        return "redirect:/portfolio";
    }
}

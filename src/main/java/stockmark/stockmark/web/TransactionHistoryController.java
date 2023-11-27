package stockmark.stockmark.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import stockmark.stockmark.model.Account;
import stockmark.stockmark.model.AccountManager;

@Controller
public class TransactionHistoryController {
    @GetMapping("/transactions")
    public String onGetTransactions(@CookieValue(value = "uuid", defaultValue = "") String uuid, Model model) {
        // if not logged in; redirect to login
        if (uuid.equals("") || !AccountManager.isLoggedIn(java.util.UUID.fromString(uuid)))
            return "redirect:/";

        return renderTransactions(uuid, model);
    }

    public String renderTransactions(String uuid, Model model) {
        Account acc = AccountManager.getFromUUID(java.util.UUID.fromString(uuid));

        var history = acc.getHistory();

        model.addAttribute("history", history);
        
        return "transactions";
    }
}

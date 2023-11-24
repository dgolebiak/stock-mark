package stockmark.stockmark.web;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import stockmark.stockmark.model.Account;
import stockmark.stockmark.model.AccountManager;

// @CookieValue(value = "uuid", defaultValue = "") String uuid
@Controller
public class PortfolioController {
    @GetMapping("/portfolio")
    public String onGetPortfolio(@CookieValue(value = "uuid", defaultValue = "") String uuid, Model model) {
        // if not logged in; redirect to login
        if (uuid.equals("") || !AccountManager.isLoggedIn(java.util.UUID.fromString(uuid)))
            return "redirect:/";

        Account acc = AccountManager.getFromUUID(java.util.UUID.fromString(uuid));

        // not complete
        model.addAttribute("assets", acc.getAssets());
        // render portfolio template
        return "portfolio";
    }
}

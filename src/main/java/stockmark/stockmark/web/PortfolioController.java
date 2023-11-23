package stockmark.stockmark.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import stockmark.stockmark.model.AccountManager;

// @CookieValue(value = "uuid", defaultValue = "") String uuid
@Controller
public class PortfolioController {
    @GetMapping("/portfolio")
    public String onGetPortfolio(@CookieValue(value = "uuid", defaultValue = "") String uuid) {
        if (uuid.equals("") || !AccountManager.isLoggedIn(java.util.UUID.fromString(uuid)))
            return "redirect:/";

        return "portfolio";
    }
}

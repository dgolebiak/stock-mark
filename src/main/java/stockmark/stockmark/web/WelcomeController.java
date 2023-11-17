package stockmark.stockmark.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import stockmark.stockmark.model.AccountManager;
import stockmark.stockmark.model.Exceptions.*;

@Controller
public class WelcomeController {
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        try {
            AccountManager.loginAccount(email, password);
            return "portfolio";
        } catch (AccountNotFoundException e) {
            model.addAttribute("email", "");
            model.addAttribute("errmsg", "Account with this email does not exist!");
        } catch (IncorrectPasswordException e) {
            model.addAttribute("email", email);
            model.addAttribute("errmsg", "Incorrect password, please try again!");
        }

        model.addAttribute("showError", "true");
        // return index template
        return "index";
    }
}

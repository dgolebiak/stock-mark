package stockmark.stockmark.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import stockmark.stockmark.model.Account;
import stockmark.stockmark.model.AccountManager;
import stockmark.stockmark.model.Exceptions.*;

@Controller
public class WelcomeController {
    @GetMapping("/register")
    public String onGetRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String onRegisterFormSubmit(@RequestParam String fname, @RequestParam String lname,
        @RequestParam String email, @RequestParam String password, Model model) {

        try {
            AccountManager.registerAccount(new Account(fname+" "+lname, email.toLowerCase(), password));
            return "portfolio";
        } catch (AccountExistsException e) {
            model.addAttribute("errorMessage", "Account with this email already exists!");
        }

        return "register";
    }

    @GetMapping("/")
    public String onGetIndex() {
        return "index";
    }

    @GetMapping("/login")
    public String onGetLogin() {
        return "index";
    }

    @PostMapping("/login")
    public String onLoginFormSubmit(@RequestParam String email, @RequestParam String password, Model model) {
        try {
            AccountManager.loginAccount(email.toLowerCase(), password);
            return "portfolio";
        } catch (AccountNotFoundException e) {
            model.addAttribute("errorMessage", "Account with this email does not exist!");
        } catch (IncorrectPasswordException e) {
            model.addAttribute("email", email);
            model.addAttribute("errorMessage", "Incorrect password, please try again!");
        }
        return "index";
    }
}

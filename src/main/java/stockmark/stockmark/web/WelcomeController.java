package stockmark.stockmark.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import stockmark.stockmark.model.AccountManager;
import stockmark.stockmark.model.Exceptions.*;

@RestController
public class WelcomeController {
    @GetMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        try {
            AccountManager.loginAccount(email, password);
            return "<h1>Login Successful!</h1>";
        } catch (AccountNotFoundException e) {
            System.out.println(e);
        } catch (IncorrectPasswordException e) {
            System.out.println(e);
        }
        return "<h1>Login Failed!</h1>";
    }
}

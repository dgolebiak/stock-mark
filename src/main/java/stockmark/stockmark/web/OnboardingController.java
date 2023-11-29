package stockmark.stockmark.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import stockmark.stockmark.model.Account;
import stockmark.stockmark.model.AccountManager;
import stockmark.stockmark.model.Exceptions.*;

import java.util.UUID;

@Controller
public class OnboardingController {
    @GetMapping("/register")
    public String onGetRegister(@CookieValue(value = "uuid", defaultValue = "") String uuid) {
        // if already logged in; redirect to portfolio
        if (!uuid.equals("") && AccountManager.isLoggedIn(java.util.UUID.fromString(uuid)))
            return "redirect:/portfolio";
        // render register template
        return "register";
    }

    @PostMapping("/register")
    public String onRegisterFormSubmit(@RequestParam String fname, @RequestParam String lname,
            @RequestParam String email, @RequestParam String password, Model model, HttpServletResponse response) {

        try {
            UUID id = AccountManager.registerAccount(new Account(fname + " " + lname, email.toLowerCase(), password));
            Cookie cookie = new Cookie("uuid", id.toString());
            response.addCookie(cookie);
            return "redirect:/portfolio";
        } catch (AccountExistsException e) {
            model.addAttribute("errorMessage", "Account with this email already exists!");
        }

        return "register";
    }

    @GetMapping("/")
    public String onGetIndex(@CookieValue(value = "uuid", defaultValue = "") String uuid) {
        // if already logged in; redirect to portfolio
        if (!uuid.equals("") && AccountManager.isLoggedIn(java.util.UUID.fromString(uuid)))
            return "redirect:/portfolio";
        // render login template
        return "login";
    }

    @PostMapping("/login")
    public String onLoginFormSubmit(@RequestParam String email, @RequestParam String password, Model model,
            HttpServletResponse response) {
        try {
            UUID id = AccountManager.loginAccount(email.toLowerCase(), password);
            Cookie cookie = new Cookie("uuid", id.toString());
            response.addCookie(cookie);
            return "redirect:/portfolio";
        } catch (AccountNotFoundException e) {
            model.addAttribute("errorMessage", "Account with this email does not exist!");
        } catch (IncorrectPasswordException e) {
            model.addAttribute("email", email);
            model.addAttribute("errorMessage", "Incorrect password, please try again!");
        }
        return "login";
    }

    @GetMapping("/logout")
    public String onLogout(@CookieValue(value = "uuid", defaultValue = "") String uuid) {
        if (!uuid.equals("")) AccountManager.logoutAccount(java.util.UUID.fromString(uuid));
        // render login template
        return "redirect:/";
    }
}

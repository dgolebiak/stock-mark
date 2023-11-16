package stockmark.stockmark.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import stockmark.stockmark.model.AccountManager;
import stockmark.stockmark.model.Exceptions.*;

@Controller
public class Test {
    @GetMapping("/test")
    public String login() {
        return "index";
    }
}

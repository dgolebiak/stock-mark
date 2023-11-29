package stockmark.stockmark.web;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import stockmark.stockmark.model.Transaction;
import stockmark.stockmark.model.Account;
import stockmark.stockmark.model.AccountManager;

@Controller
public class TransactionHistoryController {
    @GetMapping("/transactions")
    public String onGetTransactions(@CookieValue(value = "uuid", defaultValue = "") String uuid, Model model, Pageable pageable) {
        // if not logged in; redirect to login
        if (uuid.equals("") || !AccountManager.isLoggedIn(java.util.UUID.fromString(uuid)))
            return "redirect:/";

        return renderTransactions(uuid, model, pageable);
    }

    public String renderTransactions(String uuid, Model model, Pageable pageable) {
        Account acc = AccountManager.getFromUUID(java.util.UUID.fromString(uuid));

        Page<Transaction> historyPage = acc.getHistoryPage(pageable); 
        
        List<Integer> pagesList = new ArrayList<>();

        for (int i = 0; i < historyPage.getTotalPages(); i++) {
            pagesList.add(i);
        }


        model.addAttribute("history", historyPage.getContent());
        model.addAttribute("currentPage", historyPage.getNumber());
        model.addAttribute("pagesList", pagesList);
        
        return "transactions";
    }
}

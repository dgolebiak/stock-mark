package stockmark.stockmark.web;

import org.springframework.ui.Model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collection;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import stockmark.stockmark.model.Account;
import stockmark.stockmark.model.AccountManager;
import stockmark.stockmark.model.PrivateGame;
import stockmark.stockmark.model.PrivateGameManager;


record ClientAsset(String ticker, int totalValue, double pcChange) {
}

// @CookieValue(value = "uuid", defaultValue = "") String uuid
@Controller
public class PrivateGameController {
    String[] gamesList;
    @GetMapping("/privateGame")
    public String onGetPrivateGame(@CookieValue(value = "uuid", defaultValue = "") String uuid, Model model) {
        // if not logged in; redirect to login
        if (uuid.equals("") || !AccountManager.isLoggedIn(java.util.UUID.fromString(uuid)))
            return "redirect:/";
        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
        decimalFormatSymbols.setDecimalSeparator('.');
        var dc = new DecimalFormat("0.00", decimalFormatSymbols);

        Account acc = AccountManager.getFromUUID(java.util.UUID.fromString(uuid));

        Collection<PrivateGame> games = PrivateGameManager.getPrivateGames();
        gamesList = new String[games.size()];

        int i = 0;
        for (PrivateGame game : games){
            gamesList[i++] = String.format("{ gameName: '%s'}", 
                    game.getGameName()
                    ); 
            }

        model.addAttribute("privateGames", gamesList);
        model.addAttribute("globals",
            String.format("{ balance: %s }", dc.format(acc.getBalance())));
        return "privateGame";
    }
}

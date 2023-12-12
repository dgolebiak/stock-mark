package stockmark.stockmark.web;

import org.springframework.ui.Model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import stockmark.stockmark.model.Account;
import stockmark.stockmark.model.AccountManager;
import stockmark.stockmark.model.PrivateGame;
import stockmark.stockmark.model.PrivateGameManager;
import stockmark.stockmark.model.Exceptions.*;


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
            gamesList[i++] = String.format("{ gameName: '%s', budget: '%s', isPlayerInGame: %b }", 
                    game.getGameName(),
                    dc.format(game.getGameBudget()),
                    game.doesPlayerExist(acc.getName())
                    );
                    
            System.out.println("Name: " + game.getGameName() + " Budget: " + game.getGameBudget());
            System.out.println("Name: " + game.getGameName() + " Budget: " + dc.format(game.getGameBudget()));
            }

        model.addAttribute("privateGames", gamesList);
        model.addAttribute("globals",
            String.format("{ balance: %s }", dc.format(acc.getBalance())));
        return "privateGame";
    }

        @GetMapping("/createGame")
    public String onCreateGame(@CookieValue(value = "uuid", defaultValue = "") String uuid, 
            @RequestParam String gameName, 
            @RequestParam double budget,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate, 
            Model model) {
        // if not logged in; redirect to login
        if (uuid.equals("") || !AccountManager.isLoggedIn(java.util.UUID.fromString(uuid)))
            return "redirect:/";

        Account acc = AccountManager.getFromUUID(java.util.UUID.fromString(uuid));

        // Parse date strings into Instant (Unix timestamp)
        Instant startInstant = Instant.parse(startDate + "T00:00:00Z");
        Instant endInstant = Instant.parse(endDate + "T00:00:00Z");

        // Get Unix timestamps (seconds since the epoch)
        long startUnixTimestamp = startInstant.getEpochSecond();
        long endUnixTimestamp = endInstant.getEpochSecond();

        try{
        PrivateGameManager.createGame(new PrivateGame(gameName, acc, budget, startUnixTimestamp, endUnixTimestamp));
        }catch (GameExistsException e){
            model.addAttribute("errorMessage", "A Game with this name already exists!");
        }

        
        return "redirect:/privateGame";
    }

            @GetMapping("/joinGame")
    public String onJoinGame(@CookieValue(value = "uuid", defaultValue = "") String uuid,
            @RequestParam String gameViewGameName,
            Model model) {
        // if not logged in; redirect to login
        if (uuid.equals("") || !AccountManager.isLoggedIn(java.util.UUID.fromString(uuid)))
            return "redirect:/";

        Account acc = AccountManager.getFromUUID(java.util.UUID.fromString(uuid));
        PrivateGame game = PrivateGameManager.getGame(gameViewGameName);
        try{
        acc.joinGame(gameViewGameName);
        game.addPlayer(acc.getName());
        }catch (PlayerAlreadyInGameException e){
            model.addAttribute("errorMessage", "You are already in this game!");

        }
        
        return "redirect:/privateGame";
    }
    
}

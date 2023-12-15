package stockmark.stockmark.web;

import stockmark.stockmark.model.Account;
import stockmark.stockmark.model.AccountManager;
import stockmark.stockmark.model.Exceptions.IncorrectPasswordException;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;



@Controller
public class ProfileController {
    @GetMapping("/myprofile")
        public String onGetProfileInfo(@CookieValue(value = "uuid", defaultValue = "") String uuid, Model model, Pageable pageable) {
        // if not logged in; redirect to login
        if (uuid.equals("") || !AccountManager.isLoggedIn(java.util.UUID.fromString(uuid)))
            return "redirect:/";
        
        Account acc = AccountManager.getFromUUID(java.util.UUID.fromString(uuid));
        model.addAttribute("account", acc);
        
        return "myprofile";
    }
     @PostMapping("/changePassword")
     public String onChangePassword(@RequestParam String oldPassword, @RequestParam String newPassword, 
                                    @RequestParam String confNewPassword , Model model, HttpServletResponse response,
                                    @CookieValue(value = "uuid", defaultValue = "") String uuid){
        if (uuid.equals("") || !AccountManager.isLoggedIn(java.util.UUID.fromString(uuid)))
            return "redirect:/";
        
        Account acc = AccountManager.getFromUUID(java.util.UUID.fromString(uuid));
        model.addAttribute("account", acc);
        
        try{
            acc.changePassword(oldPassword, newPassword, confNewPassword);
            model.addAttribute("successMessage", "Password succefully changed! ");
            return "myprofile";
        }catch(IncorrectPasswordException e){
            model.addAttribute("errorMessage", "Incorrect old password, please try again!");
            return "myprofile";
        }
        
     }
}  
        

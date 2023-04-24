package backend.bankwebapp.controller;


import backend.bankwebapp.model.User;
import backend.bankwebapp.model.UserRepository;
import backend.bankwebapp.service.AppService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
//@RequestMapping("/api") // prefix need to add to the WebSecurityConfig.java
public class AppController {

    Boolean successAction = true;

    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/list_users")
    public String viewUsersList(Model model) throws IOException {
//        List<User> listUsers = repo.findAll();
        List<User> listUsers = UserRepository.findAll();
        model.addAttribute("listUsers", listUsers);
        return "users";
    }

    @GetMapping("/singleAccount")
    public String viewUsersList(Model model, Authentication authentication) throws IOException {
        String email = authentication.getName();
        // use the email parameter to filter the list of users
        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user);
        return "singleAccount";
    }

    //
    // FORM
    //

    @GetMapping("/myForm")
    public String myForm(Model model, Authentication authentication) {
        String email = authentication.getName();
        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user);
//        model.addAttribute("success",false);
//        model.addAttribute("message","aaa");
        model.addAttribute("show", false);
        return "myForm";
    }

    //
    // Handler for refresh Request
    //
    /**
     * Called after any form button handler is used -> like refresh of page
     * @param model - model for html page variables
     * @param authentication - for find user
     * @return - return same page - myForm
     */
    @RequestMapping(value={"/deposit", "/payment", "/open", "/close"})
    public String myFormDeposit(Model model, Authentication authentication) {
        String email = authentication.getName();
        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user);
//        model.addAttribute("success",false);
//        model.addAttribute("message","aaa");
        model.addAttribute("show", true);
        model.addAttribute("success", successAction);
        model.addAttribute("message", "/deposit");
        return "myForm";
    }

    //
    // DEPOSIT --------------------------------------------------------------------------------------
    //

    @PostMapping("/deposit")
    public String handleDeposit(@RequestParam("accountType") String accountType, @RequestParam("amount") int amount, Model model, Authentication authentication) {
        String message = "Deposit successful!";
        String email = authentication.getName();

        // Perform the open action
        try {
            // if accountType exist on user account
            if(AppService.hasTheAccountOfType(email, accountType)) {
                // add amount of finance to the account of accountType
//                AppService.writeToJsonFile(email, accountType, Integer.toString(amount));
                AppService.addMoneyToAccount(email, accountType, amount);
                successAction = true;
            } else {
                successAction = false;
                message = "Deposit failed! - Account " + accountType + " does not exist";
            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
            successAction = false;
//            throw new RuntimeException(e);
        }

        // Add the success or error message to the model
        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user); // for the html page variables !

        model.addAttribute("show", true);
        model.addAttribute("success", successAction);
        model.addAttribute("message", message);

        // Return the name of the view to render
        // return the SAME html page !
        return "myForm";
    }


    //
    // OPEN ------------------------------------------------------------------------------------------
    //

    @PostMapping("/open")
    public String handleOpen(@RequestParam("accountType") String accountType, @RequestParam("amount") int amount, Model model, Authentication authentication) {
        String message = "Account opened successfully!";
        String email = authentication.getName();

        // Perform the open action
        try {
            // if accountType NOT exist on user account accounts
            if(!AppService.hasTheAccountOfType(email, accountType)) {
                // add account of given accountType with given amount of finance
                AppService.writeToJsonFile(email, accountType, Integer.toString(amount));
                successAction = true;
            } else {
                successAction = false;
                message = "Open account failed! - User already has money account of given type (" + accountType + ")";
            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
            successAction = false;
//            throw new RuntimeException(e);
        }

        // Add the success or error message to the model
        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user); // for the html page variables !

        model.addAttribute("show", true);
        model.addAttribute("success", successAction);
        model.addAttribute("message", message);

        // Return the name of the view to render
        // return the SAME html page !
        return "myForm";
    }

}

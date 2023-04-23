package backend.bankwebapp.controller;


import backend.bankwebapp.model.User;
import backend.bankwebapp.model.UserRepository;
import backend.bankwebapp.service.AppService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
//@RequestMapping("/api") // prefix need to add to the WebSecurityConfig.java
public class AppController {

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

    @GetMapping("/tryme")
    public String tryMe(Model model, Authentication authentication) {
        String email = authentication.getName();
        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user);
        return "try";
    }

    //
    //
    //
//    @RequestMapping(value = "/addEmployee", method = RequestMethod.POST, params = "submit")
//    public String submit(@ModelAttribute("user") final User user,
//                         final BindingResult result, final ModelMap model) {
//        // same code as before
//    }
//    @RequestMapping(value = "/addEmployee", method = RequestMethod.POST, params = "cancel")
//    public void cancel(@ModelAttribute("user") final User  user,
//                       final BindingResult result, final ModelMap model) {
//        model.addAttribute("message", "You clicked cancel, please re-enter user details:");
////        return "employeeHome";
//    }

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

    @GetMapping("/deposit")
    public String myFormDeposit(Model model, Authentication authentication) {
        String email = authentication.getName();
        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user);
//        model.addAttribute("success",false);
//        model.addAttribute("message","aaa");
        model.addAttribute("show", true);
        model.addAttribute("success", true);
        model.addAttribute("message", "/deposit");
//        model.addAttribute("show", false);
        return "myForm";
    }

    @PostMapping("/deposit")
    public String handleDeposit(@RequestParam("accountType") String accountType, @RequestParam("amount") int amount, Model model, Authentication authentication) {
        boolean success = true;
        String message = "Deposit successful!";

        System.out.println("handleDeposit() - success");

        //
        String email = authentication.getName();
        //

        // Perform the deposit action
//        String email = authentication.getName();
        try {
            // if accountType NOT exist on user account accounts
            if(!AppService.hasTheAccountOfType(email, accountType)) {
                AppService.writeToJsonFile(email, accountType, Integer.toString(amount));
            } else {
                success = false;
                message = "Deposit failed! - User already has money account of given type (" + accountType + ")";
            }
        } catch (IOException e) {
//            throw new RuntimeException(e);
            System.out.println("Error: " + e);
            success = false;
        }

//        if (!success) {
//            // Set the error message and flag
//            message = "Deposit failed!";
//            success = false;
//        }

        // Add the depositParams array to the model for display in the view
//        model.addAttribute("depositParams", depositParams);

        // Add the success or error message to the model
        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user); // for the html page variables !

        model.addAttribute("show", true);
        model.addAttribute("success", success);
        model.addAttribute("message", message);

        // Return the name of the view to render
        // return the SAME html page !
        return "myForm";
    }

    @PostMapping("/deposit2")
    public String handleDeposit2(@RequestParam("accountType") String accountType, @RequestParam("amount") int amount, Model model, Authentication authentication) {
        model.addAttribute("message", "ahoj");
        return "blank";
    }

}

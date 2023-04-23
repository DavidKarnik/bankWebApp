package backend.bankwebapp.controller;


import backend.bankwebapp.model.User;
import backend.bankwebapp.model.UserRepository;
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

    @PostMapping("/deposit")
    public String handleDeposit(@RequestParam String accountType, @RequestParam int amount, Model model) {
        boolean success = true;
        String message = "Deposit successful!";

        // Perform the deposit action here
        // ...

        if (!success) {
            // Set the error message and flag
            message = "Deposit failed!";
            success = false;
        }

        // Save the accountType and amount parameters in an array
        Object[] depositParams = { accountType, amount };

        // Add the depositParams array to the model for display in the view
        model.addAttribute("depositParams", depositParams);

        // Add the success or error message to the model
        model.addAttribute("success", success);
        model.addAttribute("message", message);

        // Return the name of the view to render
        // return the SAME html page !
        return "myForm";
    }

}

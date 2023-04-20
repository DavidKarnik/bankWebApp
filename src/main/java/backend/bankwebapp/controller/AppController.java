package backend.bankwebapp.controller;


import backend.bankwebapp.model.User;
import backend.bankwebapp.model.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Controller
//@RequestMapping("/api") // prefix need to add to the WebSecurityConfig.java
public class AppController {

    @GetMapping("")
    public String viewHomePage(){
        return "index";
    }

    @GetMapping("/list_users")
    public String viewUsersList(Model model) throws IOException {
//        List<User> listUsers = repo.findAll();
        List<User> listUsers = UserRepository.findAll();
        model.addAttribute("listUsers", listUsers);
        return "users";
    }

    @GetMapping("/try")
    public String viewTry() throws IOException {
        return "try";
    }

    @GetMapping("/singleAccount")
    public String viewUsersList(Model model, Authentication authentication) throws IOException {
        String email = authentication.getName();
        // use the email parameter to filter the list of users
        User user = UserRepository.findByEmail(email);
        model.addAttribute("user", user);
        return "singleAccount";
    }

}

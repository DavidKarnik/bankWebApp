package backend.bankwebapp.controller;


import backend.bankwebapp.model.User;
import backend.bankwebapp.model.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;

@Controller
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
}

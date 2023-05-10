package backend.bankwebapp.controller;

import backend.bankwebapp.mail.MyMailService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static backend.bankwebapp.service.VerificationService.*;

@Controller
public class VerificationController {
    
    @Autowired
    private MyMailService mailService;

    @GetMapping("/verify")
    public String verify(Authentication authentication) {
        // load verify page
        // send email with verification code
        // refresh is ENEMY :(

        String email = authentication.getName();
        // Generate a verification code
        String code = generateCode();

        // TODO edit for real given email !!!
        mailService.sendEmail("david.karnik@seznam.cz", code);
        // Redirect the user to the verification page
        return "verify";
    }
    @PostMapping("/verify")
    public String verifyP() {
        // when Verify button is clicked
        return "index";
    }

    @PostMapping("/sendCode")
    public String sendCode(@RequestParam String email) {
        // Generate a verification code
        String code = generateCode();

        return "/verify";
    }

    @PostMapping("/verifyCode")
    public String verifyCode(@RequestParam("email") String email,
                             @RequestParam("code") String code,
                             Model model) throws IOException {
        // Load user data from the JSON file
        String jsonString = new String(Files.readAllBytes(Paths.get("src/main/resources/verification.json")));
        JSONObject userData = new JSONObject(jsonString);
        JSONArray users = userData.getJSONArray("users");

        // Search for the user with the specified email address
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("email").equals(email)) {
                // Retrieve the verification code and compare it with the entered code
                String storedCode = user.getString("verificationCode");
                if (code.equals(storedCode)) {
                }
            }
        }
        return "index";
    }
}

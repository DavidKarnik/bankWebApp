package backend.bankwebapp.controller;

import backend.bankwebapp.mail.MyMailService;
import backend.bankwebapp.model.User;
import backend.bankwebapp.model.UserRepository;
import backend.bankwebapp.service.VerificationService;
import jakarta.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class VerificationController {

    @Autowired
    private MyMailService mailService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/verify")
    public String verify(Authentication authentication, HttpSession session) throws IOException {
//        String email = authentication.getName();

        // Check if the verification email has already been sent for this user
        Boolean emailSent = (Boolean) session.getAttribute("emailSent");
        if (emailSent != null && emailSent) {
            // Verification email has already been sent, so skip sending it again (when refresh)
            return "verify";
        }

        // Generate a verification code
//        String code = verificationService.generateCode();
//        // save code to verification.json
//        verificationService.addVerificationCodeToUserByEmail(email, code);
//
//        // TODO edit for real given email !!!
//        mailService.sendEmail("david.karnik@seznam.cz", code);

        // Set the emailSent flag in the session to indicate that the verification email has been sent
        session.setAttribute("emailSent", true);

        // Redirect the user to the verification page
        return "verify";
    }


    @PostMapping("/verify")
    public String verifyCode(@RequestParam("code") String code, RedirectAttributes redirectAttrs, Model model) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = authentication.getName();
        System.out.println(code);
        System.out.println(email);
        if (verificationService.isCodeValid(email, code)) {
            System.out.println("Verified Code - Success !!");
            verificationService.deleteVerificationCodeToUserByEmail(email);
            // Redirect to method2 and pass the HTTP method
            redirectAttrs.addAttribute("method", "GET");
            return "redirect:/myForm";
        }

        // Add an error message to the model
        model.addAttribute("errorMessage", "Wrong Authentication Code");
        return "verify"; // wrong verification code
    }
}

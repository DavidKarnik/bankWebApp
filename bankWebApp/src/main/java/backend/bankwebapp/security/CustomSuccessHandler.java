package backend.bankwebapp.security;

import java.io.IOException;

import backend.bankwebapp.mail.MyMailService;
import backend.bankwebapp.service.VerificationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private MyMailService mailService;

    @Autowired
    private VerificationService verificationService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        String email = authentication.getName();
        // Generate a verification code
        String code = verificationService.generateCode();
        // save code to verification.json
        verificationService.addVerificationCodeToUserByEmail(email, code);
        mailService.sendEmail(email, code);

        String redirectUrl="/verify";
        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
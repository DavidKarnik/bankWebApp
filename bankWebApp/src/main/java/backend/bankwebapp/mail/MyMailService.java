package backend.bankwebapp.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MyMailService {

        @Autowired
        JavaMailSender mailSender;

        public void sendEmail(String mail, String code) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("mail.sender.dk@gmail.com");
            message.setTo(mail);
            message.setSubject("Verification code");
            message.setText("The verification code for you bank is: " + code);
            mailSender.send(message);
        }
    }
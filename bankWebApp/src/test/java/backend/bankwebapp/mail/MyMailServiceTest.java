package backend.bankwebapp.mail;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MyMailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    // @InjectMocks -> to mark a field on which an injection is to be performed
    @InjectMocks
    private MyMailService myMailService;

    @Test
    public void testSendEmail() {
        // given
        String email = "test@example.com";
        String code = "123456";

        // when
        myMailService.sendEmail(email, code); // Call the method under test

        // then
        verify(mailSender).send(any(SimpleMailMessage.class)); // Verify the interaction
    }
}


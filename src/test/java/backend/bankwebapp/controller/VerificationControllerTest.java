package backend.bankwebapp.controller;

import backend.bankwebapp.mail.MyMailService;
import backend.bankwebapp.model.UserRepository;
import backend.bankwebapp.security.TestWebSecurityConfig;
import backend.bankwebapp.service.VerificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = TestWebSecurityConfig.class)
@WebMvcTest(VerificationController.class)
public class VerificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MyMailService mailService;

    @MockBean
    private VerificationService verificationService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private UserRepository userRepo;

    @MockBean
    private Authentication authentication;

    @MockBean
    private MockHttpSession session;

    @MockBean
    private Model model;

    @MockBean
    private RedirectAttributes redirectAttrs;

    private VerificationController underTest;

    @BeforeEach
    void setUp() {
        underTest = new VerificationController(verificationService);
        // Create a mock session
        session = new MockHttpSession();
    }

    @Test
    public void itShouldChangeSessionAttributeWhenGetMappingVerify() throws Exception {
        // Create a mock session
//        MockHttpSession session = new MockHttpSession();

        // Set up the MockMvc instance
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(underTest)
                .setViewResolvers(new InternalResourceViewResolver("/WEB-INF/views/", ".jsp"))
                .build();

        // Perform the GET request to /verify
        mockMvc.perform(get("/verify").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("verify"));

        // Verify that the session attribute "emailSent" is set to true
        Boolean emailSent = (Boolean) session.getAttribute("emailSent");
        assertThat(emailSent).isTrue();
    }


    @Test
    void testVerifyCode_ValidCode() throws IOException {
        // given
        String email = "admin@mail.com";
        String code = "xxxxx"; // 59593fe7-783e

        when(verificationService.isCodeValid(eq(email), eq(code))).thenReturn(true);

        Authentication authentication = new TestingAuthenticationToken(email, null, "ROLE_ADMIN");
        // Set the authentication context
        SecurityContextHolder.getContext().setAuthentication(authentication);


        // when
        String result = underTest.verifyCode(code, redirectAttrs, model); // @PostMapping("/verify")

        // then
        assertThat(result).isEqualTo("redirect:/myForm");
        verify(verificationService).deleteVerificationCodeToUserByEmail(eq(email));
        verify(redirectAttrs).addAttribute(eq("method"), eq("GET"));
        verify(model, Mockito.never()).addAttribute(eq("errorMessage"), any(String.class));
    }
}

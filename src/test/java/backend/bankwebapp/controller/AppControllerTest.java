package backend.bankwebapp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(AppController.class)
@ExtendWith(MockitoExtension.class)
class AppControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private Model model;

    @InjectMocks
    private AppController underTest;

    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        underTest = new AppController();

        authentication = new TestingAuthenticationToken("admin@mail.com", null, "ROLE_ADMIN");
        SecurityContextHolder.getContext().setAuthentication(authentication); // Set the authentication context

        mockMvc = MockMvcBuilders.standaloneSetup(underTest)
                .setViewResolvers(new InternalResourceViewResolver("/WEB-INF/views/", ".jsp"))
                .build();
    }

    @Test
    void testViewHomePage() {
        // given
        String expectedViewName = "index";

        // when
        String actualViewName = underTest.viewHomePage();

        // then
        assertEquals(expectedViewName, actualViewName);
    }

    @Test
    void itShouldGetMyForm() throws Exception {
        // given
        String expectedViewName = "myForm";

        // when
        MvcResult result = mockMvc.perform(get("/myForm")
                        .requestAttr("model", model)
                        .principal(authentication))
                .andReturn();

        String actualViewName = result.getModelAndView().getViewName();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertEquals(expectedViewName, actualViewName);
    }

    @Test
    void testMyFormPostHandler() throws Exception {
        // given
        String expectedViewName = "myForm";

        // when
        final MvcResult result = mockMvc.perform(post("/myForm")
                        .requestAttr("model", model)
                        .principal(authentication))
                .andReturn();

        String actualViewName = result.getModelAndView().getViewName();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertEquals(expectedViewName, actualViewName);
    }

    @Test
    void itShouldInvokeMyFormDepositAndGetViewMyForm() {
        // given
        String expectedViewName = "myForm";

        // when
        String result = underTest.myFormDeposit(model,authentication);

        // then
        assertThat(result).isEqualTo(expectedViewName);
    }

    @Test
    void itShouldHandleDepositAndGetViewMyForm() {
        // given
        String accountType = "NOK";
        double amount = 0.0;
        String expectedViewName = "myForm";

        // when
        String result = underTest.handleDeposit(accountType, amount, model, authentication);

        // then
        assertEquals(expectedViewName, result);
    }

    @Test
    void itShouldHandlePaymentAndGetViewMyForm() {
        // given
        String accountType = "NOK";
        double amount = 0.0;
        String expectedViewName = "myForm";

        // when
        String result = underTest.handlePayment(accountType, amount, model, authentication);

        // then
        assertEquals(expectedViewName, result);
    }

    @Test
    void itShouldHandleOpenAndGetViewMyForm() throws Exception {
        // given
        String accountType = "NOK";
        double amount = 0.0;
        String expectedViewName = "myForm";

        // when
        String result = underTest.handleOpen(accountType, amount, model, authentication);

        // then
        assertEquals(expectedViewName, result);
    }

    @Test
    void itShouldHandleCloseAndGetViewMyForm() throws Exception {
        // given
        String accountType = "NOK";
        double amount = 0.0;
        String expectedViewName = "myForm";

        // when
        String result = underTest.handleClose(accountType, model, authentication);

        // then
        assertEquals(expectedViewName, result);
    }
}
package backend.bankwebapp.controller;

import backend.bankwebapp.security.TestWebSecurityConfig;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = TestWebSecurityConfig.class)
@WebMvcTest(CustomErrorController.class)
public class CustomErrorControllerTest {

    @MockBean
    private HttpServletRequest request;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHandleErrorNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/error")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND.value()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/error/error404"));
    }

    @Test
    public void testHandleErrorInternalServerError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/error")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/error/error500"));
    }

    @Test
    public void testHandleErrorDefault() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/error"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"));
    }
}

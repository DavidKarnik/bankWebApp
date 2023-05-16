package backend.bankwebapp.service;

import backend.bankwebapp.model.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AppServiceTest {

    private AppService underTest;

//    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        underTest = new AppService();
    }

    @Test
    void itShouldCheckIfUserAccountHasSpecificMoneyAccount() throws IOException {
        // given
        String email = "admin@mail.com";
        String accountType = "USD";
        // when
        boolean expected = AppService.hasTheAccountOfType(email, accountType);
        // then
//        assertThat
    }
}
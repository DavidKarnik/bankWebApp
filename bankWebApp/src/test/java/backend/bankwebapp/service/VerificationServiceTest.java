package backend.bankwebapp.service;

import org.junit.Ignore;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VerificationServiceTest {

    private VerificationService verificationServiceUnderTest;

    // given
    String correctEmail = "admin@mail.com";
    String wrongEmail = "xxxx";
    String code = "123456";

    @BeforeEach
    void setUp() {
        verificationServiceUnderTest = new VerificationService();
    }

    @AfterAll
    static void afterAll() {
    }

    // email:"admin@mail.com", code:"123456"

    @Test
    void itShouldAddVerificationCodeToUserByEmail() throws Exception {
        assertThat(verificationServiceUnderTest.addVerificationCodeToUserByEmail(wrongEmail, code)).isFalse();

        assertThat(verificationServiceUnderTest.addVerificationCodeToUserByEmail(correctEmail, code)).isTrue();
//        assertThatThrownBy(
//                () -> verificationServiceUnderTest.addVerificationCodeToUserByEmail("email", "code"))
//                .isInstanceOf(IOException.class);
    }

    @Test
    void itShouldDeleteVerificationCodeToUserByEmail() throws Exception {
        assertThat(verificationServiceUnderTest.deleteVerificationCodeToUserByEmail(wrongEmail)).isFalse();
        assertThat(verificationServiceUnderTest.deleteVerificationCodeToUserByEmail(correctEmail)).isTrue();
//        assertThatThrownBy(
//                () -> verificationServiceUnderTest.deleteVerificationCodeToUserByEmail("email"))
//                .isInstanceOf(IOException.class);
    }

    @Test
    @Ignore
    void itShouldGenerateCode() {
//        assertThat(verificationServiceUnderTest.generateCode()).isEqualTo("result");
    }

    @Test
    void itShouldTestIsCodeValid() throws Exception {
        assertThat(verificationServiceUnderTest.isCodeValid(wrongEmail, code)).isFalse();
        assertThat(verificationServiceUnderTest.isCodeValid(correctEmail, "99999")).isFalse();
        assertThat(verificationServiceUnderTest.isCodeValid(correctEmail, code)).isTrue();
    }

    @Test
    void itShouldSetValidVerificationStatusOfUser() throws Exception {
        assertThat(verificationServiceUnderTest.setValidVerificationStatusOfUser(wrongEmail, "status")).isFalse();
        assertThat(verificationServiceUnderTest.setValidVerificationStatusOfUser(correctEmail, "true")).isTrue();
    }

    @Test
    void itShouldTestIsUserVerified() throws Exception {
        assertThat(verificationServiceUnderTest.isUserVerified(wrongEmail)).isFalse();
        assertThat(verificationServiceUnderTest.isUserVerified(correctEmail)).isTrue();
    }
}

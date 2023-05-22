package backend.bankwebapp.service;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AppServiceTest {

    private AppService underTest;

    // given
    String correctEmail;
    String wrongEmail;
    String accountType;

    @BeforeEach
    public void setup() {
        underTest = new AppService();

        correctEmail = "admin@mail.com";
        wrongEmail = "xxxx";
        accountType = "USD";
    }

    @Test
    void itShouldCheckIfUserAccountHasSpecificMoneyAccount() throws IOException {
        // when
        boolean expected = underTest.hasTheAccountOfType(correctEmail, accountType);
        boolean expectedFalse = underTest.hasTheAccountOfType(wrongEmail, accountType);
        // then
        assertThat(expected).isTrue();
        assertThat(expectedFalse).isFalse();
    }

    @Test
    void testAddMoneyToAccount() throws Exception {
        // set up
        AppService.openMoneyAccount(correctEmail, "CAD", "100");

        // when
        Boolean result = AppService.addMoneyToAccount(correctEmail, "CAD", 100.0);
        Boolean resultFalse = AppService.addMoneyToAccount(wrongEmail, "CAD", 0.0);

        // then
        assertThat(result).isTrue();
        assertThat(resultFalse).isFalse();
        // close and verify
        assertThat(AppService.closeMoneyAccount(correctEmail, "CAD")).isTrue();
    }

    @Test
    void testWithdrawMoneyFromAccount() throws Exception {
        // set up
        AppService.openMoneyAccount(correctEmail, "CAD", "100");

        // verify
        assertThat(AppService.withdrawMoneyFromAccount(wrongEmail, "CAD", 0.0)).isEqualTo(0);
//        assertThat(AppService.withdrawMoneyFromAccount(wrongEmail, "CAD", 200)).isEqualTo(1);
        assertThat(AppService.withdrawMoneyFromAccount(correctEmail, "CAD", 0.0)).isEqualTo(2);

        // 0 - failed - Account of type not exists/not enough finance and account "CZK" account not exists/not enough finance
        // 1 - success - Account of type not exists/not enough finance and account "CZK" has enough finance
        // 2 - success - Account of type has enough finance

        // close and verify
        assertThat(AppService.closeMoneyAccount(correctEmail, "CAD")).isTrue();
    }

    @Test
    void testOpenMoneyAccount() throws IOException {
        // when
        AppService.openMoneyAccount(correctEmail, "CAD", "100"); // return void !
        // Verify the results
        // if account is opened, it can be closed with true
        assertThat(AppService.closeMoneyAccount(correctEmail, "CAD")).isTrue();
    }

    @Test
    void testCloseMoneyAccount() throws Exception {
        // set up
        AppService.openMoneyAccount(correctEmail, "CAD", "100");
        // verify
        assertThat(AppService.closeMoneyAccount(wrongEmail, "CAD")).isFalse();
        assertThat(AppService.closeMoneyAccount(correctEmail, "CAD")).isTrue();
    }


    @Test
    void testGetAccountsFromJSON() throws Exception {
        assertThat(AppService.getAccountsFromJSON(correctEmail)).isNotNull();
        assertThat(AppService.getAccountsFromJSON(wrongEmail)).isNull();
    }
}
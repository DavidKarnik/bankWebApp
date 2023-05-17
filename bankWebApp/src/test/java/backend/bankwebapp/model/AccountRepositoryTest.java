package backend.bankwebapp.model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountRepositoryTest {

    private AccountRepository underTest;

    @BeforeEach
    void setUp() {
        underTest = new AccountRepository();
    }

    @Test
    void itShouldFindAccountsByUserEmail() {
        // given
        List<String> emails = Arrays.asList("admin@mail.com", "user@mail.com", "example@mail.com");

        // when & then
        for (String email : emails) {
            // Call the method under test
            List<Account> accounts = underTest.findAccountsByUserEmail(email);

            // Assert that the accounts list is null if the email does not exist
            if (email.equals("example@mail.com")) {
                assertThat(accounts).isNull();
            } else {
                // Assert that the accounts list is not null and contains some elements
                assertThat(accounts).isNotNull();

                // if there are some accounts
                if (!accounts.isEmpty()) {
                    // Check the properties of the returned accounts if they exist
                    for (Account acc : accounts) {
                        assertThat(acc.getBalance()).isNotEmpty();
                        assertThat(acc.getCurrency()).isNotEmpty();
                    }
                }
            }
        }
    }
}

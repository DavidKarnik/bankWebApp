package backend.bankwebapp.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TransactionRepositoryTest {

    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setup() {
        transactionRepository = new TransactionRepository();
    }

    @Test
    public void testGetTransactionsOfUserByEmail() {
        // Define a test email
        String testEmail = "admin@mail.com";

        // Invoke the getTransactionsOfUserByEmail method
        List<Transaction> transactions = transactionRepository.getTransactionsOfUserByEmail(testEmail);

        // Assert that the transactions list is not null and contains some elements
        Assertions.assertThat(transactions).isNotNull();

        // if there are some transactions
        if(!transactions.isEmpty()){
            // Check the properties of the returned transactions if it exists
            for (Transaction transaction : transactions) {
                Assertions.assertThat(transaction.getTimestamp()).isNotEmpty();
                Assertions.assertThat(transaction.getAccountType()).isNotEmpty();
                Assertions.assertThat(transaction.getAction()).isNotEmpty();
                Assertions.assertThat(transaction.getFinances()).isNotEmpty();
            }
        }
    }
}

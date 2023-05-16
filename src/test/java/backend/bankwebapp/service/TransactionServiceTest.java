package backend.bankwebapp.service;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionServiceTest {

    private TransactionService transactionService;

    @BeforeEach
    public void setup() {
        transactionService = new TransactionService();
    }

    @Test
    public void itShouldAddTransactionToUserByEmail() throws IOException, JSONException {
        // given
        String testEmail = "admin@mail.com";
        String testAccountType = "CZK";
        String testAction = "Deposit";
        String testFinances = "123";

        // when
        boolean result = transactionService.addTransactionToUserByEmail(testEmail, testAccountType, testAction, testFinances);
        // for Assertions for right save addTransactionToUserByEmail() to the file
        String timeStamp = transactionService.getCurrentTimeStamp();

        // then
        // Assert that the method returned true indicating a successful transaction addition
        Assertions.assertThat(result).isTrue();

        // Verify that the transaction was added to the JSON file correctly
        JsonObject json = getJsonObjectFromTransactionsFile();
        JsonArray users = json.getJsonArray("users");
        JsonArray transactions;

        for (int i = 0; i < users.size(); i++) {
            JsonObject user = users.getJsonObject(i);
            if (user.getString("email").equals(testEmail)) {
                transactions = user.getJsonArray("transactions");
//                transactions.add("abcd");
//                assertTrue(transactions.contains("abcd"));
                Assertions.assertThat(transactions).contains(testTransactionString(timeStamp, testAccountType, testAction, testFinances));
            }
        }
    }

    @Test
    public void testGetCurrentTimeStamp() {
        // Invoke the getCurrentTimeStamp method
        String timeStamp = transactionService.getCurrentTimeStamp();

        // Get the current date and time using Java's LocalDateTime
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String expectedTimeStamp = now.format(formatter);

        // Assert that the returned timestamp matches the expected timestamp
        Assertions.assertThat(timeStamp).isEqualTo(expectedTimeStamp);
    }

    private JsonObject getJsonObjectFromTransactionsFile() throws IOException, JSONException {
//        FileReader reader = new FileReader("src/main/resources/transactions.json");
        ClassPathResource resource = new ClassPathResource("transactions.json");
        FileReader reader = new FileReader(resource.getFile().getPath());
        StringBuilder stringBuilder = new StringBuilder();
        int character;
        while ((character = reader.read()) != -1) {
            stringBuilder.append((char) character);
        }
        reader.close();
        return new JsonObject(stringBuilder.toString());
    }

    private String testTransactionString(String timeStamp, String accountType, String action, String finances) {
        //timeStamp
        return timeStamp + "|" + accountType + "|" + action + "|" + finances;
    }
}

package backend.bankwebapp.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepository {
// Class with methods just for find info and data

    static String filePath = "../transactions.json"; // ec2 aws actual absolute path

    /**
     * Get List of Transactions by Users email
     *
     * @param email - Email of target User
     * @return - List<Transaction> of Transactions
     */
    public static List<Transaction> getTransactionsOfUserByEmail(String email) {
        List<Transaction> transactionList = new ArrayList<>();

        try {
//            FileReader reader = new FileReader("src/main/resources/transactions.json");
            // must use relative path to run app .jar, not absolute
            // and use encoding UTF_8 for sure to handle czech chars
//            ClassPathResource resource = new ClassPathResource(filePath);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8);

            JSONParser parser = new JSONParser();
            JSONObject logObject = (JSONObject) parser.parse(reader);
            // Get the users array from the log object
            JSONArray usersArray = (JSONArray) logObject.get("users");
            // Loop through the users array and find the user with the specified email
            for (Object userObj : usersArray) {
                JSONObject userJson = (JSONObject) userObj;
                String userEmail = (String) userJson.get("email");
                if (userEmail.equals(email)) {
                    JSONArray transactionsJsonArray = (JSONArray) userJson.get("transactions");
                    String[] transactions = new String[transactionsJsonArray.size()];
                    for (int i = 0; i < transactionsJsonArray.size(); i++) {
                        transactions[i] = (String) transactionsJsonArray.get(i); // "2023-05-01 18:07:19|USD|Deposit|1000"
                        // Format the data to the right format
                        String timestamp = transactions[i].split("\\|")[0];
                        String accountType = transactions[i].split("\\|")[1];
                        String action = transactions[i].split("\\|")[2];
                        String finances = transactions[i].split("\\|")[3];
                        Transaction tran = new Transaction(
                                timestamp,
                                accountType,
                                action,
                                finances
                        );
                        transactionList.add(tran);
//                        System.out.println("tran = " + tran);
                    }
                    reader.close();
                    return transactionList;
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if Transaction is not found
    }
}

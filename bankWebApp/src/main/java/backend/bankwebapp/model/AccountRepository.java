package backend.bankwebapp.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountRepository {
// Class with methods just for find info and data

//    @Value("${file.path.log.json}")
    static String filePath = "data/log.json"; // ec2 aws actual absolute path

    /**
     *
     * @param email - Email of target User
     * @return - List<Account> of Accounts
     */
    public static List<Account> findAccountsByUserEmail(String email) {
        List<Account> accountsList = new ArrayList<>();

        try {
//            FileWriter fileWriter = new FileWriter(resource.getFile().getPath());
//            FileReader reader = new FileReader("src/main/resources/log.json"); // TARGET root
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
                    JSONArray accountsJsonArray = (JSONArray) userJson.get("accounts");
                    String[] accounts = new String[accountsJsonArray.size()];
                    for (int i = 0; i < accountsJsonArray.size(); i++) {
                        accounts[i] = (String) accountsJsonArray.get(i); // "USD:2939"
                        // Format the data to the right format
                        String currency = accounts[i].split(":")[0];
                        String balance = accounts[i].split(":")[1];
//                        System.out.println("accounts[i] " + accounts[i]);
                        Account acc = new Account(
                                currency,
                                balance
                        );
                        accountsList.add(acc);
//                        System.out.println("acc = " + acc);
                    }
//                    System.out.println("accounts " + accounts);
//                    System.out.println("accountsJsonArray " + accountsJsonArray);
                    reader.close();
                    return accountsList;
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if Account is not found
    }
}

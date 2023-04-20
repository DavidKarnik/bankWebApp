package backend.bankwebapp.service;

import org.springframework.stereotype.Service;

//import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class AppService {

    /**
     * Method will write into JSON file in accounts array new value type:amount
     * "accounts":["type:amount"]
     * @param email - email of the user
     * @param type - type of new account (USD, CZK, etc.)
     * @param amount - amount of money on the account
     * @throws IOException - file not found/ can not open
     */
    public static void writeToJsonFile(String email, String type, String amount) throws IOException {
        // Read the contents of the log.json file into a string
        String contents = new String(Files.readAllBytes(Paths.get("src/main/resources/log.json")));
        // "src/main/resources/log.json"
        // "target/classes/log.json"

        // Parse the contents into a JSONObject
        JSONObject json = new JSONObject(contents);

        // Get the "users" array
        JSONArray users = json.getJSONArray("users");

        // Find the user with the matching email
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("email").equals(email)) {
                // Get the "accounts" array for the matching user
                JSONArray accounts = user.getJSONArray("accounts");

//                System.out.println("Writing into the file !");

                // Add the new account entry to the accounts array
                accounts.put(type + ":" + amount);

                break;
            }
        }

        // Write the modified JSON back to the log.json file
        FileWriter file = new FileWriter("src/main/resources/log.json");
        file.write(json.toString());
        file.flush();
        file.close();
    }



    /**
     * Test main class
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Try writeToJsonFile()");
        AppService.writeToJsonFile("user@mail.com", "USD", "1000");
        // ctrl + alt + L -> beatify the code ! :)
    }
}

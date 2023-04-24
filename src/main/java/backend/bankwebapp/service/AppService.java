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

////    Variables for reduce duplicates
//
//    // Read the contents of the log.json file into a string
//    static String contents;
//
//    static {
//        try {
//            contents = new String(Files.readAllBytes(Paths.get("src/main/resources/log.json")));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    static JSONObject json = new JSONObject(contents);
//    static JSONArray users = json.getJSONArray("users");


    /**
     * Method will write into JSON file in accounts array new value type:amount
     * "accounts":["type:amount"]
     *
     * @param email  - email of the user
     * @param type   - type of new account (USD, CZK, etc.)
     * @param amount - amount of money on the account
     * @throws IOException - file not found/ can not open
     */
    public static void writeToJsonFile(String email, String type, String amount) throws IOException {
        // Read the contents of the log.json file into a string
        String contents = new String(Files.readAllBytes(Paths.get("src/main/resources/log.json")));

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
     * Verify that account has/has not money account of given type
     * Function for give permission for generate new money account
     *
     * @param email - user´s main account (Id for account is email)
     * @param type  - type of the money account to verify
     * @return - return true/false - has/has not the account
     * @throws IOException - failed work with file
     */
    public static Boolean hasTheAccountOfType(String email, String type) throws IOException {
        // Read the contents of the log.json file into a string
        String contents = new String(Files.readAllBytes(Paths.get("src/main/resources/log.json")));

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
                for (int j = 0; j < accounts.length(); j++) {
//                    System.out.println(accounts.getString(j)); // "USD:1000"
                    // "xx:yy" ... if xx equal the type return TRUE
                    if (accounts.getString(j).split(":")[0].equals(type)) {
                        return true;
                    }
                }
                break;
            }
        }
        return false;
    }

    public static Boolean addMoneyToAccount(String email, String type, int amount) throws IOException {
        // Read the contents of the log.json file into a string
        String contents = new String(Files.readAllBytes(Paths.get("src/main/resources/log.json")));

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

                for (int j = 0; j < accounts.length(); j++) {

                    String account = accounts.getString(j);
                    String[] accountParts = account.split(":");
                    String _type = accountParts[0];
                    int oldAmount = Integer.parseInt(accountParts[1]);

                    if (_type.equals(type)) {
                        // Update the amount of the matching account
                        int newAmount = oldAmount + amount;
                        accounts.put(j, _type + ":" + newAmount);

                        // Write the modified JSON back to the log.json file
                        FileWriter file = new FileWriter("src/main/resources/log.json");
                        file.write(json.toString());
                        file.flush();
                        file.close();
                        return true;
                    }
                }
                break;
            }
        }
        return false;
    }


    /**
     * Test main class
     */
    public static void main(String[] args) throws IOException {
        // ctrl + alt + L -> beatify the code ! :)

//        System.out.println("Try writeToJsonFile()");
//        AppService.writeToJsonFile("user@mail.com", "USD", "1000");

//        System.out.println("Try hasTheAccountOfType()");
//        System.out.println(AppService.hasTheAccountOfType("user@mail.com", "USD"));

        System.out.println("Try addMoneyToAccount()");
        System.out.println(AppService.addMoneyToAccount("user@mail.com", "USD", 1001));
    }
}

package backend.bankwebapp.service;

import backend.bankwebapp.model.AccountRepository;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

// TODO double amount (balance) instead of int !
@Service
public class AppService {

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
        String contents = getContentOfJSON();
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

    /**
     * Method will write into JSON file in accounts array new value type:amount
     * "accounts":["type:amount"]
     *
     * @param email  - email of the user
     * @param type   - type of new account (USD, CZK, etc.)
     * @param amount - amount of money on the account
     * @throws IOException - file not found/ can not open
     */
    public static void openMoneyAccount(String email, String type, String amount) throws IOException {
        // Read the contents of the log.json file into a string
        String contents = getContentOfJSON();
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
     * Function will add money to the specific money account of the email account
     *
     * @param email  - id for account of user
     * @param type   - type of the money account to add
     * @param amount - amount of finance to add to the money account of type
     * @return - return true -> finance added successfully to the account
     * @return - return false -> finance added failed (account not found, etc.)
     * @throws IOException - throws if work with file failed
     */
    public static Boolean addMoneyToAccount(String email, String type, double amount) throws IOException {
        // Read the contents of the log.json file into a string
        String contents = getContentOfJSON();

        // Parse the contents into a JSONObject, for File Write
        JSONObject json = new JSONObject(contents);

//         Get the "users" array
        JSONArray users = json.getJSONArray("users");

//        JSONObject user2 = getUserJSONObject(email);

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
                    double oldAmount = Double.parseDouble(accountParts[1]);

                    if (_type.equals(type)) {
                        // Update the amount of the matching account
                        double newAmount = oldAmount + amount;
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
     * @param email  - id for account of user
     * @param type   - type of the money account to add
     * @param amount - amount of finance to add to the money account of type
     * @return - number by the state
     * @throws IOException - throws if work with file failed
     *                     state:
     *                     0 - failed - Account of type not exists/not enough finance and account "CZK" account not exists/not enough finance
     *                     1 - success - Account of type not exists/not enough finance and account "CZK" has enough finance
     *                     2 - success - Account of type has enough finance
     */
    public static int withdrawMoneyFromAccount(String email, String type, double amount) throws IOException {
        String contents = getContentOfJSON();
        JSONObject json = new JSONObject(contents);
        JSONArray users = json.getJSONArray("users");

//        if (hasTheAccountOfType(email, type)) {
//
//        } else if (hasTheAccountOfType(email, "CZK")) {
//
//        }

        int state = 2;
        double newAmount = 0;

        // Find the user with the matching email
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("email").equals(email)) {

                // Get the "accounts" array for the matching user
                JSONArray accounts = user.getJSONArray("accounts");

                // try to find the account of given type
                for (int j = 0; j < accounts.length(); j++) {
                    String account = accounts.getString(j);
                    String[] accountParts = account.split(":");
                    String _type = accountParts[0];

                    if (_type.equals(type)) {
                        // account of type found
                        double oldAmount = Double.parseDouble(accountParts[1]);
                        // try
                        if (oldAmount < amount) {
                            // try czk account
                            if (type.equalsIgnoreCase("CZK")) {
                                return 0;
                            }
                            break;
                        }
                        // OK state = 2;
                        // Update the amount of the matching account
                        newAmount = oldAmount - amount;
                        accounts.put(j, _type + ":" + newAmount);
                        // Write the modified JSON back to the log.json file
                        FileWriter file = new FileWriter("src/main/resources/log.json");
                        file.write(json.toString());
                        file.flush();
                        file.close();
                        return state;
                    }
                }

                // try to find czk account if given accountType != "CZK"
                for (int a = 0; a < accounts.length(); a++) {
                    String account = accounts.getString(a);
                    String[] accountParts = account.split(":");
                    String _type = accountParts[0];

                    if (_type.equalsIgnoreCase("CZK")) {
                        // account of type found
                        double oldAmount = Double.parseDouble(accountParts[1]);
                        // try
                        if (oldAmount < amount) {
                            return 0; // account czk has not enough finance
                        }
                        // OK
                        state = 1;
                        // Update the amount of the matching account
                        newAmount = oldAmount - amount;
                        accounts.put(a, _type + ":" + newAmount);
                        // Write the modified JSON back to the log.json file
                        FileWriter file = new FileWriter("src/main/resources/log.json");
                        file.write(json.toString());
                        file.flush();
                        file.close();
                        return state;
                    }
                }
                break;
            }
        }
        return 0;
    }

    /**
     * Method will close money account that matches the given accountType from users account by users email
     * @param email - id for account of user
     * @param accountType - type of the money account to add
     * @return Boolean - true/false - successful/failed to close account
     * @throws IOException - file error
     */
    public static Boolean closeMoneyAccount(String email, String accountType) throws IOException {
        // Read the contents of the log.json file into a string
        String contents = getContentOfJSON();
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
                // try to find the account of given type
                for (int j = 0; j < accounts.length(); j++) {
                    String account = accounts.getString(j);
                    String[] accountParts = account.split(":");
                    if (accountParts[0].equals(accountType)) {
                        // account of type found
                        accounts.remove(j);
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
     * Read from "src/main/resources/log.json"
     *
     * @return - String content of log.json file
     * @throws IOException - file error
     */
    private static String getContentOfJSON() throws IOException {
        return new String(Files.readAllBytes(Paths.get("src/main/resources/log.json")));
    }

    // user.getAccountsArray() -> ["USD:100", "CZK:234", ...]
    // get
    public static String getAccountsFromJSON(String email) throws IOException {
        // Read the contents of the log.json file into a string
        String contents = getContentOfJSON();
        // Parse the contents into a JSONObject
        JSONObject json = new JSONObject(contents);
        // Get the "users" array
        JSONArray users = json.getJSONArray("users");

        // Find the user with the matching email
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("email").equals(email)) {
                // Get the "accounts" array for the matching user
//                JSONArray accounts = user.getJSONArray("accounts");
                System.out.println((user.getJSONArray("accounts")).toString());
                return (user.getJSONArray("accounts")).toString();
            }
        }
        return null;
    }

    /**
     * Test main class
     */
    public static void main(String[] args) throws IOException {
        // ctrl + alt + L -> beatify the code ! :)

//        System.out.println("Try openMoneyAccount()");
//        AppService.openMoneyAccount("user@mail.com", "USD", "1000");

//        System.out.println("Try hasTheAccountOfType()");
//        System.out.println(AppService.hasTheAccountOfType("user@mail.com", "USD"));

//        System.out.println("Try addMoneyToAccount()");
//        System.out.println(AppService.addMoneyToAccount("user@mail.com", "CZK", 1));

//        System.out.println("Try withdrawMoneyFromAccount()");
//        System.out.println(AppService.withdrawMoneyFromAccount("admin@mail.com", "USD", 11));

//        System.out.println("Try closeMoneyAccount()");
//        System.out.println(AppService.closeMoneyAccount("admin@mail.com", "USD"));

//        getAccountsFromJSON("user@mail.com");

        AccountRepository.findAccountsByUserEmail("user@mail.com");

    }
}

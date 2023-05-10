package backend.bankwebapp.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class VerificationService {

    public static Boolean addVerificationCodeToUserByEmail(String email, String code) throws IOException {
        // Read the contents of the log.json file into a string
        String contents = new String(Files.readAllBytes(Paths.get("src/main/resources/verification.json")));
        // Parse the contents into a JSONObject, for File Write
        JSONObject json = new JSONObject(contents);
        // Get the "users" array
        JSONArray users = json.getJSONArray("users");

        // Update the verification code for the user with the specified email address
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("email").equals(email)) {
                user.put("verificationCode", code);

                // Write the modified JSON back to the file
                FileWriter file = new FileWriter("src/main/resources/verification.json");
                file.write(json.toString());
                file.flush();
                file.close();
                return true;
            }
        }
        return false; // User not found
    }

    public static Boolean deleteVerificationCodeToUserByEmail(String email) throws IOException {
        String contents = new String(Files.readAllBytes(Paths.get("src/main/resources/verification.json")));
        JSONObject json = new JSONObject(contents);
        JSONArray users = json.getJSONArray("users");

        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("email").equals(email)) {
                user.put("verificationCode", "");

                // Write the modified JSON back to the file
                FileWriter file = new FileWriter("src/main/resources/verification.json");
                file.write(json.toString());
                file.flush();
                file.close();
                return true;
            }
        }
        return false; // User not found
    }

    public static String generateCode() {
        // Generate a random verification code
        // eg. 27bd5fbe-176f-488c-81a2-af8c34d0cada
        return UUID.randomUUID().toString().substring(0,13);
    }

    public static void storeCode(String email, String code) {
        // Store the verification code in a database or other data store
    }

    public static void main(String[] args) throws IOException {
        System.out.println(generateCode());
        System.out.println(addVerificationCodeToUserByEmail("admin@mail.com",generateCode()));
//        deleteVerificationCodeToUserByEmail("admin@mail.com");
    }
}

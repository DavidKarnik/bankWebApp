package backend.bankwebapp.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class VerificationService {

    String filePath = "../verification.json"; // ec2 aws actual absolute path

    /**
     * Store Verification Code To User by given email to json file
     *
     * @param email - Email of specific User
     * @param code  - Verification Code
     * @return
     * @throws IOException
     */
    public Boolean addVerificationCodeToUserByEmail(String email, String code) throws IOException {
        // Read the contents of the log.json file into a string
        // need relative path, not absolute for .jar and run app
//        String contents = new String(Files.readAllBytes(Paths.get("src/main/resources/verification.json")));
//        ClassPathResource getResource = new ClassPathResource(filePath);
        byte[] fileBytes = FileCopyUtils.copyToByteArray(new File(filePath));
        String contents = new String(fileBytes);
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
//                FileWriter file = new FileWriter("src/main/resources/verification.json");
//                ClassPathResource resource = new ClassPathResource(filePath);
                FileWriter file = new FileWriter(filePath);
                file.write(json.toString());
                file.flush();
                file.close();
                return true;
            }
        }
        return false; // User not found
    }

    public Boolean deleteVerificationCodeToUserByEmail(String email) throws IOException {
//        String contents = new String(Files.readAllBytes(Paths.get("src/main/resources/verification.json")));
//        ClassPathResource getResource = new ClassPathResource(filePath);
        byte[] fileBytes = FileCopyUtils.copyToByteArray(new File(filePath));
        String contents = new String(fileBytes);

        JSONObject json = new JSONObject(contents);
        JSONArray users = json.getJSONArray("users");

        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("email").equals(email)) {
                user.put("verificationCode", "");

                // Write the modified JSON back to the file
//                FileWriter file = new FileWriter("src/main/resources/verification.json");
//                ClassPathResource resource = new ClassPathResource(filePath);
                FileWriter file = new FileWriter(filePath);
                file.write(json.toString());
                file.flush();
                file.close();
                return true;
            }
        }
        return false; // User not found
    }

    public String generateCode() {
        // Generate a random verification code
        // eg. 27bd5fbe-176f-488c-81a2-af8c34d0cada
        return UUID.randomUUID().toString().substring(0, 13);
    }

    /**
     * @param email
     * @param code
     * @return
     * @throws IOException
     */
    public Boolean isCodeValid(String email, String code) throws IOException {
        // Load user data from the JSON file
//        String jsonString = new String(Files.readAllBytes(Paths.get("src/main/resources/verification.json")));
//        ClassPathResource getResource = new ClassPathResource(filePath);
        byte[] fileBytes = FileCopyUtils.copyToByteArray(new File(filePath));
        String contents = new String(fileBytes);

        JSONObject userData = new JSONObject(contents);
        JSONArray users = userData.getJSONArray("users");

        // Search for the user with the specified email address
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("email").equals(email)) {
                // Retrieve the verification code and compare it with the entered code
                String storedCode = user.getString("verificationCode");
                if (code.equals(storedCode)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Set Validation status of user by email (true/false)
     *
     * @param email
     * @param status - String true/false
     * @return
     * @throws IOException
     */
    public Boolean setValidVerificationStatusOfUser(String email, String status) throws IOException {
        // Read the contents of the log.json file into a string
//        String contents = new String(Files.readAllBytes(Paths.get("src/main/resources/verification.json")));
//        ClassPathResource getResource = new ClassPathResource(filePath);
        byte[] fileBytes = FileCopyUtils.copyToByteArray(new File(filePath));
        String contents = new String(fileBytes);

        // Parse the contents into a JSONObject, for File Write
        JSONObject json = new JSONObject(contents);
        // Get the "users" array
        JSONArray users = json.getJSONArray("users");

        // Update the verification code for the user with the specified email address
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("email").equals(email)) {
                user.put("isActive", status);

                // Write the modified JSON back to the file
//                FileWriter file = new FileWriter("src/main/resources/verification.json");
//                ClassPathResource resource = new ClassPathResource(filePath);
                FileWriter file = new FileWriter(filePath);
                file.write(json.toString());
                file.flush();
                file.close();
                return true; // successfully changed
            }
        }
        return false; // User not found
    }

    /**
     * User is verified successfully ? true/false
     *
     * @param email
     * @return
     * @throws IOException
     */
    public Boolean isUserVerified(String email) throws IOException {
        // Load user data from the JSON file
//        String jsonString = new String(Files.readAllBytes(Paths.get("src/main/resources/verification.json")));
//        ClassPathResource getResource = new ClassPathResource(filePath);
        byte[] fileBytes = FileCopyUtils.copyToByteArray(new File(filePath));
        String contents = new String(fileBytes);

        JSONObject userData = new JSONObject(contents);
        JSONArray users = userData.getJSONArray("users");

        // Search for the user with the specified email address
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("email").equals(email)) {
                // Retrieve the verification code and compare it with the entered code
                String isActive = user.getString("isActive");
                if (isActive.equalsIgnoreCase("true")) {
                    return true; // User is good to go to the main page
                }
            }
        }
        return false; // User do not validate himself
    }

//    public static void main(String[] args) throws IOException {
////        System.out.println(generateCode());
////        System.out.println(addVerificationCodeToUserByEmail("admin@mail.com",generateCode()));
////        deleteVerificationCodeToUserByEmail("admin@mail.com");
//    }
}

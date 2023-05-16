package backend.bankwebapp.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TransactionService {

    /**
     *
     * @param email
     * @param accountType
     * @param action
     * @param finances
     * @return
     * @throws IOException
     */
    public static Boolean addTransactionToUserByEmail(String email, String accountType, String action, String finances) throws IOException {
        // Read the contents of the log.json file into a string
        // need relative path, not absolute for .jar and run app
//        String contents = new String(Files.readAllBytes(Paths.get("src/main/resources/transactions.json")));
        ClassPathResource getResource = new ClassPathResource("transactions.json");
        byte[] fileBytes = FileCopyUtils.copyToByteArray(getResource.getInputStream());
        String contents = new String(fileBytes);
        // Parse the contents into a JSONObject, for File Write
        JSONObject json = new JSONObject(contents);
        // Get the "users" array
        JSONArray users = json.getJSONArray("users");

        // Find the user with the matching email
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("email").equals(email)) {
                // Get the "transactions" array for the matching user
                JSONArray transactions = user.getJSONArray("transactions");

                // Get the current date and time to include in the output file name
                String timeStamp = getCurrentTimeStamp();

                transactions.put(timeStamp + "|" + accountType + "|" + action + "|" + finances); // add data

                // Write the modified JSON back to the file
                // need relative path not absolute
                ClassPathResource resource = new ClassPathResource("transactions.json");
                FileWriter file = new FileWriter(resource.getFile().getPath());
//                FileWriter file = new FileWriter("src/main/resources/transactions.json");
                file.write(json.toString());
                file.flush();
                file.close();
                return true;
            }
        }
        return false; // User not found
    }

    public static String getCurrentTimeStamp() {
        // Get the current date and time to include in the output file name
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

//    public static void main(String[] args) throws IOException {
////        LocalDateTime now = LocalDateTime.now();
////        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
////        String timestamp = now.format(formatter);
//
//        System.out.println(addTransactionToUserByEmail("admin@mail.com", "CZK", "Deposit", "99"));
//    }
}

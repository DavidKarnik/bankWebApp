package backend.bankwebapp.model;


//import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Repository;


/**
 * Class for finding Users/User
 */
@Repository
public class UserRepository {
// Class with methods just for find info and data

//    File file = new File("log.json");

    public static List<User> findAll() {
        List<User> users = new ArrayList<>();

        try {
            // Read the log.json file using JSON parser
            FileReader reader = new FileReader("src/main/resources/log.json");
            JSONParser parser = new JSONParser();
            JSONObject logObject = (JSONObject) parser.parse(reader);

            // Get the users array from the log object
            JSONArray usersArray = (JSONArray) logObject.get("users");

            // Loop through the users array and create User objects
            for (Object userObj : usersArray) {
                JSONObject userJson = (JSONObject) userObj;

                JSONArray accountsJsonArray = (JSONArray) userJson.get("accounts");
                String[] accounts = new String[accountsJsonArray.size()];
                for (int i = 0; i < accountsJsonArray.size(); i++) {
                    accounts[i] = (String) accountsJsonArray.get(i);
                }

                User user = new User(
                        (String) userJson.get("email"),
                        (String) userJson.get("password"),
                        (String) userJson.get("firstName"),
                        (String) userJson.get("lastName"),
                        accounts
                );
                users.add(user);
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    public static User findByEmail(String email) {
        try {
//            System.out.println(UserRepository.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            // Read the log.json file using JSON parser
            FileReader reader = new FileReader("src/main/resources/log.json"); // TARGET root
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
                        accounts[i] = (String) accountsJsonArray.get(i);
                    }

                    User user = new User(
                            userEmail,
                            (String) userJson.get("password"),
                            (String) userJson.get("firstName"),
                            (String) userJson.get("lastName"),
                            accounts
                    );
                    reader.close();
                    return user;
                }
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // Return null if user is not found
    }

}

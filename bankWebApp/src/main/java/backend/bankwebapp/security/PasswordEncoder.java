package backend.bankwebapp.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Class for encode passwords manually
 */
public class PasswordEncoder {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "user";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
    }
}

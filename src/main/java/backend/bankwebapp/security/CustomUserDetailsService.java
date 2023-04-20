package backend.bankwebapp.security;

import backend.bankwebapp.model.User;
import backend.bankwebapp.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CustomUserDetailsService implements UserDetailsService {

//    @Autowired
//    private UserRepository repo;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = null;
//        try {
            user = UserRepository.findByEmail(email);
            System.out.println("New login request");
            System.out.println("user: " + user);
            System.out.println("email input: " + email);
//        } catch (IOException e) {
//            throw new RuntimeException(e + " errrrrrrrrrrrorrrrrr");
//        }
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user);
    }
}

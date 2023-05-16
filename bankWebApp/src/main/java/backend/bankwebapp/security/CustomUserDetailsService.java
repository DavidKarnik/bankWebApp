package backend.bankwebapp.security;

import backend.bankwebapp.mail.MyMailService;
import backend.bankwebapp.model.User;
import backend.bankwebapp.model.UserRepository;
import backend.bankwebapp.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = null;

        user = userRepository.findByEmail(email);

        System.out.println("New login request");
        System.out.println("user: " + user);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        // Assigning the "ROLE_ADMIN" role to the user
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ABC"));

        return new CustomUserDetails(user);
    }
}

package backend.bankwebapp.model;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class UserRepositoryTest {

    private UserRepository underTest;

    @BeforeEach
    public void setup() {
        underTest = new UserRepository();
    }

    @Test
    void itShouldFindAllUsers() {
        //given

        // when
        List<User> users = underTest.findAll(); // Invoke the findAll method

        //then
        // Assert that the users list is not null and contains some elements
        Assertions.assertThat(users).isNotNull().isNotEmpty();
    }

    @Test
    void itShouldFindUserByEmail() {
        // given
        String email = "admin@mail.com";

        // when
        User user = underTest.findByEmail(email);

        // then
        Assertions.assertThat(user).isNotNull(); // Assert that the returned user is not null

        Assertions.assertThat(user.getEmail()).isEqualTo(email); // Assert that the returned user has the expected email

    }
}
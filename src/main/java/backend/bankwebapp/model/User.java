package backend.bankwebapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private String email;
    private String password;
    private String firstName;
    private String lastName;

    private String[] accounts;

    public User(String email, String password, String firstName, String lastName, String[] accounts) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    /**
     * Getter for accounts converted into one String
     * @return - String of accounts ("USD:10,CZK:5")
     */
    public String getAccounts() {
        StringBuilder out = new StringBuilder();
        for (String account : accounts) {
            out.append(account).append(",");
        }
        return out.toString();
    }

    /**
     * Getter for array accounts
     * @return - String[] accounts
     */
    public String[] getAccountsArray() {
        return accounts;
    }

}

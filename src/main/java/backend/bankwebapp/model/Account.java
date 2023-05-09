package backend.bankwebapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    // Account number not needed, One user account by email can have maximum 1 money account of currency type
//    private String number;
//    private String email; // ID user?
    private String currency;
    private String balance;

    public String getCurrency() {
        return currency;
    }
    public String getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "currency='" + currency + '\'' +
                ", balance='" + balance + '\'' +
                '}';
    }
}

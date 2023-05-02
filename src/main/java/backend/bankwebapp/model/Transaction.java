package backend.bankwebapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    // eg. "2023-05-01 18:07:19|USD|Deposit|1000"
    // in file "src/main/resources/transactions.json"
    private String timestamp;
    private String accountType;
    private String action;
    private String finances;

    @Override
    public String toString() {
        return "Transaction{" +
                "timestamp='" + timestamp + '\'' +
                ", accountType='" + accountType + '\'' +
                ", action='" + action + '\'' +
                ", finances='" + finances + '\'' +
                '}';
    }
}

package backend.bankwebapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {
    // Model for format print for html page
    // Specific html page content Country|Currency|Amount|Code|ExchangeRate\n (e.g. Polsko|zlotý|1|PLN|5,121)
    private String country;
    private String currency;
    private String amount;
    private String code;
    private String exchangeRate;
}

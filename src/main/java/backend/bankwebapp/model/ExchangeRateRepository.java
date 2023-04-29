package backend.bankwebapp.model;


import backend.bankwebapp.service.ExchangeRateService;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ExchangeRateRepository {

    public static List<ExchangeRate> getListOfExchangeRates() {
        List<ExchangeRate> listExchangeRates = new ArrayList<>();
        // Get Exchange Array
        String[][] text = ExchangeRateService.getExchangeRateStringArray();
        // Format output for ExchangeRate objects
        for (int i = 0; i < text.length; i++) {
            ExchangeRate exRate = new ExchangeRate(
                    text[i][0], //country
                    text[i][1], //currency
                    text[i][2], //amount
                    text[i][3], //code
                    text[i][4] //exchangeRate
                    );
            listExchangeRates.add(exRate);
        }
        return listExchangeRates;
    }

}

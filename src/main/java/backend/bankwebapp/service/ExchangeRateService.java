package backend.bankwebapp.service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static backend.bankwebapp.model.ExchangeRateRepository.*;

@Service
@EnableScheduling // for schedule tasks, like refresh everyday or after specific time
public class ExchangeRateService {

    /**
     * Scheduled Method
     * Method will run and refresh values in exchangeRate.txt file every workday (Monday-Friday) at 15:00 Prague time
     * (cron = second minute hour day-of-month month-of-the-year *)
     * The "?" means that the day-of-month field is ignored, and it gets last param "MON-FRI".
     * cnb.cz refresh exchange rates only every work day.
     * Scheduled will NOT work on static methods !
     */
    @Scheduled(cron = "0 0 15 ? * MON-FRI", zone = "Europe/Prague")
    public void refreshFileExchangeRate() {
        String url = "https://www.cnb.cz/cs/financni-trhy/devizovy-trh/kurzy-devizoveho-trhu/kurzy-devizoveho-trhu/denni_kurz.txt";
        String htmlContent = getHtmlContent(url);
        try {
            // Get the current date and time to include in the output file name
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);
            // Write the htmlContent to a file
            FileWriter writer = new FileWriter("src/main/resources/exchangeRate.txt", false); // ,false for override whole file as new one
            writer.write(timestamp + "\n");
            writer.write(htmlContent + "\n");
            writer.close();
        } catch (IOException e) {
            // Handle the exception
        }
    }


    /**
     * @param currencyFrom - Code of Currency Exchange From
     * @param amount       - Finance for exchange
     * @return - Counted exchange of finance to CZK
     */
    public static double doExchangeRateCount(String currencyFrom, double amount) {
        double output = amount;
        if (currencyFrom.equalsIgnoreCase("CZK")) {
            return output; // From CZK to CZK
        }
        String[] exchangeInfo = getSpecificExchangeRateLineByCode(currencyFrom);
        // TODO handle exchangeInfo == null

        double ExAmount = Double.parseDouble(exchangeInfo[2].replaceAll(",",".")); // amount to CZK
        double ExRate = Double.parseDouble(exchangeInfo[4].replaceAll(",",".")); // exchange rate
        output = output * ExRate / ExAmount;
        return output;
    }

    //    Testing
    public static void main(String[] args) {
        System.out.printf("Hello and welcome!\n");
        System.out.println("-------------------\n");

//        String htmlContent = getHtmlContent("https://www.cnb.cz/cs/financni-trhy/devizovy-trh/kurzy-devizoveho-trhu/kurzy-devizoveho-trhu/denni_kurz.txt");
//        System.out.println(htmlContent);

//        printArray(getExchangeRateStringArray());

//        refreshFileExchangeRate();

        System.out.println(doExchangeRateCount("USD", 1000));

    }
}

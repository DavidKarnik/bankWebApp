package backend.bankwebapp.service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import backend.bankwebapp.model.ExchangeRateRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling // for schedule tasks, like refresh everyday or after specific time
public class ExchangeRateService {

//    @Value("${file.path.exchangeRate.txt}")
    String filePath = "data/exchangeRate.txt"; // ec2 aws actual absolute path

    private static ExchangeRateRepository exchangeRateRepository;

    /**
     * Scheduled Method
     * Method will run and refresh values in exchangeRate.txt file every workday (Monday-Friday) at 15:00 Prague time
     * (cron = second minute hour day-of-month month-of-the-year *)
     * The "?" means that the day-of-month field is ignored, and it gets last param "MON-FRI".
     * cnb.cz refresh exchange rates only every work day.
     * Scheduled will NOT work on static methods !
     */
    @Scheduled(cron = "0 0 15 ? * MON-FRI", zone = "Europe/Prague")
    public Boolean refreshFileExchangeRate() {
        String url = "https://www.cnb.cz/cs/financni-trhy/devizovy-trh/kurzy-devizoveho-trhu/kurzy-devizoveho-trhu/denni_kurz.txt";
        String htmlContent = exchangeRateRepository.getHtmlContent(url);
        try {
            // Get the current date and time to include in the output file name
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);
            // Write the htmlContent to a file
            // ,false for override whole file as new one
//            FileWriter writer = new FileWriter("src/main/resources/exchangeRate.txt", false);
//            ClassPathResource resource = new ClassPathResource(filePath);
//            FileWriter file = new FileWriter(resource.getFile().getPath(),false);
            FileWriter file = new FileWriter(filePath,false);
            file.write(timestamp + "\n");
            file.write(htmlContent + "\n");
            file.close();
            return true;
        } catch (IOException e) {
            // Handle the exception
        }
        return false;
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
        String[] exchangeInfo = exchangeRateRepository.getSpecificExchangeRateLineByCode(currencyFrom);
        // TODO handle exchangeInfo == null
        double ExAmount = Double.parseDouble(exchangeInfo[2].replaceAll(",",".")); // amount to CZK
        double ExRate = Double.parseDouble(exchangeInfo[4].replaceAll(",",".")); // exchange rate
        output = (output * ExRate) / ExAmount;
        return output;
    }

    //    Testing
//    public static void main(String[] args) {
//        System.out.printf("Hello and welcome!\n");
//        System.out.println("-------------------\n");
//
////        String htmlContent = getHtmlContent("https://www.cnb.cz/cs/financni-trhy/devizovy-trh/kurzy-devizoveho-trhu/kurzy-devizoveho-trhu/denni_kurz.txt");
////        System.out.println(htmlContent);
//
////        printArray(getExchangeRateStringArray());
//
////        refreshFileExchangeRate();
//
////        System.out.println(doExchangeRateCount("USD", 1000));
//
//    }
}

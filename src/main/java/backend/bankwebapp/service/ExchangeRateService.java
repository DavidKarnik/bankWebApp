package backend.bankwebapp.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling // for schedule tasks, like refresh everyday or after specific time
public class ExchangeRateService {


    /**
     * Method will run and refresh values in exchangeRate.txt file every workday (Monday-Friday) at 15:00 Prague time
     * (cron = second minute hour day-of-month month-of-the-year *)
     * The "?" means that the day-of-month field is ignored, and it gets last param "MON-FRI"
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
            FileWriter writer = new FileWriter("src/main/resources/exchangeRate.txt", true);
            writer.write(timestamp + "\n");
            writer.write(htmlContent + "\n");
            writer.close();
        } catch (IOException e) {
            // Handle the exception
        }
    }

    /**
     * Read exchangeRate.txt file to the String
     * @return - String content of exchangeRate.txt
     */
    public static String readExchangeRateFile() {
        StringBuilder contentBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/exchangeRate.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            // Handle the exception
        }

        return contentBuilder.toString();
    }

    /**
     * Load html page content into String
     *
     * @param url - URL address of target html page
     * @return - String text of html page content
     */
    public static String getHtmlContent(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return EntityUtils.toString(response.getEntity());
        } catch (IOException ignored) {

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Return String[][] array from currency list by cnb.cz and cuts the head info
     * Gets specific html page content Country|Currency|Amount|Code|ExchangeRate\n (e.g. Polsko|zlotý|1|PLN|5,121)
     * @return - Specific String[][] array of exchange currency table
     */
    public static String[][] getExchangeRateStringArray() {
        String[] edit = ExchangeRateService.readExchangeRateFile().split("\n");
        String[][] output = new String[edit.length - 3][5];
        String line;
        for (int i = 0; i < edit.length - 3; i++) {
            line = edit[i + 3];//.replace('\u007c', ' '); // |
            output[i] = line.split("\\|");
        }
        return output;
    }

    /**
     * Print String[][] array to the console
     *
     * @param text - String[][] array
     */
    public static void printArray(String[][] text) {
        for (int i = 0; i < text.length; i++) {
            for (int j = 0; j < text[i].length; j++) {
                System.out.print(text[i][j] + " ");
            }
            System.out.println("");
        }
    }

    //    Testing
    public static void main(String[] args) {
        // Press Alt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!\n");

//        String htmlContent = getHtmlContent("https://www.cnb.cz/cs/financni-trhy/devizovy-trh/kurzy-devizoveho-trhu/kurzy-devizoveho-trhu/denni_kurz.txt");
//        System.out.println(htmlContent);

//        char verticalLine = '\u007c'; // '|'
//        System.out.println(String.valueOf(verticalLine));

        System.out.println("-------------------\n");
        printArray(getExchangeRateStringArray());

    }
}

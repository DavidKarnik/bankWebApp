package backend.bankwebapp.model;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ExchangeRateRepository {
// Class with methods just for find info and data

    static String filePath = "../data/exchangeRate.txt"; // ec2 aws actual absolute path

    /**
     * @return - List<ExchangeRate> getListOfExchangeRates
     */
    public static List<ExchangeRate> getListOfExchangeRates() {
        List<ExchangeRate> listExchangeRates = new ArrayList<>();
        // Get Exchange Array
        String[][] text = getExchangeRateStringArray();
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

    /**
     * Read exchangeRate.txt file to the String
     *
     * @return - String content of exchangeRate.txt
     */
    public static String readExchangeRateFile() {
        StringBuilder contentBuilder = new StringBuilder();

        try {
            // absolute path will not work in .jar file, because it works only for IDE environment, so use relative like that
//            ClassPathResource resource = new ClassPathResource(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));

            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }

            reader.close();
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
     *
     * @return - Specific String[][] array of exchange currency table
     */
    public static String[][] getExchangeRateStringArray() {
        String[] edit = readExchangeRateFile().split("\n");
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
            System.out.println();
        }
    }

    /**
     * @param currencyCode - Code of Currency (eg. "USD", "CZK")
     * @return - String[] array with of one found line of Exchange Rate
     */
    public static String[] getSpecificExchangeRateLineByCode(String currencyCode) {
        String[][] read = getExchangeRateStringArray();
        String[] output;
        for (int i = 0; i < read.length; i++) {
            //check for CODE
            if (read[i][3].equalsIgnoreCase(currencyCode)) {
                return read[i];
            }
        }
        return null;
    }

    /**
     * @param currencyCode - Code of Currency (eg. "USD", "CZK")
     * @return - eg. "USD 1x 21,144"
     */
    public static String getSpecificExchangeRateForPrint(String currencyCode) {
        String[] exRate = getSpecificExchangeRateLineByCode(currencyCode);
        String output = exRate[3] + " " + exRate[2] + "x " + exRate[4];
        return output;
    }

    /**
     * Get Time of Refresh (Valid time) for Exchange Rates
     *
     * @return - String of time, eg. 28.04.2023
     */
    public static String getRefreshedTimeOfRates() {
        String[] edit = readExchangeRateFile().split("\n");
        String output = edit[1].split(" ")[0]; // 28.04.2023 #83
        return (output + " 15:00 (UTC+2 Europe/Prague)"); // eg. 28.04.2023 + ...
    }

    //    Testing
//    public static void main(String[] args) {
//        // Press Alt+Enter with your caret at the highlighted text to see how
//        // IntelliJ IDEA suggests fixing it.
//        System.out.printf("Hello and welcome!\n");
//        System.out.println("-------------------\n");
//
////        String htmlContent = getHtmlContent("https://www.cnb.cz/cs/financni-trhy/devizovy-trh/kurzy-devizoveho-trhu/kurzy-devizoveho-trhu/denni_kurz.txt");
////        System.out.println(htmlContent);
//
////        char verticalLine = '\u007c'; // '|'
////        System.out.println(String.valueOf(verticalLine));
//
////        printArray(getExchangeRateStringArray());
//
////        System.out.println(getRefreshedTimeOfRates());
//
//        System.out.println(readExchangeRateFile());
//    }
}

package backend.bankwebapp.service;

import java.io.IOException;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateService {

    private static final String ulrCNB = "https://www.cnb.cz/cs/financni-trhy/devizovy-trh/kurzy-devizoveho-trhu/kurzy-devizoveho-trhu/denni_kurz.txt";

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
     * Return String[][] array from currency list by cnb.cz and cuts the header info
     *
     * @param text - Specific html page content Name|Type|Amount|Currency\n...
     * @return - Specific String[][] array of exchange currency table
     */
    public static String[][] doStringArray(String text) {
        String[] edit = text.split("\n");
        String[][] output = new String[edit.length - 2][5];
        String line;
        for (int i = 0; i < edit.length - 2; i++) {
            line = edit[i + 2].replace('\u007c', ' ');
            output[i] = line.split(" ");
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

        String htmlContent = getHtmlContent(ulrCNB);
        System.out.println(htmlContent);

//        char verticalLine = '\u007c'; // '|'
//        System.out.println(String.valueOf(verticalLine));

        System.out.println("-------------------\n");
        printArray(doStringArray(htmlContent));
    }
}

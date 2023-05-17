package backend.bankwebapp.model;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//@TestPropertySource(properties = "file.encoding=UTF-8")
class ExchangeRateRepositoryTest {

    private ExchangeRateRepository underTest;

    @BeforeEach
    void setUp() {
        underTest = new ExchangeRateRepository();
    }

    @Test
    void itShouldGetListOfExchangeRates() {

        // when
        List<ExchangeRate> exchangeRates = underTest.getListOfExchangeRates();

        // then
        assertThat(exchangeRates).isNotNull();
//        Austrálie|dolar|1|AUD|14,228
//        Brazílie|real|1|BRL|4,240
        assertThat(exchangeRates.get(0).getCurrency()).isEqualTo("dolar");
        assertThat(exchangeRates.get(0).getAmount()).isEqualTo("1");
//        assertThat(exchangeRates.get(0).getExchangeRate()).isEqualTo("14,228");

        //Bulharsko|lev|1|BGN|12,105
        assertThat(exchangeRates.get(2).getCountry()).isEqualTo("Bulharsko");
        assertThat(exchangeRates.get(2).getCurrency()).isEqualTo("lev");
        assertThat(exchangeRates.get(2).getAmount()).isEqualTo("1");
    }


    @Test
    void itShouldReadExchangeRateFile() throws IOException {

        // when
        underTest.readExchangeRateFile();

        // then

    }


    @Test
    void itShouldGetHtmlContent() {
        // given
        String url = "https://example.com";

        // TODO mock CloseableHttpClient ?

        // when
        String result = underTest.getHtmlContent(url);

        // then
        assertThat(result).isNotEmpty().isNotNull();
    }

    @Test
    void getExchangeRateStringArray() {
        // given
        // test just first 4 column that are not changing
        // Austrálie|dolar|1|AUD|14,481
        String[][] compare = {
                {"Austrálie", "dolar", "1", "AUD"},
                {"Brazílie", "real", "1", "BRL"},
                {"Bulharsko", "lev", "1", "BGN"},
                {"Čína", "žen-min-pi", "1", "CNY"},
                {"Dánsko", "koruna", "1", "DKK"},
                {"EMU", "euro", "1", "EUR"},
                {"Filipíny", "peso", "100", "PHP"},
                {"Hongkong", "dolar", "1", "HKD"},
                {"Indie", "rupie", "100", "INR"}


                // and so on ...
        };

        // when
        String[][] result = underTest.getExchangeRateStringArray();

        // then
        // every 3rd
        // TODO fix encoding to UTF-8 somehow :(
        for (int i = 3; i < compare.length; i=i+3) {
            for (int j = 3; j < compare[i].length; j++) {
                assertThat(compare[i][j]).isEqualTo(result[i][j]);
            }
        }
    }


    @Test
    @Ignore
    void printArray() {
        // just print String[][] array
    }

    @Test
    void getSpecificExchangeRateLineByCode() {
        // given
        List<String> codes = Arrays.asList("PLN", "USD", "XXX");

        // when & then
        for (String code : codes) {
            String[] result = underTest.getSpecificExchangeRateLineByCode(code);
            // Polsko|zlotý|1|PLN|5,208

            // then
            // Assert that the accounts list is null if the email does not exist
            if (code.equals("XXX")) {
                assertThat(result).isNull();
            } else {
//                assertThat(result[0]).isEqualTo("Polsko");
//                assertThat(result[1]).isEqualTo("zlotý");
//                assertThat(result[2]).isEqualTo("1");
//                assertThat(result[3]).isEqualTo("PLN");
                assert result != null;
                assertThat(result[0]).isNotEmpty();
                assertThat(result[1]).isNotEmpty();
                assertThat(result[2]).hasSizeBetween(1, 4).isNotEmpty();
                assertThat(result[3]).hasSize(3);
            }
        }
    }

    @Test
    @Ignore
    void getSpecificExchangeRateForPrint() {
        // just for print
    }

    @Test
    void itShouldGetRefreshedTimeOfRates() {
        // return (output + " 15:00 (UTC+2 Europe/Prague)"); eg. 28.04.2023 + ...
        // given
//        String prefix = "12.05.2023"; // for now, last refreshed
        String postfix = " 15:00 (UTC+2 Europe/Prague)";

        // when
        String result = underTest.getRefreshedTimeOfRates();

        // then
        assertThat(result).isNotEmpty().isNotNull();
        assertThat(result).contains(postfix);
//        assertThat(result).isEqualTo(prefix + postfix);
    }
}
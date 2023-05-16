package backend.bankwebapp.service;

import backend.bankwebapp.model.ExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    private ExchangeRateService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ExchangeRateService();
    }

    @Test
    void itShouldDoExchangeRateCount() {
        // given
        String currencyFrom = "USD";
        double amount = 2;
        double expected = 43.356;

        // when
        double result = underTest.doExchangeRateCount(currencyFrom, amount);

        // then
        assertThat(result).isEqualTo(expected, within(4.0));
    }

    @Test
    void itShouldRefreshFileExchangeRate() {
        // when
        boolean result = underTest.refreshFileExchangeRate();
        // then
        assertThat(result).isTrue();
    }

    @Test
    void itShouldVerifyActualTimeOfExchangeRateAfterRefresh() {
        // given
        String[] edit = exchangeRateRepository.readExchangeRateFile().split("\n");
        String actualDate = edit[1].split(" ")[0] + " 15:00 (UTC+2 Europe/Prague)";

        // when
        underTest.refreshFileExchangeRate();
        String compareDate = exchangeRateRepository.getRefreshedTimeOfRates();

        // then
        assertThat(compareDate).isEqualTo(actualDate);
    }
}

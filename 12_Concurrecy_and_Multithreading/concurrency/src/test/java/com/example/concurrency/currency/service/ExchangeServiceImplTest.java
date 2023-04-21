package com.example.concurrency.currency.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.concurrency.AbstractTestSupporter;
import com.example.concurrency.currency.dao.ExchangeDao;
import com.example.concurrency.currency.exception.NotEnoughMoneyException;
import com.example.concurrency.currency.model.Currency;
import com.example.concurrency.currency.service.impl.ExchangeServiceImpl;
import java.io.File;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@DisplayName("Tests for ExchangeService class")
class ExchangeServiceImplTest extends AbstractTestSupporter {


    @Mock
    private ExchangeDao dao;

    @InjectMocks
    private ExchangeServiceImpl service;

    private static final BigDecimal decimal0 = new BigDecimal("0.0");
    private static final double countMoreThanSumInAccount = decimal111.intValue() + 1;

    private static Stream<Arguments> currencyAndCountMoreThanSumInAccountProvider() {
        return Stream.of(
	       arguments(Currency.usd, Currency.zl, countMoreThanSumInAccount),
	       arguments(Currency.usd, Currency.rubble, countMoreThanSumInAccount),
	       arguments(Currency.rubble, Currency.zl, countMoreThanSumInAccount),
	       arguments(Currency.rubble, Currency.usd, countMoreThanSumInAccount),
	       arguments(Currency.zl, Currency.rubble, countMoreThanSumInAccount),
	       arguments(Currency.zl, Currency.usd, countMoreThanSumInAccount));
    }

    @DisplayName("getCurrencyCount(), should return sum of money in account")
    @ParameterizedTest
    @MethodSource("currencyProvider")
    void getCurrencyCount_realCurrency_correctSumReturned(Currency currency) {
        when(dao.readSumToExchangeFromFile(any(Currency.class))).thenReturn(Optional.of(decimal111));

        final BigDecimal currencyCount = service.getAmountOfMoney(currency);

        assertEquals(currencyCount, decimal111);
    }

    @DisplayName("getMoneyForAllAccounts(), should return money in each account")
    @Test
    void getMoneyForAllAccounts_realCurrency_correctSumForEachAccountReturned() {
        when(dao.readSumToExchangeFromFile(Currency.rubble)).thenReturn(Optional.of(decimal111));
        when(dao.readSumToExchangeFromFile(Currency.usd)).thenReturn(Optional.of(decimal222));
        when(dao.readSumToExchangeFromFile(Currency.zl)).thenReturn(Optional.of(decimal333));

        final Map<Currency, BigDecimal> moneyForAllAccounts = service.getMoneyForAllAccounts();

        assertEquals(moneyForAllAccounts, dummyMoneyInEachAccount);
    }

    @DisplayName("exchangeCurrency(), should invoke methods in DAO layer")
    @ParameterizedTest
    @MethodSource("currencyAndCountProvider")
    void exchangeCurrency_realCurrenciesAndCount_correspondMethodsInDaoInvoked(Currency from,
								        Currency to,
								        double count) {
        when(dao.readSumToExchangeFromFile(any(Currency.class))).thenReturn(Optional.of(decimal111));
        when(dao.writeExchangedSumToFile(any(Currency.class), any(BigDecimal.class))).thenReturn(new File(""));

        service.exchangeCurrency(from, to, count);

        verify(dao, times(2)).readSumToExchangeFromFile(any(Currency.class));
        verify(dao, times(2)).writeExchangedSumToFile(any(Currency.class), any(BigDecimal.class));
    }

    @DisplayName("exchangeCurrency(), should throw NotEnoughMoneyException exception when money in account is 0")
    @ParameterizedTest
    @MethodSource("currencyAndCountProvider")
    void exchangeCurrency_sumMoneyInAccountIs0_exceptionThrown(Currency from,
							 Currency to,
							 double count) {
        when(dao.readSumToExchangeFromFile(any(Currency.class))).thenReturn(Optional.of(decimal0));

        assertThrows(NotEnoughMoneyException.class,
	       () -> service.exchangeCurrency(from, to, count));
    }

    @DisplayName("exchangeCurrency(), should throw NotEnoughMoneyException exception when count to exchange more than money presents  in account")
    @ParameterizedTest
    @MethodSource("currencyAndCountMoreThanSumInAccountProvider")
    void exchangeCurrency_countMoreThanSumInAccount_exceptionThrown(Currency from,
							     Currency to,
							     double count) {
        when(dao.readSumToExchangeFromFile(any(Currency.class))).thenReturn(Optional.of(decimal111));

        assertThrows(NotEnoughMoneyException.class, () -> service.exchangeCurrency(from, to, count));
    }


}
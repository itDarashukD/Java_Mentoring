package com.example.concurrency.currency.util.impl;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.concurrency.AbstractTestSupporter;
import com.example.concurrency.currency.model.Currency;
import com.example.concurrency.currency.service.ExchangeService;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;


@DisplayName("Tests for UserUtilImpl class")
class UserUtilImplTest extends AbstractTestSupporter {


    @Mock
    private ExchangeService service;

    @InjectMocks
    private UserUtilImpl util;


    static Stream<Arguments> currencyAndCountIs0Provider() {
        return Stream.of(
	       arguments(Currency.usd, Currency.zl, dummyCount0),
	       arguments(Currency.usd, Currency.rubble, dummyCount0),
	       arguments(Currency.rubble, Currency.zl, dummyCount0),
	       arguments(Currency.rubble, Currency.usd, dummyCount0),
	       arguments(Currency.zl, Currency.rubble, dummyCount0),
	       arguments(Currency.zl, Currency.usd, dummyCount0));
    }

    @DisplayName("getCurrencyCount(), should invoke getCurrencyCount() in 'service' layer")
    @ParameterizedTest
    @MethodSource("currencyProvider")
    void getCurrencyCount_realCurrency_correspondMethodInServiceLayerInvoked(Currency currency) {
        when(service.getAmountOfMoney(any(Currency.class))).thenReturn(decimal111);

        util.getCurrencyCountForAccount(currency);

        verify(service, times(1)).getAmountOfMoney(currency);
    }

    @Test
    void getMoneyForAllAccounts_correspondMethodInServiceLayerInvoked() {
        when(service.getMoneyForAllAccounts()).thenReturn(dummyMoneyInEachAccount);

        util.getMoneyForAllAccounts();

        verify(service, times(1)).getMoneyForAllAccounts();
    }

    @DisplayName("exchangeCurrency(), should invoke methods 'service' layer")
    @ParameterizedTest
    @MethodSource("currencyAndCountProvider")
    void exchangeCurrency_realCurrenciesAndCount_correspondMethodsInDaoInvoked(Currency from,
								        Currency to,
								        double count) {
        doNothing().when(service)
	       .exchangeCurrency(any(Currency.class), any(Currency.class), anyDouble());

        util.exchangeCurrency(from, to, count);

        verify(service, times(1)).exchangeCurrency(any(Currency.class),
	       any(Currency.class),
	       anyDouble());
    }

    @DisplayName("exchangeCurrency(), should throw exception when count is 0")
    @ParameterizedTest
    @MethodSource("currencyAndCountIs0Provider")
    void exchangeCurrency_countIs0_exceptionThrown(Currency from, Currency to, double count) {
        assertThrows(IllegalArgumentException.class, () -> util.exchangeCurrency(from, to, count));
    }


}


package com.example.concurrency.currency.util.impl;


import com.example.concurrency.ConcurrencyApplication;
import com.example.concurrency.currency.model.Currency;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {ConcurrencyApplication.class})
class RunAllMethodsWithConcurrency {

    @Autowired
    private UserUtilImpl userUtilImpl;

    private static final Logger log = LoggerFactory.getLogger(ConcurrencyApplication.class);

    @Test
    void multithreadingTest() {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        Runnable exchangeUsdRub = () -> {
	   Thread.currentThread().setName("--- UsdRub ---");
	   log.info(String.format("%s has started ", Thread.currentThread().getName()));

	   while (true) {
	       userUtilImpl.exchangeCurrency(Currency.usd, Currency.rubble, 10);
	   }
        };

        Runnable exchangeRubZl = () -> {
	   Thread.currentThread().setName("--- RubZl ---");
	   log.info(String.format("%s has started ", Thread.currentThread().getName()));

	   while (true) {
	       log.info("exchangeRubZl has started");
	       userUtilImpl.exchangeCurrency(Currency.rubble, Currency.zl, 3);
	   }
        };

        Runnable exchangeZlUsd = () -> {
	   Thread.currentThread().setName("--- ZlUsd ---");
	   log.info(String.format("%s has started ", Thread.currentThread().getName()));

	   while (true) {
	       userUtilImpl.exchangeCurrency(Currency.zl, Currency.usd, 25);
	   }
        };

        Runnable getAllAccounts = () -> {
	   Thread.currentThread().setName("--- AllMoney ---");
	   log.info(String.format("%s has started ", Thread.currentThread().getName()));

	   while (true) {

	       final Map<Currency, BigDecimal>
		      moneyForAllAccounts =
		      userUtilImpl.getMoneyForAllAccounts();

	       System.out.println(moneyForAllAccounts);
	   }
        };

        Runnable getSumUsdAccount = () -> {
	   log.info("getSumUsdAccount money has started");
	   final BigDecimal
		  currencyCountForAccount =
		  userUtilImpl.getCurrencyCountForAccount(Currency.usd);

	   System.out.println(currencyCountForAccount);
        };

        Runnable getSumRubbleAccount = () -> {
	   log.info("getSumRubbleAccount money has started");
	   final BigDecimal
		  currencyCountForAccount =
		  userUtilImpl.getCurrencyCountForAccount(Currency.rubble);

	   System.out.println(currencyCountForAccount);
        };

        Runnable getSumZlAccount = () -> {
	   log.info("getSumZlAccount money has started");
	   final BigDecimal
		  currencyCountForAccount =
		  userUtilImpl.getCurrencyCountForAccount(Currency.zl);

	   System.out.println(currencyCountForAccount);
        };

        executor.execute(getAllAccounts);
        executor.execute(exchangeRubZl);
        executor.execute(exchangeUsdRub);
        executor.execute(exchangeZlUsd);
        executor.execute(getSumRubbleAccount);
        executor.execute(getSumUsdAccount);
        executor.execute(getSumZlAccount);
    }


}
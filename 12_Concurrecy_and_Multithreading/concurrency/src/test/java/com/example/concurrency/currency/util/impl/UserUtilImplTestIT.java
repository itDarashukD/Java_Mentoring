package com.example.concurrency.currency.util.impl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.ArgumentMatchers.anyString;

import com.example.concurrency.AbstractTestSupporter;
import com.example.concurrency.ConcurrencyApplication;
import com.example.concurrency.currency.model.Currency;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {ConcurrencyApplication.class})
class UserUtilImplTestIT extends AbstractTestSupporter {

    @Autowired
    private UserUtilImpl userUtilImpl;

    @AfterEach
      void after() {
        returnFilesIntoInitialState();
    }


    @DisplayName("getCurrencyCountForAccount(), should return correct sum on Account for currency ")
    @ParameterizedTest
    @MethodSource("currencyAndPathProvider")
    void getCurrencyCountForAccount_realCurrency_sumInAccountReturned(Currency currency,
							       Path path,
			 				       Optional<BigDecimal> bigDecimal) {
        try (MockedStatic<Paths> dummy = Mockito.mockStatic(Paths.class)) {
	   dummy.when(() -> Paths.get(anyString())).thenReturn(path);

	   final BigDecimal
		  currencyCountForAccount =
		  userUtilImpl.getCurrencyCountForAccount(currency);

	   assertEquals(currencyCountForAccount, bigDecimal.get());
        }
    }

    @DisplayName("getMoneyForAllAccounts(), should return map with sum for each Account")
    @Test
    void getMoneyForAllAccounts_correctSumForEachAccountReturned() {
        final Map<Currency, BigDecimal> moneyForAllAccounts = userUtilImpl.getMoneyForAllAccounts();

        assertEquals(moneyForAllAccounts, dummyMoneyInEachAccount);
    }

    @DisplayName("exchangeCurrency() end to end test, should do exchange for each currency pair")
    @TestFactory
    Collection<DynamicTest> exchangeCurrency_realCurrency_exchangeResulSumOfEachOperationIsCorrect() {

        return Arrays.asList(
	       dynamicTest("1st usd --> rub exchange", () -> {
		  userUtilImpl.exchangeCurrency(Currency.usd, Currency.rubble, 10.0);

		  assertEquals(readFile(Currency.usd), BigDecimal.valueOf(212.222));
		  assertEquals(readFile(Currency.rubble), BigDecimal.valueOf(136.111));
	       }),
	       dynamicTest("2st usd --> zl exchange", () -> {
		  userUtilImpl.exchangeCurrency(Currency.usd, Currency.zl, 11.1);

		  assertEquals(readFile(Currency.usd), BigDecimal.valueOf(201.122));
		  assertEquals(readFile(Currency.zl), BigDecimal.valueOf(383.283));
	       }),
	       dynamicTest("3st rubble --> zl exchange", () -> {
		  userUtilImpl.exchangeCurrency(Currency.rubble, Currency.zl, 11.1);

		  assertEquals(readFile(Currency.rubble), BigDecimal.valueOf(125.011));
		  assertEquals(readFile(Currency.zl), BigDecimal.valueOf(405.483));
	       }),
	       dynamicTest("4th rubble --> usd exchange", () -> {
		  userUtilImpl.exchangeCurrency(Currency.rubble, Currency.usd, 22.22);

		  assertEquals(readFile(Currency.rubble), BigDecimal.valueOf(102.791));
		  assertEquals(readFile(Currency.usd), BigDecimal.valueOf(210.01));
	       }),
	       dynamicTest("5th zl --> usd exchange", () -> {
		  userUtilImpl.exchangeCurrency(Currency.zl, Currency.usd, 33.33);
		  assertEquals(readFile(Currency.zl), BigDecimal.valueOf(372.153));
		  assertEquals(readFile(Currency.usd), BigDecimal.valueOf(217.3426));
	       }),
	       dynamicTest("6th zl --> rubble exchange", () -> {
		  userUtilImpl.exchangeCurrency(Currency.zl, Currency.rubble, 44.44);
		  assertEquals(readFile(Currency.zl), BigDecimal.valueOf(327.713));
		  assertEquals(readFile(Currency.rubble), BigDecimal.valueOf(125.011));
	       })
        );

    }

    private BigDecimal readFile(Currency currency) {
        String fileName = String.format("%s.txt", currency);
        String pathToReadFile = PATH_TO_TEST_FILES_FOLDER + fileName;

        try {
	   Path path = Paths.get(pathToReadFile);
	   return Files.lines(path)
		  .map(string -> BigDecimal.valueOf(Double.parseDouble(string)))
		  .findAny()
		  .get();

        } catch (IOException e) {
	   throw new RuntimeException(e);
        }
    }

    private void returnFilesIntoInitialState() {
         dummyMoneyInEachAccount.forEach((key, value) -> {

	   String fileName = String.format("%s.txt", key.name());
	   String pathToWriteFile = PATH_TO_TEST_FILES_FOLDER + fileName;

	   if (!clearFile(pathToWriteFile)) {
	       throw new IllegalStateException(
		      "Can't save currency count, because file is not clear");
	   }
	   try {
	       Path path = Paths.get(pathToWriteFile);
	       Files.write(path, String.valueOf(value.doubleValue()).getBytes());
	   } catch (IOException e) {
	       throw new RuntimeException(e);
	   }
        });
    }

    private Boolean clearFile(String pathToFile) {
        try {
	   Path path = Paths.get(pathToFile);
	   final BufferedWriter bufferedWriter = Files.newBufferedWriter(path);
	   bufferedWriter.write("");
	   bufferedWriter.flush();

	   return path.toFile().length() == 0;
        } catch (IOException e) {
	   throw new RuntimeException(e);
        }
    }


}


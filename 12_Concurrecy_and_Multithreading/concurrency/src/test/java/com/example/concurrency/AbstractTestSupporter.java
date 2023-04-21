package com.example.concurrency;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.example.concurrency.currency.model.Currency;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AbstractTestSupporter {


    protected static final double dummyCount0 = 0;
    protected static final double dummyCount11 = 11.0;

    protected static final BigDecimal decimal111 = new BigDecimal("111.111");
    protected static final BigDecimal decimal222 = new BigDecimal("222.222");
    protected static final BigDecimal decimal333 = new BigDecimal("333.333");

    protected static final String PATH_TO_TEST_FILES_FOLDER = "src/test/resources/userAccounts/";
    protected static final String PATH_TO_ZL_FILE = "src/test/resources/userAccounts/zl.txt";
    protected static final String PATH_TO_USD_FILE = "src/test/resources/userAccounts/usd.txt";
    protected static final String PATH_TO_RUBBLE_FILE = "src/test/resources/userAccounts/rubble.txt";

    protected static final Map<Currency, BigDecimal> dummyMoneyInEachAccount = new HashMap() {{
        put(Currency.rubble, decimal111);
        put(Currency.usd, decimal222);
        put(Currency.zl, decimal333);
    }};

    protected static Stream<Arguments> currencyProvider() {
        return Stream.of(arguments(Currency.usd),
	       arguments(Currency.rubble),
	       arguments(Currency.zl));
    }

    protected static Stream<Arguments> currencyAndCountProvider() {
        return Stream.of(arguments(Currency.usd, Currency.zl, dummyCount11),
	       arguments(Currency.usd, Currency.rubble, dummyCount11),
	       arguments(Currency.rubble, Currency.zl, dummyCount11),
	       arguments(Currency.rubble, Currency.usd, dummyCount11),
	       arguments(Currency.zl, Currency.rubble, dummyCount11),
	       arguments(Currency.zl, Currency.usd, dummyCount11));
    }

    protected static Stream<Arguments> currencyAndBigDecimalProvider() {
        return Stream.of(arguments(Currency.rubble, decimal111),
	       arguments(Currency.usd, decimal222),
	       arguments(Currency.usd, decimal333));
    }

    protected static Stream<Arguments> currencyAndPathProvider() {
        return Stream.of(arguments(Currency.rubble,
		      Paths.get(PATH_TO_RUBBLE_FILE),
		      Optional.of(decimal111)),
	       arguments(Currency.usd, Paths.get(PATH_TO_USD_FILE), Optional.of(decimal222)),
	       arguments(Currency.usd, Paths.get(PATH_TO_ZL_FILE), Optional.of(decimal333)));
    }


}

package com.example.concurrency.currency.dao.impl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

import com.example.concurrency.AbstractTestSupporter;
import com.example.concurrency.currency.model.Currency;
import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.CleanupMode;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;


@DisplayName("Tests for ExchangeDaoImpl class")
class ExchangeDaoImplTest extends AbstractTestSupporter {


    @TempDir(cleanup = CleanupMode.ON_SUCCESS)
    static Path tempDirectory;

    @InjectMocks
    private ExchangeDaoImpl dao;

    private static final String PATH_TO_EMPTY_FILE = "src/test/resources/userAccounts/empty.txt";
    private static final String PATH_TO_BAD_CONTENT_FILE = "src/test/resources/userAccounts/badContent.txt";
    private static final String PATH_TO_TOO_MUCH_LINES_FILE = "src/test/resources/userAccounts/tooMuchLines.txt";


    @DisplayName("readSumToExchangeFromFile() should read and return correct BigDecimal after readings the files")
    @ParameterizedTest
    @MethodSource("currencyAndPathProvider")
    void readSumToExchangeFromFile_realCurrencyAndPaths_bigDecimalReturned(Currency currency,
						    Path path,
						    Optional<BigDecimal> bigDecimal) {
        try (MockedStatic<Paths> dummy = Mockito.mockStatic(Paths.class)) {
	   dummy.when(() -> Paths.get(anyString())).thenReturn(path);

	   final Optional<BigDecimal> bigDecimalFromFile = dao.readSumToExchangeFromFile(currency);

	   assertEquals(bigDecimalFromFile, bigDecimal);
        }
    }

    @DisplayName("readSumToExchangeFromFile() should throw exception when file is empty")
    @ParameterizedTest
    @MethodSource("currencyProvider")
    void readSumToExchangeFromFile_emptyFile_exceptionThrown(Currency currency) {
        final Path emptyFilePath = Paths.get(PATH_TO_EMPTY_FILE);

        try (MockedStatic<Paths> dummy = Mockito.mockStatic(Paths.class)) {
	   dummy.when(() -> Paths.get(anyString())).thenReturn(emptyFilePath);

	   assertThrows(NoSuchElementException.class, () -> dao.readSumToExchangeFromFile(currency));
        }
    }

    @DisplayName("readSumToExchangeFromFile() should throw exception when too mach lines in the File")
    @ParameterizedTest
    @MethodSource("currencyProvider")
    void readSumToExchangeFromFile_toMuchLinesInFile_exceptionThrown(Currency currency) {
        final Path tooMuchLinesFilePath = Paths.get(PATH_TO_TOO_MUCH_LINES_FILE);

        try (MockedStatic<Paths> dummy = Mockito.mockStatic(Paths.class)) {
	   dummy.when(() -> Paths.get(anyString())).thenReturn(tooMuchLinesFilePath);

	   assertThrows(IllegalArgumentException.class, () -> dao.readSumToExchangeFromFile(currency));
        }
    }


    @DisplayName("readSumToExchangeFromFile() should throw exception when content in file is incorrect")
    @ParameterizedTest
    @MethodSource("currencyProvider")
    void readSumToExchangeFromFile_fileWithBadContent_exceptionThrown(Currency currency) {
        final Path badContentFilePath = Paths.get(PATH_TO_BAD_CONTENT_FILE);

        try (MockedStatic<Paths> dummy = Mockito.mockStatic(Paths.class)) {
	   dummy.when(() -> Paths.get(anyString())).thenReturn(badContentFilePath);

	   assertThrows(NumberFormatException.class, () -> dao.readSumToExchangeFromFile(currency));
        }
    }

    @DisplayName("writeExchangeSumToFile(), should do correct writing to file")
    @ParameterizedTest
    @MethodSource("currencyAndBigDecimalProvider")
    void writeExchangeSumToFile_realCurrencyAndBigDecimal_writtenFileExistAndIncludeContent(Currency currency,
	 								BigDecimal bigDecimal) {
        Path resolve = tempDirectory.resolve("test.txt");
        try (MockedStatic<Paths> dummy = Mockito.mockStatic(Paths.class)) {

	   dummy.when(() -> Paths.get(anyString())).thenReturn(resolve);

	   final File actualFile = dao.writeExchangedSumToFile(currency, bigDecimal);

	   assertTrue(actualFile.exists());
	   assertTrue(actualFile.length() > 0);
	   assertEquals("test.txt", actualFile.getName());
        }
    }


}

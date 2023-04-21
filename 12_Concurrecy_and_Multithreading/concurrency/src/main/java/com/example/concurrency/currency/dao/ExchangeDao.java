package com.example.concurrency.currency.dao;

import com.example.concurrency.currency.model.Currency;
import java.io.File;
import java.math.BigDecimal;
import java.util.Optional;

public interface ExchangeDao {

    File writeExchangedSumToFile(Currency currency, BigDecimal exchangeResulSum);

    Optional<BigDecimal> readSumToExchangeFromFile(Currency currency);

}

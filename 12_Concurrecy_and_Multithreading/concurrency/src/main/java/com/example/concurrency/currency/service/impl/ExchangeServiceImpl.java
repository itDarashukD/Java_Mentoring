package com.example.concurrency.currency.service.impl;

import com.example.concurrency.currency.dao.ExchangeDao;
import com.example.concurrency.currency.exception.NotEnoughMoneyException;
import com.example.concurrency.currency.model.Currency;
import com.example.concurrency.currency.model.ExchangeRate;
import com.example.concurrency.currency.service.ExchangeService;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExchangeServiceImpl implements ExchangeService {


    private static final Logger log = LoggerFactory.getLogger(ExchangeServiceImpl.class);

    private final ExchangeDao dao;

    @Autowired
    public ExchangeServiceImpl(ExchangeDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional
    public void exchangeCurrency(Currency from, Currency to, double count) {
        log.info(String.format("exchangeCurrency... from %s, to %s, count %f ", from, to, count));

        BigDecimal initialSumFrom = getAmountOfMoney(from);

        if (initialSumFrom.compareTo(BigDecimal.ZERO) <= 0 || initialSumFrom.compareTo(
	       BigDecimal.valueOf(count)) < 0) {
	   throw new NotEnoughMoneyException(String.format(
		  "It is not enough money on your: %s account, to do any exchange ", from));
        }

        ExchangeRate exchangeRate = getExchangeRate(from, to);

        BigDecimal exchangeResulSum =
	       BigDecimal.valueOf(count).multiply(BigDecimal.valueOf(exchangeRate.getRate()));

        BigDecimal updatedAmount = reduceMoneyForFromAccount(initialSumFrom, count);
        BigDecimal updatedSumTo = sumUpMoneyForToAccount(exchangeResulSum, to);

        saveCurrency(from, updatedAmount);
        saveCurrency(to, updatedSumTo);
    }

    @Override
    public Map<Currency, BigDecimal> getMoneyForAllAccounts() {
        log.info("getMoneyForAllAccounts...");

        Map<Currency, BigDecimal> moneyOnAccounts = new HashMap<>();
        for (Currency currency : Currency.values()) {
	   final BigDecimal bigDecimal = dao.readSumToExchangeFromFile(currency).get();
	   moneyOnAccounts.put(currency, bigDecimal);
        }
        return moneyOnAccounts;
    }

    @Override
    public BigDecimal getAmountOfMoney(Currency currency) {
        log.info(String.format("getCurrencyCount... for %s ", currency));

        return dao.readSumToExchangeFromFile(currency).get();
    }

    private BigDecimal sumUpMoneyForToAccount(BigDecimal exchangeResulSum, Currency to) {
        BigDecimal initialSumTo = getAmountOfMoney(to);
        return initialSumTo.add(exchangeResulSum);
    }

    private BigDecimal reduceMoneyForFromAccount(BigDecimal initialSumFrom, double count) {
        return initialSumFrom.subtract(BigDecimal.valueOf(count));
    }

    private ExchangeRate getExchangeRate(Currency from, Currency to) {
        return Arrays.stream(ExchangeRate.values())
	       .filter(rate -> rate.name().toLowerCase().startsWith(from.name()))
	       .filter(rate -> rate.name().toLowerCase().contains(to.name()))
	       .findAny()
	       .orElseThrow(() -> new IllegalArgumentException(String.format(
		      "Can't find an Exchange rate for %s and %s currency", from, to)));
    }

    private void saveCurrency(Currency to, BigDecimal exchangeResulSum) {
        log.info(String.format("saveCurrency...sum = %s for %s ", exchangeResulSum, to));

        dao.writeExchangedSumToFile(to, exchangeResulSum);
    }


}

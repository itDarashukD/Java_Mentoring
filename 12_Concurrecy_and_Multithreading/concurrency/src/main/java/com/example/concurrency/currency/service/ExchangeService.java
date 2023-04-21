package com.example.concurrency.currency.service;

import com.example.concurrency.currency.model.Currency;
import java.math.BigDecimal;
import java.util.Map;

public interface ExchangeService {


    BigDecimal getAmountOfMoney(Currency currency);

    Map<Currency, BigDecimal> getMoneyForAllAccounts();

    void exchangeCurrency(Currency from, Currency to, double count);

}

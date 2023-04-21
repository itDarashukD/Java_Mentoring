package com.example.concurrency.currency.util;

import com.example.concurrency.currency.model.Currency;
import java.math.BigDecimal;
import java.util.Map;

public interface UserUtil {


    BigDecimal getCurrencyCountForAccount(Currency currency);

    Map<Currency, BigDecimal> getMoneyForAllAccounts();

    void exchangeCurrency(Currency from, Currency to, double count);

}

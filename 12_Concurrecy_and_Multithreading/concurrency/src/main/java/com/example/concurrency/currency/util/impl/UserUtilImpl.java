package com.example.concurrency.currency.util.impl;

import com.example.concurrency.currency.model.Currency;
import com.example.concurrency.currency.service.ExchangeService;
import com.example.concurrency.currency.util.UserUtil;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserUtilImpl implements UserUtil {


    private ExchangeService service;

    @Autowired
    public UserUtilImpl(ExchangeService service) {
        this.service = service;
    }

    @Override
    public void exchangeCurrency(Currency from, Currency to, double count) {
        if (count <= 0) {
	   throw new IllegalArgumentException("The count must be more than 0");
        }
        service.exchangeCurrency(from, to, count);
    }

    @Override
    public Map<Currency, BigDecimal> getMoneyForAllAccounts() {
        return service.getMoneyForAllAccounts();
    }

    @Override
    public BigDecimal getCurrencyCountForAccount(Currency currency) {
        return service.getAmountOfMoney(currency);
    }

}

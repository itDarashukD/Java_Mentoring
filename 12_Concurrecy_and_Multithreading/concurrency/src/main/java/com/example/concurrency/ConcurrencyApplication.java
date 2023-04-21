package com.example.concurrency;

import com.example.concurrency.currency.dao.ExchangeDao;
import com.example.concurrency.currency.dao.impl.ExchangeDaoImpl;
import com.example.concurrency.currency.model.Currency;
import com.example.concurrency.currency.service.ExchangeService;
import com.example.concurrency.currency.service.impl.ExchangeServiceImpl;
import com.example.concurrency.currency.util.UserUtil;
import com.example.concurrency.currency.util.impl.UserUtilImpl;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConcurrencyApplication {

    private static final Logger log = LoggerFactory.getLogger(ConcurrencyApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(ConcurrencyApplication.class, args);
    }


}

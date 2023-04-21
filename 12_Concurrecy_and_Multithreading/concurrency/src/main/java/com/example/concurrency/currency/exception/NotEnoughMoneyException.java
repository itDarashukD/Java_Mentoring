package com.example.concurrency.currency.exception;

public class NotEnoughMoneyException extends RuntimeException {

    public static final long serialVersionUID = -123123123123L;

    public NotEnoughMoneyException(String cause) {
        super(cause);
    }

}

package org.example.service.exception;


public class SubscriptionNotExistException extends RuntimeException {

    private  final static long serialVersionUID = 12345678901234L;

    public SubscriptionNotExistException(String message) {
        super(message);

    }
}

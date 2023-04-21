package org.example.service.configuration.db;


public class SqlRequests {

    private final String SQL_ADD_BANK_USER = "INSERT INTO BANK_USER (card_number,name,surname,birthday) VALUES (?,?,?,?)";
    private final String SQL_GET_ALL_USERS = "SELECT * FROM BANK_USER";
    private final String SQL_ADD_SUBSCRIPTION = "INSERT INTO SUBSCRIPTION (card_number,subscription_date) VALUES (?,?)";
    private final String SQL_GET_ALL_SUBSCRIPTION = "SELECT * FROM SUBSCRIPTION";
    private final String SQL_GET_SUBSCRIPTION_BY_BANK_CARD_NUMBER = "SELECT * FROM SUBSCRIPTION WHERE card_number=?";


    public String getSQL_ADD_BANK_USER() {
        return SQL_ADD_BANK_USER;

    }

    public String getSQL_GET_ALL_USERS() {
        return SQL_GET_ALL_USERS;

    }

    public String getSQL_ADD_SUBSCRIPTION() {
        return SQL_ADD_SUBSCRIPTION;

    }

    public String getSQL_GET_ALL_SUBSCRIPTION() {
        return SQL_GET_ALL_SUBSCRIPTION;

    }

    public String getSQL_GET_SUBSCRIPTION_BY_BANK_CARD_NUMBER() {
        return SQL_GET_SUBSCRIPTION_BY_BANK_CARD_NUMBER;

    }

}


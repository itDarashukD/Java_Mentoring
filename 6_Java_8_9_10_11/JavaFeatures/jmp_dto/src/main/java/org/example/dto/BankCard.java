package org.example.dto;

import java.util.Objects;

public class BankCard {

    public BankCard() {
    }

    private User user;
    private String number;

    public BankCard(String number, User user) {
        this.user = user;
        this.number = number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BankCard)) return false;
        BankCard bankCard = (BankCard) o;
        return Objects.equals(user, bankCard.user) && Objects.equals(number, bankCard.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, number);
    }
}

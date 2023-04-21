package org.example.serviceApi;

import org.example.dto.User;
import org.example.dto.BankCard;
import org.example.dto.Subscription;

import java.util.List;
import java.util.function.Predicate;

public interface Service {

    void subscribe(BankCard card);

    Subscription getSubscriptionByBankCardNumber(String cardNumber);

    List<User> getAllUsers();

    default double getAverageUsersAge() {
        return 0;
    }

    boolean isPayableUser(User user);

    List<Subscription> getAllSubscriptionsByCondition(Predicate<Subscription> predicate);


}

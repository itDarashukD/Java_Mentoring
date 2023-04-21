package org.example.service.repository;

import java.util.List;
import org.example.dto.User;
import org.example.dto.BankCard;
import org.example.dto.Subscription;


public interface BankCardDao {

    void subscribe(BankCard card);

    Subscription getSubscriptionByBankCardNumber(String cardNumber);

    List<User> getAllUsers();

    List<Subscription> getAllSubscription();
}

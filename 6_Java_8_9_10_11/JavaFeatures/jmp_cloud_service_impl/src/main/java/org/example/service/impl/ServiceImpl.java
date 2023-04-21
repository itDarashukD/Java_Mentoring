package org.example.service.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.example.dto.User;
import org.example.dto.BankCard;
import org.example.dto.Subscription;
import org.example.serviceApi.Service;
import org.example.service.repository.BankCardDao;


public class ServiceImpl implements Service {

    private final static int PAYABLE_AGE = 18;
    private final static int MAXIMUM_YEAR_LIFE = 130;
    private BankCardDao cardDao;


    public ServiceImpl() {
    }

    public ServiceImpl(BankCardDao cardDao) {
        this.cardDao = cardDao;

    }

    @Override
    public void subscribe(BankCard card) {
        cardDao.subscribe(card);

    }

    @Override
    public Subscription getSubscriptionByBankCardNumber(String cardNumber) {
        return cardDao.getSubscriptionByBankCardNumber(cardNumber);
    }

    @Override
    public List<User> getAllUsers() {
        return cardDao.getAllUsers();

    }

    @Override
    public double getAverageUsersAge() {
        var allUsers = getAllUsers();
        var unmodifiableUserList = allUsers.stream().collect(Collectors.toUnmodifiableList());

        return unmodifiableUserList.stream()
                .map(User::getBirthday)
                .map(birthday ->
                {
                    if (birthday.isAfter(LocalDate.now()) || ChronoUnit.YEARS.between(birthday, LocalDate.now()) > MAXIMUM_YEAR_LIFE) {
                        throw new IllegalArgumentException("Birthday is not correct");
                    }
                    return birthday;
                })
                .map(localDate -> ChronoUnit.YEARS.between(localDate, LocalDate.now()))
                .mapToDouble(old -> old)
                .average()
                .orElseThrow(() -> new RuntimeException("The average age of users did not calculated"));

    }

    @Override
    public boolean isPayableUser(User user) {
        return ChronoUnit.YEARS.between(user.getBirthday(), LocalDate.now()) >= PAYABLE_AGE;

    }

    @Override
    public List<Subscription> getAllSubscriptionsByCondition(Predicate<Subscription> predicateDateAfter2015) {
        var allSubscription = cardDao.getAllSubscription();

        return allSubscription.stream()
                .filter(predicateDateAfter2015)
                .collect(Collectors.toList());

    }

}

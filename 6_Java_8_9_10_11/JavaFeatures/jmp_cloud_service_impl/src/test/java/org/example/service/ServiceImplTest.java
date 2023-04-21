package org.example.service;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import org.example.dto.User;
import org.example.dto.BankCard;
import org.example.dto.Subscription;

import org.example.service.impl.ServiceImpl;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.util.function.Predicate;

import java.time.LocalDate;
import org.example.service.repository.BankCardDaoDBImpl;


@DisplayName("tests for ServiceImpl")
@ExtendWith(MockitoExtension.class)
class ServiceImplTest {

    @Mock
    private BankCardDaoDBImpl dao;

    @InjectMocks
    private ServiceImpl service = new ServiceImpl(dao);


    private static final User user1986year = new User("name1", "surName1", LocalDate.of(1986, 1, 1));
    private static final User user2010year = new User("name2", "surName2", LocalDate.of(2010, 3, 3));
    private static final User user2002year = new User("name3", "surName3", LocalDate.of(2002, 2, 2));
    private static final List<User> correctUserYears1986_2010 = new ArrayList<>() {{
        add(user1986year);
        add(user2010year);
        add(user2002year);
    }};

    private static final User user2012year = new User("name7", "surName7", LocalDate.of(2012, 1, 1));
    private static final User user2013year = new User("name8", "surName8", LocalDate.of(2013, 3, 3));
    private static final User user2019year = new User("name9", "surName9", LocalDate.of(2019, 2, 2));
    private static final List<User> correctUserYears2012_2019 = new ArrayList<>() {{
        add(user2012year);
        add(user2013year);
        add(user2019year);
    }};

    private static final User user1829year = new User("name4", "surName4", LocalDate.of(1829, 1, 1));
    private static final User user2050year = new User("name5", "surName5", LocalDate.of(2050, 3, 3));
    private static final User user2023year = new User("name6", "surName6", LocalDate.of(2023, 2, 2));
    private static final List<User> impossibleUserYears = new ArrayList<>() {{
        add(user1829year);
        add(user2050year);
        add(user2023year);
    }};

    private static final Subscription subscription2002 = new Subscription("1111", LocalDate.of(2002, 2, 2));
    private static final Subscription subscription2016 = new Subscription("2222", LocalDate.of(2016, 2, 2));
    private static final Subscription subscription2022 = new Subscription("3333", LocalDate.of(2022, 2, 2));
    private static final Subscription subscription2030 = new Subscription("3333", LocalDate.of(2030, 2, 2));
    private static final List<Subscription> subscriptionsMixYear = new ArrayList<>() {{
        add(subscription2002);
        add(subscription2016);
        add(subscription2022);
        add(subscription2030);
    }};

    private static final List<Subscription> subscriptionsAfter2015Years = new ArrayList<>() {{
        add(subscription2016);
        add(subscription2022);
        add(subscription2030);
    }};


    @DisplayName("is subscribe() in dao will invoke")
    @Test
    void subscribe_isDaoSubscribedInvoked_invoked1Time() {
        BankCard bankCard = new BankCard("card number", user1986year);
        doNothing().when(dao).subscribe(any(BankCard.class));

        dao.subscribe(bankCard);

        verify(dao, times(1)).subscribe(bankCard);

    }


    @DisplayName("getSubscriptionByBankCardNumber(), invoked and return expected value")
    @Test
    void getSubscriptionByBankCardNumber_daoSubscribedInvokedAndReturnedExpectedValue_returnedEqualValue() {
        when(dao.getSubscriptionByBankCardNumber(anyString())).thenReturn(subscription2002);

        Subscription expected = dao.getSubscriptionByBankCardNumber("1111");

        assertEquals(expected, subscription2002);

    }


    @DisplayName("getAllUsers(), invoked and return expected list of users")
    @Test
    void getAllUsers_daoGetAllUsersInvokedAndReturnedExpectedListOfUsers_returnedEqualList() {
        when(dao.getAllUsers()).thenReturn(correctUserYears1986_2010);

        List<User> expected = dao.getAllUsers();

        assertEquals(expected, correctUserYears1986_2010);

    }


    @DisplayName("getAverageUsersAge(), have to return calculated average age of users")
    @ParameterizedTest
    @MethodSource("ListAndDoubleProvider")
    void getAverageUsersAge_withDifferentUserYearsHaveToReturnsAverageAge_returnedExpectedAge(List<User> usersList, double result) {
        when(dao.getAllUsers()).thenReturn(usersList);

        double averageUsersAge = service.getAverageUsersAge();

        assertEquals(averageUsersAge, result, 0.01);

    }


    @DisplayName("getAverageUsersAge(), leverage impossible user age - have to throw Exception")
    @ParameterizedTest
    @MethodSource("ListProvider")
    void getAverageUsersAge_withImpossibleUserYearsHaveToThrowException_exceptionThrown(List<User> usersList) {
        when(dao.getAllUsers()).thenReturn(usersList);

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> service.getAverageUsersAge());

        assertEquals(ex.getMessage(), "Birthday is not correct");

    }


    @DisplayName("isPayableUser(), check is user age more that 18 years, @ParameterizedTest")
    @ParameterizedTest
    @MethodSource("UserAndBooleanProvider")
    void isPayableUser_isUserAgeMoreThan18_returnExpectedBooleansValue(User user, boolean payable) {
        boolean expected = service.isPayableUser(user);

        assertEquals(expected, payable);

    }

    @DisplayName("getAllSubscriptionsByCondition(), check is subscription date is after year 2015")
    @Test
    void getAllSubscriptionsByCondition_returnAllSubscriptionAfterYear2015_returnsAllSubscriptionAfter2015() {
        when(dao.getAllSubscription()).thenReturn(subscriptionsMixYear);
        Predicate<Subscription> predicate = subscription -> subscription
                .getStartDate()
                .isAfter(LocalDate.of(2015, 1, 1));

        List<Subscription> expected = service.getAllSubscriptionsByCondition(predicate);

        assertEquals(expected, subscriptionsAfter2015Years);

    }

    static Stream<Arguments> ListAndDoubleProvider() {
        return Stream.of(
                arguments(correctUserYears1986_2010, 22.66666),
                arguments(correctUserYears2012_2019, 7.333333)
        );

    }

    static Stream<Arguments> ListProvider() {
        return Stream.of(
                arguments(impossibleUserYears)
        );

    }

    static Stream<Arguments> UserAndBooleanProvider() {
        return Stream.of(
                arguments(user1986year, true),
                arguments(user2010year, false),
                arguments(user2002year, true),
                arguments(user2012year, false),
                arguments(user2013year, false),
                arguments(user2019year, false),
                arguments(user1829year, true),
                arguments(user2050year, false),
                arguments(user2023year, false));

    }

}
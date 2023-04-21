package org.example.service.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.*;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

import org.example.dto.User;
import org.example.dto.BankCard;
import org.example.dto.Subscription;
import org.example.service.configuration.db.SqlRequests;
import org.example.service.configuration.db.DBConnection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


@DisplayName("tests for BankCardDaoDBImpl class")
@ExtendWith(MockitoExtension.class)
class BankCardDaoDBImplTest {

    @InjectMocks
    private BankCardDaoDBImpl bankCardDaoDb;

    private static Statement statement;

    private static final DBConnection testDbConnection = DBConnection.getInstance();
    private final static Connection testConnection = testDbConnection.getConnection();
    private static final SqlRequests request = new SqlRequests();

    private final static String CREATE_USER_TABLE = "CREATE TABLE BANK_USER (card_number VARCHAR(255) NOT NULL,name VARCHAR(255) NOT NULL,surname VARCHAR(255) NOT NULL,birthday VARCHAR(255) NOT NULL ,CONSTRAINT PK_BANK_USER  PRIMARY KEY (card_number));";
    private final static String CREATE_SUBSCRIPTION_TABLE = "CREATE TABLE SUBSCRIPTION (card_number VARCHAR(255) NOT NULL,subscription_date VARCHAR(255) NOT NULL, CONSTRAINT PK_SUBSCRIPTION  PRIMARY KEY (card_number));";

    private static final User user2001Year = new User("name1", "surName1", LocalDate.of(2001, 1, 1));
    private static final User user2012Year = new User("name2", "surName2", LocalDate.of(2012, 2, 2));
    private static final User user2003Year = new User("name3", "surName3", LocalDate.of(2003, 3, 3));
    private static final User user2014Year = new User("name4", "surName4", LocalDate.of(2014, 4, 4));
    private static final User user2015Year = new User("name5", "surName5", LocalDate.of(2015, 5, 5));
    private static final List<User> usersList2001To2012Years = new ArrayList<>() {{
        add(user2001Year);
        add(user2012Year);
        add(user2003Year);
    }};

    private static final Subscription subscription2001Year = new Subscription("1111", LocalDate.of(2001, 1, 1));
    private static final Subscription subscription2022Year = new Subscription("2222", LocalDate.of(2022, 2, 2));
    private static final Subscription subscription2013Year = new Subscription("3333", LocalDate.of(2013, 3, 3));
    private static final List<Subscription> subscriptions2001To2022 = new ArrayList<>() {{
        add(subscription2001Year);
        add(subscription2022Year);
        add(subscription2013Year);
    }};

    private static final BankCard cardWithUser2001 = new BankCard("1111", user2001Year);
    private static final BankCard cardWithUser2012 = new BankCard("2222", user2012Year);
    private static final BankCard cardWithUser2003 = new BankCard("3333", user2003Year);
    private static final BankCard cardWithUser2014 = new BankCard("4444", user2014Year);
    private static final BankCard cardWithUser2015 = new BankCard("5555", user2015Year);
    private static final List<BankCard> cardList2001To2012 = new ArrayList<>() {{
        add(cardWithUser2001);
        add(cardWithUser2012);
        add(cardWithUser2003);
    }};

    @BeforeAll
    static void setup() throws SQLException {
        statement = testConnection.createStatement();
        createUserTable();
        createSubscriptionTable();

        addTestValuesToUserTable();
        addTestValuesToSubscriptionTable();

    }

    private static void createUserTable() throws SQLException {
        statement.executeUpdate(CREATE_USER_TABLE);
    }

    private static void createSubscriptionTable() throws SQLException {
        statement.executeUpdate(CREATE_SUBSCRIPTION_TABLE);
    }

    private static void addTestValuesToUserTable() {
        for (BankCard card : cardList2001To2012) {

            try (PreparedStatement prepareStatement = testConnection.prepareStatement(request.getSQL_ADD_BANK_USER())) {
                prepareStatement.setString(1, card.getNumber());
                prepareStatement.setString(2, card.getUser().getName());
                prepareStatement.setString(3, card.getUser().getSureName());
                prepareStatement.setString(4, String.valueOf(card.getUser().getBirthday()));

                prepareStatement.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private static void addTestValuesToSubscriptionTable() {
        for (Subscription subscription : subscriptions2001To2022) {
            try (PreparedStatement prepareStatement = testConnection.prepareStatement(request.getSQL_ADD_SUBSCRIPTION())) {

                prepareStatement.setString(1, subscription.getBankCard());
                prepareStatement.setString(2, String.valueOf(subscription.getStartDate()));

                prepareStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @DisplayName("subscribe(), check will values add to Subscribe table after do subscription ")
    @Test
    void subscribe_addToSubscribeTableAfterSubscribe_subscriptionPresentInSubscribeTable() {
        bankCardDaoDb.subscribe(cardWithUser2014);

        Subscription subscription = bankCardDaoDb.getSubscriptionByBankCardNumber(cardWithUser2014.getNumber());

        assertEquals(subscription.getBankCard(), cardWithUser2014.getNumber());

    }

    @DisplayName("subscribe(), check will values add to BANK_USER table after do subscription ")
    @Test
    void subscribe_addToBankUserTableAfterSubscribe_userPresentInBankUserTable() {
        bankCardDaoDb.subscribe(cardWithUser2015);

        List<User> allUsers = bankCardDaoDb.getAllUsers();

        assertTrue(allUsers.contains(user2015Year));

    }

    @DisplayName("getSubscriptionByBankCardNumber(), check is return expected Subscription by card number ")
    @Test
    void getSubscriptionByBankCardNumber_haveToReturnSubscriptionFromSubscriptionTable_returnExpectedSubscription() {
        Subscription expected = bankCardDaoDb.getSubscriptionByBankCardNumber(cardWithUser2001.getNumber());

        assertEquals(expected, subscription2001Year);

    }

    @DisplayName("getAllUsers(), is return all users from Bank_User table ")
    @Test
    void getAllUsers_haveToReturnAllUsersFromBankUserTable_returnListWithAllUsers() {
        List<User> allUsers = bankCardDaoDb.getAllUsers();

        assertEquals(allUsers, usersList2001To2012Years);

    }

    @DisplayName("getAllSubscription(), is return all subscription from Subscription table ")
    @Test
    void getAllSubscription_haveToReturnAllSubscriptionFromSubscriptionTable_returnListWithAllSubscriptions() {
        List<Subscription> allSubscription = bankCardDaoDb.getAllSubscription();

        assertEquals(allSubscription, subscriptions2001To2022);

    }

}
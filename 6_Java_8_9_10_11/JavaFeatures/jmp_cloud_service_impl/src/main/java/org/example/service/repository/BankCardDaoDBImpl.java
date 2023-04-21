package org.example.service.repository;

import java.sql.*;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;

import org.example.dto.User;
import org.example.dto.BankCard;
import org.example.dto.Subscription;
import org.example.service.configuration.db.DBConnection;
import org.example.service.configuration.db.SqlRequests;


public class BankCardDaoDBImpl implements BankCardDao {

    private final DBConnection dbConnection = DBConnection.getInstance();
    private final Connection connection = dbConnection.getConnection();
    private final SqlRequests request = new SqlRequests();

    @Override
    public void subscribe(BankCard card) {
        addBankUser(card);
        addSubscription(card);
    }

    private void addBankUser(BankCard card) {
        try (PreparedStatement prepareStatement = connection.prepareStatement(request.getSQL_ADD_BANK_USER())) {
            prepareStatement.setString(1, card.getNumber());
            prepareStatement.setString(2, card.getUser().getName());
            prepareStatement.setString(3, card.getUser().getSureName());
            prepareStatement.setString(4, String.valueOf(card.getUser().getBirthday()));

            prepareStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void addSubscription(BankCard card) {
        try (PreparedStatement prepareStatement = connection.prepareStatement(request.getSQL_ADD_SUBSCRIPTION())) {
            prepareStatement.setString(1, card.getNumber());
            prepareStatement.setString(2, String.valueOf(LocalDate.now()));

            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Subscription getSubscriptionByBankCardNumber(String cardNumber) {
        Subscription subscription = new Subscription();

        try (PreparedStatement preparedStatement = connection.prepareStatement(request.getSQL_GET_SUBSCRIPTION_BY_BANK_CARD_NUMBER())) {
            preparedStatement.setString(1, cardNumber);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                subscription.setBankCard(cardNumber);
                subscription.setStartDate(LocalDate.parse(resultSet.getString(2)));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return subscription;

    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(request.getSQL_GET_ALL_USERS());

            while (resultSet.next()) {
                User user = new User();
                user.setName(resultSet.getString(2));
                user.setSureName(resultSet.getString(3));
                user.setBirthday(LocalDate.parse(resultSet.getString(4)));

                userList.add(user);
            }
            return userList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Subscription> getAllSubscription() {
        List<Subscription> subscriptionList = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(request.getSQL_GET_ALL_SUBSCRIPTION());

            while (resultSet.next()) {
                Subscription subscription = new Subscription();
                subscription.setBankCard(resultSet.getString(1));
                subscription.setStartDate(LocalDate.parse(resultSet.getString(2)));

                subscriptionList.add(subscription);
            }
            return subscriptionList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
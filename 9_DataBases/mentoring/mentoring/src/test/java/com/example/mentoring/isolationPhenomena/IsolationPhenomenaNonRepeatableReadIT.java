package com.example.mentoring.isolationPhenomena;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.mentoring.MentoringApplication;
import com.example.mentoring.TestSupporter;
import com.example.mentoring.service.StudentService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;


@DisplayName("tests for IsolationPhenomenaNonRepeatableRead class")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {MentoringApplication.class})
public class IsolationPhenomenaNonRepeatableReadIT extends TestSupporter {


    @Autowired
    private StudentService service;

    @Autowired
    private DataSource dataSource;


    @AfterEach
    void setUp() throws SQLException {
        revertDBInInitialState();
    }

    @DisplayName("readUnCommittedWithNonRepeatableRead(), check if readUnCommitted isolation level resolving NonRepeatableRead")
    @Test
    public void readUnCommittedWithNonRepeatableRead_realDbData_readUnCommittedDidNotResolveNonRepeatableRead() throws SQLException {
        Connection firstConnection = dataSource.getConnection();
        firstConnection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        firstConnection.setAutoCommit(false);

        boolean isResolved = isIsolationLevelResolveNonRepeatableRead(firstConnection);

        assertFalse(isResolved);
    }

    @DisplayName("readCommittedWithNonRepeatableRead(), check if readCommitted isolation level resolving NonRepeatableRead")
    @Test
    public void readCommittedWithNonRepeatableRead_realDbData_readCommittedDidNotResolveNonRepeatableRead() throws SQLException {
        Connection firstConnection = dataSource.getConnection();
        firstConnection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        firstConnection.setAutoCommit(false);

        boolean isResolved = isIsolationLevelResolveNonRepeatableRead(firstConnection);

        assertFalse(isResolved);
    }

    @DisplayName("repeatableReadLevelWithNonRepeatableRead(), check if repeatableRead isolation level resolving NonRepeatableRead")
    @Test
    public void repeatableReadLevelWithNonRepeatableRead_realDbData_repeatableReadResolvedNonRepeatableRead() throws SQLException {
        Connection firstConnection = dataSource.getConnection();
        firstConnection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        firstConnection.setAutoCommit(false);

        boolean isResolved = isIsolationLevelResolveNonRepeatableRead(firstConnection);

        assertTrue(isResolved);
    }

    @DisplayName("serializableLevelWithNonRepeatableRead(), check if serializable isolation level resolving NonRepeatableRead")
    @Test
    public void serializableLevelWithNonRepeatableRead_realDbData_serializableResolvedNonRepeatableRead() throws SQLException {
        Connection firstConnection = dataSource.getConnection();
        firstConnection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        firstConnection.setAutoCommit(false);

        boolean isResolved = isIsolationLevelResolveNonRepeatableRead(firstConnection);

        assertTrue(isResolved);
    }

    private boolean isIsolationLevelResolveNonRepeatableRead(Connection firstConnection) throws SQLException {
        int initialRowsCount = getCountTableRowsWhereAgeMoreThanTestAge(firstConnection);

        int isolationLevel = firstConnection.getTransactionIsolation();
        updateWithAnotherConnection(isolationLevel);

        int rowsCountAfterUpdate = getCountTableRowsWhereAgeMoreThanTestAge(firstConnection);
        firstConnection.commit();

        return initialRowsCount == rowsCountAfterUpdate;
    }

    private int getCountTableRowsWhereAgeMoreThanTestAge(Connection connection) {
        int rowsCount = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ROWS_COUNT_BY_AGE)) {
	   preparedStatement.setInt(1, DUMMY_AGE_15);
	   ResultSet resultSet = preparedStatement.executeQuery();

	   while (resultSet.next()) {
	       rowsCount = resultSet.getInt(1);
	   }
        } catch (SQLException e) {
	   throw new RuntimeException(e);
        }

        return rowsCount;
    }

    private void updateWithAnotherConnection(int isolationLevel) throws SQLException {
        Connection secondConnection = dataSource.getConnection();
        secondConnection.setTransactionIsolation(isolationLevel);
        secondConnection.setAutoCommit(false);

        try (PreparedStatement preparedStatement = secondConnection.prepareStatement(
	       UPDATE_AGE_FOR_NAME)) {
	   preparedStatement.setInt(1, DUMMY_AGE_20);
	   preparedStatement.setString(2, DUMMY_NAME_ULAD);

	   preparedStatement.executeUpdate();
        } catch (SQLException e) {
	   throw new RuntimeException(e);
        }
        secondConnection.commit();
    }


}
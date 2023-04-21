package com.example.mentoring.isolationPhenomena;


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


@DisplayName("tests for IsolationPhenomenaOnDirtyRead class," + "Dirty Read is not using in PostgresQL, so test shows the same results for each Isolation level : PostgresQl resolved DirtyRead issue.")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {MentoringApplication.class})
public class IsolationPhenomenaOnDirtyReadIT extends TestSupporter {

    @Autowired
    private StudentService service;
    @Autowired
    private DataSource dataSource;


    @AfterEach
    void setUp() throws SQLException {
        revertDBInInitialState();
    }

    @DisplayName("readUnCommittedWithDirtyRead(), check if readUnCommitted isolation level resolving DirtyRead")
    @Test
    public void readUnCommittedWithDirtyRead_realDbData_readUnCommittedLevelResolvedDirtyRead() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        connection.setAutoCommit(false);

        boolean isResolved = isIsolationLevelResolveDirtyRead(connection);

        assertTrue(isResolved);
    }

    @DisplayName("readCommittedWithDirtyRead(), check if readCommitted Level isolation level DirtyRead")
    @Test
    public void readCommittedWithDirtyRead_realDbData_readCommittedResolvedDirtyRead() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        connection.setAutoCommit(false);

        boolean isResolved = isIsolationLevelResolveDirtyRead(connection);

        assertTrue(isResolved);
    }

    @DisplayName("repeatableReadLevelWithDirtyRead(), check if repeatableRead isolation level DirtyRead")
    @Test
    public void repeatableReadLevelWithDirtyRead_realDbData_repeatableReadResolvedDirtyRead() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        connection.setAutoCommit(false);

        boolean isResolved = isIsolationLevelResolveDirtyRead(connection);

        assertTrue(isResolved);
    }

    @DisplayName("serializableLevelWithDirtyRead(), check if serializable isolation level resolving DirtyRead")
    @Test
    public void serializableLevelWithDirtyRead_realDbData_serializableResolvedDirtyRead() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        connection.setAutoCommit(false);

        boolean isResolved = isIsolationLevelResolveDirtyRead(connection);

        assertTrue(isResolved);
    }

    private boolean isIsolationLevelResolveDirtyRead(Connection firstConnection) throws SQLException {
        int initialAge = getAge(firstConnection);

        int isolationLevel = firstConnection.getTransactionIsolation();
        Connection secondConnection = updateAgeWithAnotherConnection(isolationLevel);

        int ageAfterUpdating = getAge(firstConnection);
        firstConnection.commit();

        secondConnection.rollback();

        return initialAge == ageAfterUpdating;
    }

    private Connection updateAgeWithAnotherConnection(int isolationLevel) throws SQLException {
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
        return secondConnection;
    }

    private int getAge(Connection connection) throws SQLException {
        int age = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_AGE_FOR_NAME)) {
	   preparedStatement.setString(1, DUMMY_NAME_ULAD);
	   ResultSet resultSet = preparedStatement.executeQuery();

	   while (resultSet.next()) {
	       age = resultSet.getInt(1);
	   }
        } catch (SQLException e) {
	   throw new RuntimeException(e);
        }
        return age;
    }


}
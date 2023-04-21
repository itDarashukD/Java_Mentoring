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


@DisplayName("tests for IsolationPhenomenaPhantom class," + "PostgresQL for Repeatable Read and Serialize implementation does not allow phantom reads")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {MentoringApplication.class})
public class IsolationPhenomenaPhantomIT extends TestSupporter {


    @Autowired
    private StudentService service;

    @Autowired
    private DataSource dataSource;

    @AfterEach
    void setUp() throws SQLException {
        revertDBInInitialStateForIsolation();

    }

    @DisplayName("readUnCommittedWithDirtyRead(), check if readUnCommitted isolation level resolving PhantomRead")
    @Test
    public void readUnCommittedWithPhantomRead_realDbData_readUnCommittedDidNotResolvedPhantom() throws SQLException {
        Connection firstConnection = dataSource.getConnection();
        firstConnection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        firstConnection.setAutoCommit(false);

        boolean isResolved = isIsolationLevelResolvePhantomRead(firstConnection);

        assertFalse(isResolved);
    }

    @DisplayName("readCommittedWithPhantomRead(), check if readCommitted isolation level resolving PhantomRead")
    @Test
    public void readCommittedWithPhantomRead_realDbData_readCommittedDidNotResolvedPhantom() throws SQLException {
        Connection firstConnection = dataSource.getConnection();
        firstConnection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        firstConnection.setAutoCommit(false);

        boolean isResolved = isIsolationLevelResolvePhantomRead(firstConnection);

        assertFalse(isResolved);
    }

    @DisplayName("repeatableReadWithPhantomRead(), check if repeatableRea isolation level resolving PhantomRead")
    @Test
    public void repeatableReadWithPhantomRead_realDbData_repeatableDidNotResolvedPhantom() throws SQLException {
        Connection firstConnection = dataSource.getConnection();
        firstConnection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        firstConnection.setAutoCommit(false);

        boolean isResolved = isIsolationLevelResolvePhantomRead(firstConnection);

        assertTrue(isResolved);
    }

    @DisplayName("serializableLevelWithNonRepeatableRead(), check if serializable isolation level resolving PhantomRead")
    @Test
    public void serializableLevelWithNonRepeatableRead_realDbData_serializableWasResolvePhantom() throws SQLException {
        Connection firstConnection = dataSource.getConnection();
        firstConnection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        firstConnection.setAutoCommit(false);

        boolean isResolved = isIsolationLevelResolvePhantomRead(firstConnection);

        assertTrue(isResolved);
    }

    private boolean isIsolationLevelResolvePhantomRead(Connection firstConnection) throws SQLException {
        int initialSumOfAge = getSumOfAgeForTable(firstConnection);

        int isolationLevel = firstConnection.getTransactionIsolation();
        insertWithAnotherConnection(isolationLevel);

        int sumOfAgeAfterInserting = getSumOfAgeForTable(firstConnection);
        firstConnection.commit();

        return initialSumOfAge == sumOfAgeAfterInserting;
    }

    private int getSumOfAgeForTable(Connection connection) {
        int sumOfAge = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(
	       GET_SUM_OF_AGE_FOR_TABLE)) {
	   ResultSet resultSet = preparedStatement.executeQuery();

	   while (resultSet.next()) {
	       sumOfAge = resultSet.getInt(1);
	   }
        } catch (SQLException e) {
	   throw new RuntimeException(e);
        }
        return sumOfAge;
    }

    private void insertWithAnotherConnection(int isolationLevel) throws SQLException {
        Connection secondConnection = dataSource.getConnection();
        secondConnection.setTransactionIsolation(isolationLevel);
        secondConnection.setAutoCommit(false);

        try (PreparedStatement preparedStatement = secondConnection.prepareStatement(
	       INSERT_INTO_TABLE)) {
	   preparedStatement.setString(1, DUMMY_NAME_OLGA);
	   preparedStatement.setInt(2, DUMMY_AGE_55);
	   preparedStatement.executeUpdate();
        } catch (SQLException e) {
	   throw new RuntimeException(e);
        }
        secondConnection.commit();
    }


}
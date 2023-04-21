package org.example.service.configuration.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DBConnection {

    private static final Logger logger = Logger.getLogger(DBConnection.class.getName());
    private final static String password = "sa";
    private final static String userName = "sa";
    private final static String url = "jdbc:hsqldb:mem:myDb";
    private final static String driver = "org.hsqldb.jdbc.JDBCDriver";
    private static DBConnection instance;
    public Connection connection;


    private DBConnection() {
        open();

    }

    public static DBConnection getInstance() {
        if (instance == null) {
	   instance = new DBConnection();
        }
        return instance;

    }

    public Connection getConnection() {
        return connection;

    }

    public void open() {
        try {
	   Class.forName(driver);
	   this.connection = DriverManager.getConnection(url, userName, password);

        } catch (ClassNotFoundException | SQLException ex) {
	   logger.info("Database Connection creation failed : " + ex.getMessage());
        }

    }

    public void close() throws SQLException {
        if (connection != null) {
	   connection.close();

	   logger.info("Connection was closed");
        }

    }

}

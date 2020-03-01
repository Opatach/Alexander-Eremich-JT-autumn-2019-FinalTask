/*
 * Connection Manager
 *
 * Version 1.0
 *
 * Date 30 Jan 2020
 */

package com.epam.bets.managers;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that manages database connections.
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
public class ConnectionManager {
    /**
     * Database driver name.
     */
    private static final String DATABASE_DRIVER_NAME = "DATABASE_DRIVER_NAME";
    /**
     * Database url.
     */
    private static final String DATABASE_URL = "DATABASE_URL";
    /**
     * Database username.
     */
    private static final String DATABASE_USERNAME = "DATABASE_USERNAME";
    /**
     * Database password.
     */
    private static final String DATABASE_PASSWORD = "DATABASE_PASSWORD";
    /**
     * The maximum number of connections.
     */
    private static final String MAX_NUMBER_OF_CONNECTION =
            "MAX_NUMBER_OF_CONNECTION";

    /**
     * Logger.
     */
    private static Logger log = LogManager.getLogger(ConnectionManager.class);
    /**
     * Connection manager instance.
     */
    private static ConnectionManager instance;

    /**
     * List of connections.
     */
    private List<Connection> connections = new ArrayList<>();

    /**
     * Creates a new connection manager.
     */
    private ConnectionManager() {
        log.info("Connection manager instance created.");
    }

    /**
     * Creates a new connection manager instance or returns an existing one.
     * @return connection manager instance.
     */
    public static synchronized ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
            int connectionCount = Integer.parseInt(ConfigurationManager
                    .getInstance().getProperty(MAX_NUMBER_OF_CONNECTION));
            for (int i = 0; i < connectionCount; i++) {
                instance.connections.add(createConnection());
            }
        }

        return instance;
    }

    /**
     * Create a new connection.
     * @return connection.
     */
    private static Connection createConnection() {
        ConfigurationManager config = ConfigurationManager.getInstance();

        Connection connection = null;

        String driverName = config.getProperty(DATABASE_DRIVER_NAME);
        String url = config.getProperty(DATABASE_URL);
        String username = config.getProperty(DATABASE_USERNAME);
        String password = config.getProperty(DATABASE_PASSWORD);

        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            log.error(e);
        }

        return connection;
    }

    /**
     * @return connection.
     */
    public synchronized Connection getConnection() {
        return connections.remove(0);
    }

    /**
     * Release connection.
     * @param connection connection.
     */
    public synchronized void releaseConnection(Connection connection) {
        connections.add(connection);
    }

    /**
     * Closes all connections.
     */
    public void close() {
        for (Connection connection : connections) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error(e);
            }
        }

        instance = null;
    }
}

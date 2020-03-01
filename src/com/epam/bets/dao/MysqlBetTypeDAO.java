/*
 * MySQL BetTypeDAO
 *
 * Version 1.0
 *
 * Date 30 Jan 2020
 */

package com.epam.bets.dao;

import com.epam.bets.managers.ConfigurationManager;
import com.epam.bets.managers.ConnectionManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.*;

/**
 * MySQL BetTypeDAO
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
public class MysqlBetTypeDAO implements BetTypeDAO {
    /**
     * The name of the property containing
     * SQL command to insert bet type.
     */
    private static final String INSERT_BET_TYPE = "SQL_INSERT_BET_TYPE";
    /**
     * The name of the property containing
     * SQL command to delete bet type.
     */
    private static final String DELETE_BET_TYPE = "SQL_DELETE_BET_TYPE";
    /**
     * The name of the property containing
     * SQL command to update bet type.
     */
    private static final String UPDATE_BET_TYPE = "SQL_UPDATE_BET_TYPE";
    /**
     * The name of the property containing
     * SQL command to find bet type by id.
     */
    private static final String FIND_BET_TYPE_BY_ID =
            "SQL_FIND_BET_TYPE_BY_ID";
    /**
     * The name of the property containing
     * SQL command to find bet type by name.
     */
    private static final String FIND_BET_TYPE_BY_NAME =
            "SQL_FIND_BET_TYPE_BY_NAME";

    /**
     * Logger.
     */
    private static Logger log = LogManager.getLogger(MysqlBetTypeDAO.class);

    @Override
    public int insertBetType(String betType) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(INSERT_BET_TYPE);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int id = -1;

        log.info("Inserting a bet type '" + betType + "'...");

        try {
            preparedStatement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, betType);

            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            id = resultSet.getInt(1);

            log.info(betType + " bet type added with id " + id);
        }
        catch (SQLException e) {
            log.error(e);
        }
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    log.error(e);
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    log.error(e);
                }
            }
        }

        connectionManager.releaseConnection(connection);

        return id;
    }

    @Override
    public String findBetTypeByID(int id) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(FIND_BET_TYPE_BY_ID);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String type = "";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                type = resultSet.getString(1);
            }
        }
        catch (SQLException e) {
            log.error(e);
        }
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    log.error(e);
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    log.error(e);
                }
            }
        }

        connectionManager.releaseConnection(connection);

        return type;
    }

    @Override
    public int findBetTypeByName(String name) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(FIND_BET_TYPE_BY_NAME);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        int id = -1;

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        }
        catch (SQLException e) {
            log.error(e);
        }
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    log.error(e);
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    log.error(e);
                }
            }
        }

        connectionManager.releaseConnection(connection);

        return id;
    }

    @Override
    public boolean updateBetType(int id, String name) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(UPDATE_BET_TYPE);

        PreparedStatement preparedStatement = null;
        int success = 0;

        log.info("Updating bet type...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, id);

            success = preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            log.error(e);
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    log.error(e);
                }
            }
        }

        connectionManager.releaseConnection(connection);

        if (success == 0) {
            log.info("Bet type has not been updated.");
            return false;
        }
        else {
            log.info("Bet type successfully updated.");
            return true;
        }
    }

    @Override
    public boolean deleteBetType(int id) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(DELETE_BET_TYPE);

        PreparedStatement preparedStatement = null;
        int success = 0;

        log.info("Deleting a bet type with id " + id + "...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            success = preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            log.error(e);
        }
        finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    log.error(e);
                }
            }
        }

        connectionManager.releaseConnection(connection);

        if (success == 0) {
            log.info("Bet type with id " + id + " has not been deleted.");
            return false;
        }
        else {
            log.info("Bet type with id " + id + " successfully deleted.");
            return true;
        }
    }
}

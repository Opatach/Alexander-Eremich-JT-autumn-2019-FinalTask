/*
 * MySQL PositionHeldDAO
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
 * MySQL PositionHeldDAO
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
public class MysqlPositionHeldDAO implements PositionHeldDAO {
    /**
     * The name of the property containing
     * SQL command to insert position held.
     */
    private static final String INSERT_POSITION_HELD = "SQL_INSERT_POSITION_HELD";
    /**
     * The name of the property containing
     * SQL command to delete position held.
     */
    private static final String DELETE_POSITION_HELD = "SQL_DELETE_POSITION_HELD";
    /**
     * The name of the property containing
     * SQL command to update position held.
     */
    private static final String UPDATE_POSITION_HELD = "SQL_UPDATE_POSITION_HELD";
    /**
     * The name of the property containing
     * SQL command to find position held by id.
     */
    private static final String FIND_POSITION_HELD_BY_ID =
            "SQL_FIND_POSITION_HELD_BY_ID";
    /**
     * The name of the property containing
     * SQL command to find position held by name.
     */
    private static final String FIND_POSITION_HELD_BY_NAME =
            "SQL_FIND_POSITION_HELD_BY_NAME";

    /**
     * Logger.
     */
    private static Logger log = LogManager.getLogger(MysqlPositionHeldDAO.class);


    @Override
    public int insertPositionHeld(String positionHeld) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(INSERT_POSITION_HELD);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int id = -1;

        log.info("Inserting a position '" + positionHeld + "'...");

        try {
            preparedStatement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, positionHeld);

            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            id = resultSet.getInt(1);

            log.info(positionHeld + " position added with id " + id);
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
    public String findPositionHeldByID(int id) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(FIND_POSITION_HELD_BY_ID);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String position = "";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                position = resultSet.getString(1);
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

        return position;
    }

    @Override
    public int findPositionHeldByName(String name) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(FIND_POSITION_HELD_BY_NAME);

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
    public boolean updatePositionHeld(int id, String name) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(UPDATE_POSITION_HELD);

        PreparedStatement preparedStatement = null;
        int success = 0;

        log.info("Updating position held...");

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
            log.info("Position has not been updated.");
            return false;
        }
        else {
            log.info("Position successfully updated.");
            return true;
        }
    }

    @Override
    public boolean deletePositionHeld(int id) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(DELETE_POSITION_HELD);

        PreparedStatement preparedStatement = null;
        int success = 0;

        log.info("Deleting a position with id " + id + "...");

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
            log.info("Position with id " + id + " has not been deleted.");
            return false;
        }
        else {
            log.info("Position with id " + id + " successfully deleted.");
            return true;
        }
    }
}

/*
 * MySQL BetDAO
 *
 * Version 1.0
 *
 * Date 30 Jan 2020
 */

package com.epam.bets.dao;

import com.epam.bets.horseracing.Bet;
import com.epam.bets.horseracing.Race;
import com.epam.bets.managers.ConfigurationManager;
import com.epam.bets.managers.ConnectionManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * MySQL BetDAO
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
public class MysqlBetDAO implements BetDAO {
    /**
     * The name of the property containing
     * SQL command to insert bet.
     */
    private static final String INSERT_BET = "SQL_INSERT_BET";
    /**
     * The name of the property containing
     * SQL command to delete bet.
     */
    private static final String DELETE_BET = "SQL_DELETE_BET";
    /**
     * The name of the property containing
     * SQL command to update tour.
     */
    private static final String UPDATE_BET = "SQL_UPDATE_BET";
    /**
     * The name of the property containing
     * SQL command to find bet by id.
     */
    private static final String FIND_BET_BY_ID = "SQL_FIND_BET_BY_ID";
    /**
     * The name of the property containing
     * SQL command to find bet by customer id.
     */
    private static final String SELECT_BETS_BY_RACE_ID = "SQL_SELECT_BETS_BY_RACE_ID";

    /**
     * The name of the property containing
     * SQL command to select bets by customer id.
     */
    private static final String SELECT_BETS_BY_CUSTOMER_ID =
            "SQL_SELECT_BETS_BY_CUSTOMER_ID";
    /**
     * The name of the property containing
     * SQL command to select all bets.
     */
    private static final String SELECT_ALL_BETS =
            "SQL_SELECT_ALL_BETS";


    /**
     * Logger
     */
    private static Logger log = LogManager.getLogger(MysqlBetDAO.class);

    /**
     * Create a new bet
     *
     * @param resultSet query result
     * @return bet
     * @throws SQLException an exception that provides information on
     * a database access error or other errors.
     */
    private Bet createBet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        int customerId = resultSet.getInt(2);
        int typeID = resultSet.getInt(3);
        int horseNumber = resultSet.getInt(4);
        int betAmount = resultSet.getInt(5);
        int raceID = resultSet.getInt(6);

        Bet bet = new Bet(horseNumber, betAmount);
        bet.setId(id);

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        if (daoFactory != null) {
            RaceDAO raceDAO = daoFactory.getRaceDAO();
            CustomerDAO customerDAO = daoFactory.getCustomerDAO();

            bet.setType(daoFactory.getBetTypeDAO().findBetTypeByID(typeID));
            bet.setCustomer(customerDAO.findCustomerByID(customerId));
            bet.setRace(raceDAO.findRaceByID(raceID));
        }

        return bet;
    }

    @Override
    public int insertBet(Bet bet) {

        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(INSERT_BET);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int id = -1;
        int typeID = -1;

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        if (daoFactory != null) {
            typeID = daoFactory.getBetTypeDAO().findBetTypeByName(bet.getType());
        }

        log.info("Inserting a bet '" + bet.getType() + "'...");

        try {
            preparedStatement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, bet.getCustomer().getId());
            preparedStatement.setInt(2, typeID);
            preparedStatement.setInt(3, bet.getHorseNumber());
            preparedStatement.setInt(4, bet.getBetAmount());
            preparedStatement.setInt(5, bet.getRace().getId());

            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            id = resultSet.getInt(1);

            log.info(bet.getType() + " bet added with id " + id);
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
    public boolean deleteBet(int id) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(DELETE_BET);

        PreparedStatement preparedStatement = null;
        int success = 0;

        log.info("Deleting a bet with id " + id + "...");

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
            log.info("Bet with id " + id + " has not been deleted.");
            return false;
        }
        else {
            log.info("Bet with id " + id + " successfully deleted.");
            return true;
        }
    }

    @Override
    public Bet findBetByID(int id) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(FIND_BET_BY_ID);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Bet bet = null;

        log.info("Search bet with id " + id + "...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                bet = createBet(resultSet);
                log.info("Bet with id " + id + " successfully found.");
            }
            else {
                log.info("Bet with id " + id + " not found.");
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
        return bet;
    }

    @Override
    public Bet[] selectBetsByCustomerID(int customerId) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(SELECT_BETS_BY_CUSTOMER_ID);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Bet> bets = new LinkedList<>();

        log.info("Search bets with customer id '" + customerId + "'...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, customerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                bets.add(createBet(resultSet));
            }

            log.info(bets.size() + " bets with customer id '" + customerId +
                    "' found.");
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
        return bets.toArray(new Bet[0]);
    }

    @Override
    public Bet[] selectAllBets() {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(SELECT_ALL_BETS);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Bet> bets = new LinkedList<>();

        log.info("Selecting all bets...");

        try {
            preparedStatement = connection.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                bets.add(createBet(resultSet));
            }

            log.info(bets.size() + " bets selected.");
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
        return bets.toArray(new Bet[0]);
    }

    @Override
    public boolean updateBet(Bet bet) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(UPDATE_BET);

        PreparedStatement preparedStatement = null;
        int success = 0;

        log.info("Updating the bet...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, bet.getCustomer().getId());
            preparedStatement.setString(2, bet.getType());
            preparedStatement.setInt(3, bet.getHorseNumber());
            preparedStatement.setInt(4, bet.getBetAmount());
            preparedStatement.setInt(5, bet.getRace().getId());
            preparedStatement.setInt(6, bet.getId());

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
            log.info("Bet has not been updated.");
            return false;
        }
        else {
            log.info("Bet successfully updated.");
            return true;
        }
    }

    @Override
    public Bet[] selectBetsByRaceID(int raceId) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(SELECT_BETS_BY_RACE_ID);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Bet> bets = new LinkedList<>();

        log.info("Search bets with customer id '" + raceId + "'...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, raceId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                bets.add(createBet(resultSet));
            }

            log.info(bets.size() + " bets with race id '" + raceId +
                    "' found.");
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
        return bets.toArray(new Bet[0]);
    }
}

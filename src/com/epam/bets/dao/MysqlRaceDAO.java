/*
 * MySQL RaceDAO
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
import java.util.LinkedList;
import java.util.List;

/**
 * MySQL RaceDAO
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
public class MysqlRaceDAO implements RaceDAO {
    /**
     * The name of the property containing
     * SQL command to insert race.
     */
    private static final String INSERT_RACE = "SQL_INSERT_RACE";
    /**
     * The name of the property containing
     * SQL command to delete race.
     */
    private static final String DELETE_RACE = "SQL_DELETE_RACE";
    /**
     * The name of the property containing
     * SQL command to update race.
     */
    private static final String UPDATE_RACE = "SQL_UPDATE_RACE";
    /**
     * The name of the property containing
     * SQL command to find race by id.
     */
    private static final String FIND_RACE_BY_ID =
            "SQL_FIND_RACE_BY_ID";
    /**
     * The name of the property containing
     * SQL command to find race by name.
     */
    private static final String FIND_RACE_BY_NAME =
            "SQL_FIND_RACE_BY_NAME";
    /**
     * The name of the property containing
     * SQL command to select all races.
     */
    private static final String SELECT_ALL_RACES =
            "SQL_SELECT_ALL_RACES";

    /**
     * Logger
     */
    private static Logger log = LogManager.getLogger(MysqlBetDAO.class);

    /**
     * Create a new race
     *
     * @param resultSet query result
     * @return race
     * @throws SQLException an exception that provides information on
     * a database access error or other errors.
     */
    private Race createRace(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        String name = resultSet.getString(2);
        double coefficient = resultSet.getDouble(3);
        int firstPlace = resultSet.getInt(4);
        int secondPlace = resultSet.getInt(5);
        int thirdPlace = resultSet.getInt(6);

        Race race = new Race(name, coefficient);
        race.setId(id);
        race.setFirstPlace(firstPlace);
        race.setSecondPlace(secondPlace);
        race.setThirdPlace(thirdPlace);

        return race;
    }

    @Override
    public int insertRace(Race race) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(INSERT_RACE);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int id = -1;

        log.info("Inserting a race '" + race.getName() + "'...");

        try {
            preparedStatement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, race.getName());
            preparedStatement.setDouble(2, race.getCoefficient());
            preparedStatement.setInt(3, race.getFirstPlace());
            preparedStatement.setInt(4, race.getSecondPlace());
            preparedStatement.setInt(5, race.getThirdPlace());

            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            id = resultSet.getInt(1);

            log.info(race.getName() + " race added with id " + id);
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
    public boolean deleteRace(int id) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(DELETE_RACE);

        PreparedStatement preparedStatement = null;
        int success = 0;

        log.info("Deleting a race with id " + id + "...");

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
            log.info("Race with id " + id + " has not been deleted.");
            return false;
        }
        else {
            log.info("Race with id " + id + " successfully deleted.");
            return true;
        }
    }

    @Override
    public Race findRaceByID(int id) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(FIND_RACE_BY_ID);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Race race = null;

        log.info("Search race with id " + id + "...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                race = createRace(resultSet);
                log.info("Race with id " + id + " successfully found.");
            }
            else {
                log.info("Race with id " + id + " not found.");
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
        return race;
    }

    @Override
    public Race findRaceByName(String name) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(FIND_RACE_BY_NAME);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Race race = null;

        log.info("Search for a race " + name + "...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                race = createRace(resultSet);
                log.info(name + " successfully found.");
            }
            else {
                log.info(name + " not found.");
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
        return race;
    }

    @Override
    public Race[] selectAllRaces() {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(SELECT_ALL_RACES);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Race> races = new LinkedList<>();

        log.info("Selecting all races...");

        try {
            preparedStatement = connection.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                races.add(createRace(resultSet));
            }

            log.info(races.size() + " races selected.");
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
        return races.toArray(new Race[0]);
    }

    @Override
    public boolean updateRace(Race race) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(UPDATE_RACE);

        PreparedStatement preparedStatement = null;
        int success = 0;

        log.info("Updating the race...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, race.getName());
            preparedStatement.setDouble(2, race.getCoefficient());
            preparedStatement.setInt(3, race.getFirstPlace());
            preparedStatement.setInt(4, race.getSecondPlace());
            preparedStatement.setInt(5, race.getThirdPlace());
            preparedStatement.setInt(6, race.getId());

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
            log.info("Race has not been updated.");
            return false;
        }
        else {
            log.info("Race successfully updated.");
            return true;
        }
    }

}

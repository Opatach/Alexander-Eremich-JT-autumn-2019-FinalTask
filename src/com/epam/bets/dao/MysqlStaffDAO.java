/*
 * MySQL StaffDAO
 *
 * Version 1.0
 *
 * Date 30 Jan 2020
 */

package com.epam.bets.dao;

import com.epam.bets.horseracing.Admin;
import com.epam.bets.horseracing.Bookmaker;
import com.epam.bets.horseracing.Staff;
import com.epam.bets.managers.ConfigurationManager;
import com.epam.bets.managers.ConnectionManager;
import com.epam.bets.servlets.CustomerHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * MySQL StaffDAO
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
public class MysqlStaffDAO implements StaffDAO {

    /**
     * The name of the property containing
     * SQL command to insert staff.
     */
    private static final String INSERT_STAFF = "SQL_INSERT_STAFF";
    /**
     * The name of the property containing
     * SQL command to delete staff.
     */
    private static final String DELETE_STAFF = "SQL_DELETE_STAFF";
    /**
     * The name of the property containing
     * SQL command to update staff.
     */
    private static final String UPDATE_STAFF = "SQL_UPDATE_STAFF";
    /**
     * The name of the property containing
     * SQL command to find staff by id.
     */
    private static final String FIND_STAFF_BY_ID =
            "SQL_FIND_STAFF_BY_ID";
    /**
     * The name of the property containing
     * SQL command to find staff by position held.
     */
    private static final String FIND_STAFF_BY_POSITION =
            "SQL_FIND_STAFF_BY_POSITION";
    /**
     * The name of the property containing
     * SQL command to find staff by email.
     */
    private static final String FIND_STAFF_BY_EMAIL =
            "SQL_FIND_STAFF_BY_EMAIL";

    private static Logger log = LogManager.getLogger(MysqlBetDAO.class);

    /**
     * Create a new staff
     *
     * @param resultSet query result
     * @return staff
     * @throws SQLException an exception that provides information on
     * a database access error or other errors.
     */
    private Staff createStaff(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        String username = resultSet.getString(2);
        String email = resultSet.getString(3);
        String password = resultSet.getString(4);
        int positionHeldID = resultSet.getInt(5);
        String positionHeld = "";

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        if (daoFactory != null) {
            positionHeld = daoFactory.getPositionHeldDAO()
                    .findPositionHeldByID(positionHeldID).toUpperCase();
        }

        Staff staff;

        switch (positionHeld) {
            case "ADMIN":
                staff = new Admin(username, email, password);
                staff.setId(id);
                break;
            case "BOOKMAKER":
                staff = new Bookmaker(username, email, password);
                staff.setId(id);
                break;
            default:
                staff = null;
                break;
        }

        return staff;
    }

    @Override
    public int insertStaff(Staff staff) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(INSERT_STAFF);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int id = -1;
        int positionHeldID = -1;

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        if (daoFactory != null) {
            positionHeldID = daoFactory.getPositionHeldDAO()
                    .findPositionHeldByName(staff.getPositionHeld());
        }

        log.info("Inserting staff '" + staff.getPositionHeld() + "'...");

        try {
            preparedStatement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, staff.getUsername());
            preparedStatement.setString(2, staff.getEmail());
            preparedStatement.setString(3, staff.getPassword());
            preparedStatement.setInt(4, positionHeldID);

            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            id = resultSet.getInt(1);

            log.info(staff.getPositionHeld() + " staff added with id " + id);
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
    public boolean deleteStaff(int id) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(DELETE_STAFF);

        PreparedStatement preparedStatement = null;
        int success = 0;

        log.info("Deleting staff with id " + id + "...");

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
            log.info("Staff with id " + id + " has not been deleted.");
            return false;
        }
        else {
            log.info("Staff with id " + id + " successfully deleted.");
            return true;
        }
    }

    @Override
    public Staff findStaffByID(int id) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(FIND_STAFF_BY_ID);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Staff staff = null;

        log.info("Search staff with id " + id + "...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                staff = createStaff(resultSet);
                log.info("Staff with id " + id + " successfully found.");
            }
            else {
                log.info("Staff with id " + id + " not found.");
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
        return staff;
    }

    @Override
    public Staff findStaffByEmail(String email) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(FIND_STAFF_BY_EMAIL);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Staff staff = null;

        log.info("Search staff with getPositionHeldemail " + email + "...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                staff = createStaff(resultSet);
                log.info("Staff with email " + email + " successfully found.");
            }
            else {
                log.info("Staff with email " + email + " not found.");
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
        return staff;
    }

    @Override
    public Staff[] findStaffByPosition(String positionHeld) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
            .getProperty(FIND_STAFF_BY_POSITION);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Staff> staffList = new LinkedList<>();

        log.info("Search staff '" + positionHeld + "' ...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, positionHeld);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                staffList.add(createStaff(resultSet));
            }

            log.info(staffList.size() + " staff found.");
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
        return staffList.toArray(new Staff[0]);
    }

    @Override
    public boolean updateStaff(Staff staff) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(UPDATE_STAFF);

        PreparedStatement preparedStatement = null;
        int success = 0;
        int positionHeldID = -1;

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        if (daoFactory != null) {
            positionHeldID = daoFactory.getPositionHeldDAO()
                    .findPositionHeldByName(staff.getPositionHeld());
        }

        log.info("Updating staff...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, staff.getUsername());
            preparedStatement.setString(2, staff.getEmail());
            preparedStatement.setString(3, staff.getPassword());
            preparedStatement.setInt(4, positionHeldID);
            preparedStatement.setInt(5, staff.getId());

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
            log.info("Staff has not been updated.");
            return false;
        }
        else {
            log.info("Staff successfully updated.");
            return true;
        }
    }
}

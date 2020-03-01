/*
 * MySQL CustomerDAO
 *
 * Version 1.0
 *
 * Date 30 Jan 2020
 */

package com.epam.bets.dao;

import com.epam.bets.horseracing.Customer;
import com.epam.bets.horseracing.Staff;
import com.epam.bets.managers.ConfigurationManager;
import com.epam.bets.managers.ConnectionManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.*;

/**
 * MySQL CustomerDAO
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
public class MysqlCustomerDAO implements CustomerDAO {

    /**
     * The name of the property containing
     * SQL command to insert customer.
     */
    private static final String INSERT_CUSTOMER = "SQL_INSERT_CUSTOMER";
    /**
     * The name of the property containing
     * SQL command to delete customer.
     */
    private static final String DELETE_CUSTOMER = "SQL_DELETE_CUSTOMER";
    /**
     * The name of the property containing
     * SQL command to update customer.
     */
    private static final String UPDATE_CUSTOMER = "SQL_UPDATE_CUSTOMER";
    /**
     * The name of the property containing
     * SQL command to find customer by id.
     */
    private static final String FIND_CUSTOMER_BY_ID =
            "SQL_FIND_CUSTOMER_BY_ID";
    /**
     * The name of the property containing
     * SQL command to find customer by name.
     */
    private static final String FIND_CUSTOMER_BY_NAME =
            "SQL_FIND_CUSTOMER_BY_NAME";
    /**
     * The name of the property containing
     * SQL command to find customer by email.
     */
    private static final String FIND_CUSTOMER_BY_EMAIL =
            "SQL_FIND_CUSTOMER_BY_EMAIL";

    /**
     * Logger
     */
    private static Logger log = LogManager.getLogger(MysqlCustomerDAO.class);

    /**
     * Create a new customer
     *
     * @param resultSet query result
     * @return customer
     * @throws SQLException an exception that provides information on
     * a database access error or other errors.
     */
    private Customer createCustomer(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        String username = resultSet.getString(2);
        String email = resultSet.getString(3);
        String password = resultSet.getString(4);
        int balance = resultSet.getInt(5);

        Customer customer = new Customer(username, email, password);
        customer.setProfit(balance);
        customer.setId(id);

        return customer;
    }

    @Override
    public int insertCustomer(Customer customer) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(INSERT_CUSTOMER);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int id = -1;

        log.info("Inserting a customer " + customer.getUsername() + "...");

        try {
            preparedStatement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, customer.getUsername());
            preparedStatement.setString(2, customer.getEmail());
            preparedStatement.setString(3, customer.getPassword());
            preparedStatement.setInt(4, customer.getProfit());

            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            id = resultSet.getInt(1);
            customer.setId(id);

            log.info(customer.getUsername() + " added with id " + id);
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
    public boolean deleteCustomer(int id) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(DELETE_CUSTOMER);

        PreparedStatement preparedStatement = null;
        int success = 0;

        log.info("Deleting a customer with id " + id + "...");

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
            log.info("Customer with id " + id + " has not been deleted.");
            return false;
        }
        else {
            log.info("Customer with id " + id + " successfully deleted.");
            return true;
        }
    }

    @Override
    public Customer findCustomerByID(int id) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(FIND_CUSTOMER_BY_ID);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Customer customer = null;

        log.info("Search customer with id " + id + "...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                customer = createCustomer(resultSet);
                log.info("Customer with id " + id + " successfully found.");
            }
            else {
                log.info("Customer with id " + id + " not found.");
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
        return customer;
    }

    @Override
    public Customer findCustomerByEmail(String email) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(FIND_CUSTOMER_BY_EMAIL);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Customer customer = null;

        log.info("Search for a customer with email " + email + "...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                customer = createCustomer(resultSet);
                log.info("Customer with email " + email + " successfully found.");
            }
            else {
                log.info("Customer with email " + email + " not found.");
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
        return customer;
    }

    @Override
    public Customer findCustomerByName(String username) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(FIND_CUSTOMER_BY_NAME);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Customer customer = null;

        log.info("Search for a customer " + username + "...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                customer = createCustomer(resultSet);
                log.info(username + " successfully found.");
            }
            else {
                log.info(username + " not found.");
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
        return customer;
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.getConnection();
        String sql = ConfigurationManager.getInstance()
                .getProperty(UPDATE_CUSTOMER);

        PreparedStatement preparedStatement = null;
        int success = 0;

        log.info("Updating customer...");

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, customer.getUsername());
            preparedStatement.setString(2, customer.getEmail());
            preparedStatement.setString(3, customer.getPassword());
            preparedStatement.setInt(4, customer.getProfit());
            preparedStatement.setInt(5, customer.getId());
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
            log.info("Customer has not been updated.");
            return false;
        }
        else {
            log.info("Customer successfully updated.");
            return true;
        }
    }
}

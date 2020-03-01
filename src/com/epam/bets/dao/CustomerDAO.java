/*
 * CustomerDAO
 *
 * Version 1.0
 *
 * Date 30 Jan 2020
 */

package com.epam.bets.dao;

import com.epam.bets.horseracing.Customer;

/**
 * Interface describing CustomerDAO
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
public interface CustomerDAO {
    /**
     * Insert a new customer.
     *
     * @param customer customer .
     * @return customer id.
     */
    public int insertCustomer(Customer customer);

    /**
     * Delete customer.
     *
     * @param id customer id.
     * @return true if success and false otherwise.
     */
    public boolean deleteCustomer(int id);

    /**
     * Search customer by id.
     *
     * @param id customer id.
     * @return found customer.
     */
    public Customer findCustomerByID(int id);

    /**
     * Search customer by email.
     *
     * @param email customer email.
     * @return found customer.
     */
    public Customer findCustomerByEmail(String email);

    /**
     * Search customer by name.
     *
     * @param username customer first name.
     * @return found customer.
     */
    public Customer findCustomerByName(String username);

    /**
     * Update customer.
     *
     * @param customer updated customer.
     * @return true if success and false otherwise.
     */
    public boolean updateCustomer(Customer customer);
}

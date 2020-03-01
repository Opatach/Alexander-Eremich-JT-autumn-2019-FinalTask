/*
 * Customer
 *
 * Version 1.0
 *
 * Date 30 Jan 2020
 */

package com.epam.bets.horseracing;

import com.epam.bets.dao.BetDAO;
import com.epam.bets.dao.CustomerDAO;
import com.epam.bets.dao.DAOFactory;
import com.epam.bets.dao.RaceDAO;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Customer class.
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
public class Customer extends Person {
    /**
     * Logger
     */
    private static final Logger log = LogManager.getLogger(Customer.class);
    /**
     * Customer profit
     */
    private int profit;

    /**
     * Create a new customer
     *
     * @param username customer username
     * @param email customer email
     * @param password customer password
     */
    public Customer(String username, String email, String password) {
        super(username, email, password);
        profit = 0;
    }

    /**
     * Making a bet
     *
     * @param bet bet
     * @return true if success, false if otherwise
     */
    public boolean makeABet(Bet bet) {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        if (daoFactory == null) {
            log.error("DAO not found.");
            return false;
        }

        BetDAO betDAO = daoFactory.getBetDAO();

        betDAO.insertBet(bet);

        return true;
    }

    /**
     * @return customer profit
     */
    public int getProfit() {
        return profit;
    }

    /**
     * @param profit customer profit
     */
    public void setProfit(int profit) {
        this.profit += profit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Customer customer = (Customer) o;
        return profit == customer.profit;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + profit * 43;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + getId() +
                ", username='" + getUsername() +
                "', email='" + getEmail() +
                "', profit=" + getProfit() +
                '}';
    }
}

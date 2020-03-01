/*
 * Bookmaker
 *
 * Version 1.0
 *
 * Date 30 Jan 2020
 */

package com.epam.bets.horseracing;

import com.epam.bets.dao.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Bookmaker class.
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
public class Bookmaker extends Staff {
    /**
     * Logger
     */
    private static final Logger log = LogManager.getLogger(Bookmaker.class);

    /**
     * Create a new bookmaker
     *
     * @param username bookmaker username
     * @param email bookmaker email
     * @param password bookmaker password
     */
    public Bookmaker(String username, String email, String password) {
        super(username, email, password);
    }

    /**
     * Calculating winnings
     *
     * @param betId bet id
     * @return true if success, false if otherwise
     */
    public boolean calculateWinnings(int betId) {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        if (daoFactory == null) {
            log.error("DAO not found.");
            return false;
        }
        BetDAO betDAO = daoFactory.getBetDAO();
        CustomerDAO customerDAO = daoFactory.getCustomerDAO();

        Bet bet = betDAO.findBetByID(betId);

        if (bet == null) {
            return false;
        }
        else {
            Race race = bet.getRace();
            Customer customer = bet.getCustomer();

            switch (bet.getType().toUpperCase()) {

                case "WIN":
                    if (race.getFirstPlace() != 0 &&
                            race.getFirstPlace() == bet.getHorseNumber()) {
                        customer.setProfit((int) (race.getCoefficient() * bet.getBetAmount() +
                                bet.getBetAmount()));
                    }
                    break;

                case "SHOW":
                    if (race.getFirstPlace() != 0 &&
                            (race.getFirstPlace() == bet.getHorseNumber() ||
                                    race.getSecondPlace() == bet.getHorseNumber() ||
                                    race.getThirdPlace() == bet.getHorseNumber())) {
                        customer.setProfit((int) (race.getCoefficient() * bet.getBetAmount() +
                                bet.getBetAmount()));
                    }
                    break;

                default:
                    customer.setProfit(0);
                    break;
            }
            customerDAO.updateCustomer(customer);
            betDAO.deleteBet(betId);
        }

        return true;
    }

    @Override
    public String getPositionHeld() {
        return "Bookmaker";
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Bookmaker{" +
                "id=" + getId() +
                ", username='" + getUsername() +
                "', email='" + getEmail() +
                "'}";
    }
}

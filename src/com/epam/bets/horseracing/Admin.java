/*
 * Admin
 *
 * Version 1.0
 *
 * Date 30 Jan 2020
 */

package com.epam.bets.horseracing;

import com.epam.bets.dao.DAOFactory;
import com.epam.bets.dao.RaceDAO;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Admin class.
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
public class Admin extends Staff {
    /**
     * Logger
     */
    private static final Logger log = LogManager.getLogger(Admin.class);

    /**
     * Create a new admin
     *
     * @param username admin username
     * @param email admin email
     * @param password admin password
     */
    public Admin(String username, String email, String password) {
        super(username, email, password);

    }

    /**
     * Determination of the winner
     * @param raceId race id
     * @param firstWinner first winner
     * @param secondWinner second winner
     * @param thirdWinner third winner
     * @return true if success, false if otherwise
     */
    public boolean determineTheWinner(int raceId, int firstWinner,
                                      int secondWinner, int thirdWinner) {
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        if (daoFactory == null) {
            log.error("DAO not found.");
            return false;
        }

        RaceDAO raceDAO = daoFactory.getRaceDAO();

        Race race = raceDAO.findRaceByID(raceId);
        if (race == null) {
            return false;
        }

        race.setFirstPlace(firstWinner);
        race.setSecondPlace(secondWinner);
        race.setThirdPlace(thirdWinner);
        raceDAO.updateRace(race);

        return true;
    }

    @Override
    public String getPositionHeld() {
        return "Admin";
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
        return "Administrator{" +
                "id=" + getId() +
                ", username='" + getUsername() +
                "', email='" + getEmail() +
                '}';
    }
}

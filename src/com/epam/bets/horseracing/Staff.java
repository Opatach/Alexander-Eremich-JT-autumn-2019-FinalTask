/*
 * Staff
 *
 * Version 1.0
 *
 * Date 30 Jan 2020
 */

package com.epam.bets.horseracing;

/**
 * Staff class.
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
public abstract class Staff extends Person {

    /**
     * Creates a new staff
     *
     * @param username staff username
     * @param email staff email
     * @param password staff password
     */
    public Staff(String username, String email, String password) {
        super(username, email, password);
    }

    /**
     * @return position held
     */
    public abstract String getPositionHeld();
}

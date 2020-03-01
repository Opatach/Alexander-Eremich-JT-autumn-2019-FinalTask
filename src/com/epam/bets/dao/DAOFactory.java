/*
 * DAO factory
 *
 * Version 1.0
 *
 * Date 30 Jan 2020
 */

package com.epam.bets.dao;

/**
 * DAO factory
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
public abstract class DAOFactory
{
    /**
     * MySQL database.
     */
    public static final int MYSQL = 1;

    /**
     * @return customers DAO.
     */
    public abstract CustomerDAO getCustomerDAO();

    /**
     * @return staff DAO.
     */
    public abstract StaffDAO getStaffDAO();

    /**
     * @return staff position held DAO.
     */
    public abstract PositionHeldDAO getPositionHeldDAO();

    /**
     * @return bet DAO.
     */
    public abstract BetDAO getBetDAO();

    /**
     * @return bet type DAO.
     */
    public abstract BetTypeDAO getBetTypeDAO();

    /**
     * @return race DAO.
     */
    public abstract RaceDAO getRaceDAO();

    /**
     * Creates a DAO factory for the selected database.
     *
     * @param whichFactory database index.
     * @return DAO factory.
     */
    public static DAOFactory getDAOFactory(int whichFactory) {
        switch (whichFactory) {
            case MYSQL:
                return new MysqlDAOFactory();
            default:
                return null;
        }
    }

}

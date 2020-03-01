/*
 * MySQL DAOFactory
 *
 * Version 1.0
 *
 * Date 30 Jan 2020
 */

package com.epam.bets.dao;

/**
 * MySQL DAOFactory
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
public class MysqlDAOFactory extends DAOFactory {

    @Override
    public CustomerDAO getCustomerDAO() {
        return new MysqlCustomerDAO();
    }

    @Override
    public StaffDAO getStaffDAO() {
        return new MysqlStaffDAO();
    }

    @Override
    public PositionHeldDAO getPositionHeldDAO() {
        return new MysqlPositionHeldDAO();
    }

    @Override
    public BetDAO getBetDAO() {
        return new MysqlBetDAO();
    }

    @Override
    public BetTypeDAO getBetTypeDAO() {
        return new MysqlBetTypeDAO();
    }

    @Override
    public RaceDAO getRaceDAO() {
        return new MysqlRaceDAO();
    }
}

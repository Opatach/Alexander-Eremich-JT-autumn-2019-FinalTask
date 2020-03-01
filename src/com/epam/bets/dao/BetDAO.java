/*
 * BetDAO
 *
 * Version 1.0
 *
 * Date 30 Jan 2020
 */

package com.epam.bets.dao;

import com.epam.bets.horseracing.Bet;

/**
 * Interface describing BetDAO
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
public interface BetDAO {

    /**
     * Insert a new bet
     *
     * @param bet bet object
     * @return bet id
     */
    public int insertBet(Bet bet);

    /**
     * Delete bet
     *
     * @param id bet id
     * @return true if success, false otherwise
     */
    public boolean deleteBet(int id);

    /**
     * Search bet by id
     *
     * @param id bet id
     * @return found bet
     */
    public Bet findBetByID(int id);

    /**
     * Select bets by race id
     *
     * @param id race id
     * @return found bets
     */
    public Bet[] selectBetsByRaceID(int id);

    /**
     * Select bets by customer id
     *
     * @param id customer id
     * @return found bets
     */
    public Bet[] selectBetsByCustomerID(int id);

    /**
     * Select all bets
     *
     * @return found bets
     */
    public Bet[] selectAllBets();

    /**
     * Update bet
     *
     * @param bet bet
     * @return true if success, false otherwise
     */
    public boolean updateBet(Bet bet);
}

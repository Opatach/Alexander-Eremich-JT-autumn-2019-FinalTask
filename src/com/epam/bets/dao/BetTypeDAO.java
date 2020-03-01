/*
 * BetTypeDAO
 *
 * Version 1.0
 *
 * Date 30 Jan 2020
 */

package com.epam.bets.dao;

/**
 * Interface describing BetTypeDAO
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
public interface BetTypeDAO {
    /**
     * Insert a new bet type
     *
     * @param betType bet type
     * @return bet type id
     */
    public int insertBetType(String betType);

    /**
     * Search bet type by id
     *
     * @param id bet type id
     * @return found bet type
     */
    public String findBetTypeByID(int id);

    /**
     * Search bet type by name
     *
     * @param name bet type name
     * @return found bet type
     */
    public int findBetTypeByName(String name);

    /**
     * Update bet type
     *
     * @param id  bet type id
     * @param name  bet type name
     * @return true if success, false otherwise
     */
    public boolean updateBetType(int id, String name);

    /**
     * Delete bet type
     *
     * @param id bet type id
     * @return true if success, false otherwise
     */
    public boolean deleteBetType(int id);
}

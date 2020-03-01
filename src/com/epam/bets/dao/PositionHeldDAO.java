/*
 * PositionHeldDAO
 *
 * Version 1.0
 *
 * Date 30 Jan 2020
 */

package com.epam.bets.dao;

/**
 * Interface describing PositionHeldDAO
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
public interface PositionHeldDAO {
    /**
     * Insert a new position held
     *
     * @param positionHeld position held
     * @return position held id
     */
    public int insertPositionHeld(String positionHeld);

    /**
     * Search position held by id
     *
     * @param id position held id
     * @return found position held
     */
    public String findPositionHeldByID(int id);

    /**
     * Search position held by name
     *
     * @param name position held name
     * @return found position held
     */
    public int findPositionHeldByName(String name);

    /**
     * Update position held
     *
     * @param id  position held id
     * @param name  position held name
     * @return true if success, false otherwise
     */
    public boolean updatePositionHeld(int id, String name);

    /**
     * Delete position held
     *
     * @param id position held id
     * @return true if success, false otherwise
     */
    public boolean deletePositionHeld(int id);
}

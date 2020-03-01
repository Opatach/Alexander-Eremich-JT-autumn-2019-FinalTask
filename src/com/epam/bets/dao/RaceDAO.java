/*
 * RaceDAO
 *
 * Version 1.0
 *
 * Date 30 Jan 2020
 */

package com.epam.bets.dao;

import com.epam.bets.horseracing.Race;

/**
 * Interface describing RaceDAO.
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
public interface RaceDAO {
    /**
     * Insert a new race
     *
     * @param race race
     * @return race id
     */
    public int insertRace(Race race);

    /**
     * Delete race
     *
     * @param id race id
     * @return true if success, false otherwise
     */
    public boolean deleteRace(int id);

    /**
     * Search race by id
     *
     * @param id race id
     * @return found race
     */
    public Race findRaceByID(int id);

    /**
     * Search race by name
     *
     * @param name race name
     * @return found race
     */
    public Race findRaceByName(String name);

    /**
     * Select all races by name
     *
     * @return races array
     */
    public Race[] selectAllRaces();

    /**
     * Update race
     *
     * @param race race
     * @return true if success, false otherwise
     */
    public boolean updateRace(Race race);
}

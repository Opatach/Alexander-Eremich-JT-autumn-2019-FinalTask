/*
 * StaffDAO
 *
 * Version 1.0
 *
 * Date 30 Jan 2020
 */

package com.epam.bets.dao;

import com.epam.bets.horseracing.Staff;

/**
 * Interface describing StaffDAO.
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
public interface StaffDAO {
    /**
     * Insert a new staff
     *
     * @param staff staff object
     * @return staff id
     */
    public int insertStaff(Staff staff);

    /**
     * Delete staff
     *
     * @param id staff id
     * @return true if success, false otherwise
     */
    public boolean deleteStaff(int id);

    /**
     * Search staff by id
     *
     * @param id staff id
     * @return found staff
     */
    public Staff findStaffByID(int id);

    /**
     * Search staff by email
     *
     * @param email staff email
     * @return found staff
     */
    public Staff findStaffByEmail(String email);

    /**
     * Search staff by position
     *
     * @param position staff position
     * @return found staff
     */
    public Staff[] findStaffByPosition(String position);

    /**
     * Update staff
     *
     * @param staff updated staff
     * @return true if success, false otherwise
     */
    public boolean updateStaff(Staff staff);
}

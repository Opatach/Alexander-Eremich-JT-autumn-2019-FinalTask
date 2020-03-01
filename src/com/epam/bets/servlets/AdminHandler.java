/*
 * AdminHandler
 *
 * Version 1.0
 *
 * Date 30 Jan 2020
 */

package com.epam.bets.servlets;

import com.epam.bets.dao.*;
import com.epam.bets.horseracing.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Servlet processing admin AJAX requests.
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
@WebServlet("/AdminHandler")
public class AdminHandler extends HttpServlet {
    /**
     * Enum of actions.
     */
    enum Actions {
        /**
         * Add a new race.
         */
        ADD_RACE,
        /**
         * Edit race.
         */
        EDIT_RACE,
        /**
         * Delete race.
         */
        DELETE_RACE,
        /**
         * Determine the winner.
         */
        DETERMINE_THE_WINNER,
        /**
         * Get all races.
         */
        GET_ALL_RACES,
        /**
         * Logout.
         */
        LOGOUT
    }

    /**
     * Logger.
     */
    private static final Logger log = LogManager.getLogger(BookmakerHandler.class);

    /**
     * Race data access object.
     */
    private RaceDAO raceDAO;
    /**
     *Bets data access object.
     */
    private BetDAO betsDAO;

    /**
     * Converts races to JSON array.
     *
     * @param races races.
     * @return JSON array.
     */
    private JSONArray racesToJSONArray(Race[] races) {
        JSONArray racesJSON = new JSONArray();
        JSONObject raceJSON;

        for (Race race : races) {
            raceJSON = new JSONObject();
            raceJSON.put("id", race.getId());
            raceJSON.put("name", race.getName());
            raceJSON.put("coefficient", race.getCoefficient());
            raceJSON.put("firstWinner", race.getFirstPlace());
            raceJSON.put("secondWinner", race.getSecondPlace());
            raceJSON.put("thirdWinner", race.getThirdPlace());
            racesJSON.put(raceJSON);
        }

        return racesJSON;
    }

    /**
     * Return all races.
     *
     * @return all races.
     */
    private JSONObject getAllRaces() {
        JSONObject res = new JSONObject();

        Race[] races = raceDAO.selectAllRaces();

        res.put("success", true);
        res.put("races", racesToJSONArray(races));

        return res;
    }

    /**
     * Add a new race.
     *
     * @param req JSON request.
     * @return JSON response.
     */
    private JSONObject addRace(JSONObject req) {
        JSONObject res = new JSONObject();

        String name = req.getString("name");
        double coefficient = req.getDouble("coefficient");

        try {
            Race race = new Race(name, coefficient);
            raceDAO.insertRace(race);
            res.put("success", true);

        } catch (Exception e) {
            log.error(e);
            res.put("success", false);
            res.put("msg", "Race has not been added.");
        }

        return res;
    }

    /**
     * Edit race.
     *
     * @param req JSON request.
     * @return JSON response.
     */
    private JSONObject editRace(JSONObject req) {
        JSONObject res = new JSONObject();

        int raceID = req.getInt("raceID");
        String name = req.getString("name");
        double coefficient = req.getDouble("coefficient");
        int firstWinner = req.getInt("firstWinner");
        int secondWinner = req.getInt("secondWinner");
        int thirdWinner = req.getInt("thirdWinner");

        try {
            Race race = new Race(name, coefficient);
            race.setId(raceID);
            race.setFirstPlace(firstWinner);
            race.setSecondPlace(secondWinner);
            race.setThirdPlace(thirdWinner);

            if (raceDAO.updateRace(race)) {
                res.put("success", true);
            }
            else {
                res.put("success", false);
                res.put("msg", "Race has not been updated.");
            }
        } catch (Exception e) {
            log.error(e);
            res.put("success", false);
            res.put("msg", "Race has not been added.");
        }

        return res;
    }

    /**
     * Delete race.
     *
     * @param req JSON request.
     * @return JSON response.
     */
    private JSONObject deleteRace(JSONObject req) {
        JSONObject res = new JSONObject();
        int id = req.getInt("raceID");
        Bet[] bets;
        if (raceDAO.deleteRace(id)) {
            bets = betsDAO.selectBetsByRaceID(id);
            for (Bet bet : bets) {
                betsDAO.deleteBet(bet.getId());
            }
            res.put("success", true);
        }
        else {
            res.put("success", false);
            res.put("msg", "Race has not been deleted.");
        }

        return res;
    }

    /**
     * Determine the winner.
     *
     * @param req JSON request.
     * @return JSON response.
     */
    private JSONObject determineTheWinner(JSONObject req, HttpSession session) {
        JSONObject res = new JSONObject();
        Admin admin = (Admin) session.getAttribute("user");

        int id = req.getInt("raceID");
        int firstWinner = req.getInt("firstWinner");
        int secondWinner = req.getInt("secondWinner");
        int thirdWinner = req.getInt("thirdWinner");

        if (admin.determineTheWinner(id, firstWinner, secondWinner, thirdWinner)) {

            res.put("success", true);
        }
        else {
            res.put("success", false);
            res.put("msg", "You can not determine the winner for this race.");
        }

        return res;
    }

    @Override
    public void init() throws ServletException {
        super.init();

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
        if (daoFactory == null) {
            log.fatal("DAO not found.");
            return;
        }

        raceDAO = daoFactory.getRaceDAO();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        String reqBody = req.getReader().lines().collect(
                Collectors.joining(System.lineSeparator()));
        JSONObject reqJSON = new JSONObject(reqBody);
        JSONObject resJSON;

        Actions action = Actions.valueOf(
                reqJSON.getString("action").toUpperCase());

        switch (action) {
            case ADD_RACE:
                resJSON = addRace(reqJSON);
                break;
            case EDIT_RACE:
                resJSON = editRace(reqJSON);
                break;
            case DELETE_RACE:
                resJSON = deleteRace(reqJSON);
                break;
            case GET_ALL_RACES:
                resJSON = getAllRaces();
                break;
            case DETERMINE_THE_WINNER:
                resJSON = determineTheWinner(reqJSON, session);
                break;
            case LOGOUT:
                session.setAttribute("user", null);
                resJSON = new JSONObject();
                resJSON.put("success", true);
                resJSON.put("url", "../index.html");
                break;
            default:
                resJSON = new JSONObject();
                resJSON.put("success", false);
                resJSON.put("msg", "Unknown action.");
                break;
        }

        resp.setContentType("application/json;charset=utf-8");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Pragma", "no-cache");
        resp.getWriter().write(resJSON.toString());
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}

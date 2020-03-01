/*
 * BookmakerHandler
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
 * Servlet processing bookmaker AJAX requests.
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
@WebServlet("/BookmakerHandler")
public class BookmakerHandler extends HttpServlet {
    /**
     * Enum of actions.
     */
    enum Actions {
        /**
         * Setting bet win amount.
         */
        SET_WIN,
        /**
         * Get all bets.
         */
        GET_ALL_BETS,
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
     * Bet data access object.
     */
    private BetDAO betDAO;


    /**
     * Converts bets to JSON array.
     *
     * @param bets bets.
     * @return JSON array.
     */
    private JSONArray betsToJSONArray(Bet[] bets) {
        JSONArray betsJSON = new JSONArray();
        JSONObject betJSON;

        for (Bet bet : bets) {
            betJSON = new JSONObject();
            betJSON.put("id", bet.getId());
            betJSON.put("customerID", bet.getCustomer().getId());
            betJSON.put("betType", bet.getType());
            betJSON.put("horseNumber", bet.getHorseNumber());
            betJSON.put("betAmount", bet.getBetAmount());
            betJSON.put("raceID", bet.getRace().getId());
            betsJSON.put(betJSON);
        }

        return betsJSON;
    }

    /**
     * Set win amount.
     *
     * @param req JSON request.
     * @return JSON response.
     */
    private JSONObject setWinAmount(JSONObject req, HttpSession session) {
        JSONObject res = new JSONObject();
        Bookmaker bookmaker = (Bookmaker) session.getAttribute("user");

        int id = req.getInt("betID");

        if (bookmaker.calculateWinnings(id)) {
            res.put("success", true);
            res.put("msg", "Win amount successfully set.");
        }
        else {
            res.put("success", false);
            res.put("msg", "You can not set win amount for this bet.");
        }

        return res;
    }

    /**
     * Return all bets.
     *
     * @return all bets.
     */
    private JSONObject getAllBets() {
        JSONObject res = new JSONObject();

        Bet[] bets = betDAO.selectAllBets();

        res.put("success", true);
        res.put("bets", betsToJSONArray(bets));

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

        betDAO = daoFactory.getBetDAO();
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
            case SET_WIN:
                resJSON = setWinAmount(reqJSON, session);
                break;
            case GET_ALL_BETS:
                resJSON = getAllBets();
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

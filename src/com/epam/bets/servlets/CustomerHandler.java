/*
 * CustomerHandler
 *
 * Version 1.0
 *
 * Date 30 Jan 2020
 */

package com.epam.bets.servlets;

import com.epam.bets.dao.*;
import com.epam.bets.horseracing.*;
import com.epam.bets.managers.ConnectionManager;
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
 * Servlet processing customer AJAX requests.
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
@WebServlet("/CustomerHandler")
public class CustomerHandler extends HttpServlet {
    /**
     * Enum of user types.
     */
    enum UserTypes {
        /**
         * Admin user.
         */
        ADMIN,
        /**
         * Bookmaker user.
         */
        BOOKMAKER,
        /**
         * Customer user.
         */
        CUSTOMER
    }
    /**
     * Enum of actions.
     */
    enum Actions {
        /**
         * Register a new user.
         */
        REGISTER,
        /**
         * Login.
         */
        LOGIN,
        /**
         * Make a bet.
         */
        MAKE_A_BET,
        /**
         * Add money.
         */
        ADD_MONEY,
        /**
         * Logout.
         */
        LOGOUT,
        /**
         * Get customer bets.
         */
        GET_CUSTOMER_BETS,
        /**
         * Get all races.
         */
        GET_ALL_RACES
    }

    /**
     * Bookmaker page address.
     */
    private static final String BOOKMAKER_PAGE = "./pages/bookmaker.html";

    /**
     * Admin page address.
     */
    private static final String ADMIN_PAGE = "./pages/admin.html";

    /**
     * Logger.
     */
    private static final Logger log = LogManager.getLogger(CustomerHandler.class);

    /**
     * Race data access object.
     */
    private RaceDAO raceDAO;
    /**
     * Bet data access object.
     */
    private BetDAO betDAO;
    /**
     * Customer data access object.
     */
    private CustomerDAO customerDAO;
    /**
     * Staff data access object.
     */
    private StaffDAO staffDAO;

    public CustomerHandler() {
        super();
    }


    /**
     * Converts a customer to JSON object.
     *
     * @param customer customer.
     * @return JSON object.
     */
    private JSONObject customerToJSON(Customer customer) {
        JSONObject user = new JSONObject();
        user.put("name", customer.getUsername());
        user.put("balance", customer.getProfit());
        return user;
    }

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
     * Login bookmaker.
     *
     * @param bookmaker bookmaker.
     * @return JSON response.
     */
    private JSONObject loginBookmaker(Bookmaker bookmaker) {
        JSONObject res = new JSONObject();
        JSONObject user = new JSONObject();
        user.put("name", bookmaker.getUsername());
        res.put("success", true);
        res.put("user", user);
        return res;
    }

    /**
     * Login admin.
     *
     * @param admin admin.
     * @return JSON response.
     */
    private JSONObject loginAdmin(Admin admin) {
        JSONObject res = new JSONObject();
        JSONObject user = new JSONObject();
        user.put("name", admin.getUsername());
        res.put("success", true);
        res.put("user", user);
        return res;
    }

    /**
     * Login customer.
     *
     * @param customer customer.
     * @return JSON response.
     */
    private JSONObject loginCustomer(Customer customer) {
        JSONObject res = new JSONObject();
        res.put("success", true);
        res.put("user", customerToJSON(customer));
        return res;
    }

    /**
     * Register a new bookmaker.
     *
     * @param bookmaker new bookmaker.
     * @return JSON response.
     */
    private JSONObject registerBookmaker(Bookmaker bookmaker) {
        JSONObject res = new JSONObject();

        if (staffDAO.insertStaff(bookmaker) == -1) {
            res.put("success", false);
            res.put("msg", "Bookmaker has not been inserted.");
        }
        else {
            res = loginBookmaker(bookmaker);
        }

        return res;
    }

    /**
     * Register a new admin.
     *
     * @param admin new admin.
     * @return JSON response.
     */
    private JSONObject registerAdmin(Admin admin) {
        JSONObject res = new JSONObject();

        if (staffDAO.insertStaff(admin) == -1) {
            res.put("success", false);
            res.put("msg", "Admin has not been inserted.");
        }
        else {
            res = loginAdmin(admin);
        }

        return res;
    }

    /**
     * Register a new customer.
     *
     * @param customer new customer.
     * @return JSON response.
     */
    private JSONObject registerCustomer(Customer customer) {
        JSONObject res = new JSONObject();

        if (customerDAO.insertCustomer(customer) == -1) {
            res.put("success", false);
            res.put("msg", "Customer has not been inserted.");
        }
        else {
            res = loginCustomer(customer);
        }


        return res;
    }

    /**
     * Register a new user.
     *
     * @param req JSON request.
     * @param session session.
     * @return JSON response.
     */
    private JSONObject register(JSONObject req, HttpSession session) {
        JSONObject res = new JSONObject();

        String username = req.getString("username");
        String email = req.getString("email");
        String password = req.getString("password");
        UserTypes userType = UserTypes.valueOf(
                req.getString("userType").toUpperCase());

        if (staffDAO.findStaffByEmail(email) == null &&
                customerDAO.findCustomerByEmail(email) == null) {

            switch (userType) {
                case CUSTOMER:
                    Customer customer = new Customer(username, email, password);
                    res = registerCustomer(customer);
                    if (res.getBoolean("success")) {
                        session.setAttribute("user", customer);
                    }
                    break;
                case BOOKMAKER:
                    Staff bookmaker = new Bookmaker(username, email, password);
                    res = registerBookmaker((Bookmaker) bookmaker);
                    if (res.getBoolean("success")) {
                        session.setAttribute("user", bookmaker);
                    }
                    break;
                case ADMIN:
                    Staff admin = new Admin(username, email, password);
                    res = registerAdmin((Admin) admin);
                    if (res.getBoolean("success")) {
                        session.setAttribute("user", admin);
                    }
                    break;
            }
        }
        else {
            res.put("success", false);
            res.put("msg", "User already registered.");
        }

        return res;
    }

    /**
     * Login.
     *
     * @param req JSON request.
     * @param session session.
     * @return JSON response.
     */
    private JSONObject login(JSONObject req, HttpSession session) {
        JSONObject res = new JSONObject();
        String email = req.getString("email");
        String password = req.getString("password");
        UserTypes userType = UserTypes.valueOf(
                req.getString("userType").toUpperCase());

        switch (userType) {
            case CUSTOMER:
                Customer customer = customerDAO.findCustomerByEmail(email);
                if (customer != null) {
                    if (customer.getPassword().equals(password)) {
                        res = loginCustomer(customer);
                        if (res.getBoolean("success")) {
                            session.setAttribute("user", customer);
                        }
                    }
                    else {
                        res.put("success", false);
                        res.put("msg", "Wrong password");
                    }
                    return res;
                }
                break;
            case BOOKMAKER:
                Staff bookmaker = staffDAO.findStaffByEmail(email);
                if (bookmaker != null) {
                    if (bookmaker.getPassword().equals(password)) {
                        res = loginBookmaker((Bookmaker) bookmaker);
                        if (res.getBoolean("success")) {
                            session.setAttribute("user", bookmaker);
                        }
                    }
                    else {
                        res.put("success", false);
                        res.put("msg", "Wrong password");
                    }
                    return res;
                }
                break;
            case ADMIN:
                Staff admin = staffDAO.findStaffByEmail(email);
                if (admin != null) {
                    if (admin.getPassword().equals(password)) {
                        res = loginAdmin((Admin) admin);
                        if (res.getBoolean("success")) {
                            session.setAttribute("user", admin);
                        }
                    }
                    else {
                        res.put("success", false);
                        res.put("msg", "Wrong password");
                    }
                    return res;
                }
                break;
        }

        res.put("success", false);
        res.put("msg", "User not found.");

        return res;
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
     * Return all customer bets.
     *
     * @param session session
     * @return all customer bets.
     */
    private JSONObject getCustomerBets(HttpSession session) {
        JSONObject res = new JSONObject();
        Customer customer = (Customer) session.getAttribute("user");

        Bet[] bets = betDAO.selectBetsByCustomerID(customer.getId());

        res.put("success", true);
        res.put("bets", betsToJSONArray(bets));

        return res;
    }

    /**
     * Add money.
     *
     * @param req JSON request.
     * @return JSON response.
     */
    private JSONObject addMoney(JSONObject req, HttpSession session) {
        JSONObject res = new JSONObject();
        Customer customer = (Customer) session.getAttribute("user");
        if(customer != null) {
            customer.setProfit(req.getInt("amount"));
            if (customerDAO.updateCustomer(customer)) {
                res.put("success", true);
                res.put("user", customerToJSON(customer));
            } else {
                customer.setProfit(-req.getInt("amount"));
                res.put("success", false);
                res.put("msg", "Customer has not been updated.");
            }
        }
        return res;
    }

    /**
     * Making a bet.
     *
     * @param req JSON request.
     * @param session session.
     * @return JSON response.
     */
    private JSONObject makeABet(JSONObject req, HttpSession session) {
        JSONObject res = new JSONObject();
        Customer customer = (Customer) session.getAttribute("user");

        Bet bet = new Bet(req.getInt("horseNumber"), req.getInt("betAmount"));
        Race race = raceDAO.findRaceByID(req.getInt("raceNumber"));
        bet.setCustomer(customer);
        bet.setType(req.getString("betType"));

        if (bet != null && race != null) {
            bet.setRace(race);
            if (customer.getProfit() > bet.getBetAmount()) {
                if (customer.makeABet(bet)) {
                    customer.setProfit(-bet.getBetAmount());
                    customerDAO.updateCustomer(customer);
                    res.put("success", true);
                    res.put("user", customerToJSON(customer));
                }
            }
            else {
                res.put("success", false);
                res.put("msg", "Not enough money");
            }
        }
        else {
            res.put("success", false);
            res.put("msg", "Race isn't exist");
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
        betDAO = daoFactory.getBetDAO();
        customerDAO = daoFactory.getCustomerDAO();
        staffDAO = daoFactory.getStaffDAO();
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
        Person user = (Person) session.getAttribute("user");

        switch (action) {
            case REGISTER:
                resJSON = register(reqJSON, session);
                user = (Person) session.getAttribute("user");
                if (user != null && user.getClass() == Bookmaker.class) {
                    resJSON.put("success", true);
                    resJSON.put("url", BOOKMAKER_PAGE);
                }
                if (user != null && user.getClass() == Admin.class) {
                    resJSON.put("success", true);
                    resJSON.put("url", ADMIN_PAGE);
                }
                break;

            case LOGIN:
                resJSON = login(reqJSON, session);
                user = (Person) session.getAttribute("user");
                if (user != null && user.getClass() == Bookmaker.class) {
                    resJSON.put("success", true);
                    resJSON.put("url", BOOKMAKER_PAGE);
                }
                if (user != null && user.getClass() == Admin.class) {
                    resJSON.put("success", true);
                    resJSON.put("url", ADMIN_PAGE);
                }
                break;

            case LOGOUT:
                session.setAttribute("user", null);
                resJSON = new JSONObject();
                resJSON.put("success", true);
                break;
            case MAKE_A_BET:
                if (user != null && user.getClass() == Customer.class) {
                    resJSON = makeABet(reqJSON, session);
                }
                else {
                    resJSON = new JSONObject();
                    resJSON.put("success", false);
                    resJSON.put("msg", "You are not authorized.");
                }
                break;

            case ADD_MONEY:
                if (user != null && user.getClass() == Customer.class) {
                    resJSON = addMoney(reqJSON, session);
                }
                else {
                    resJSON = new JSONObject();
                    resJSON.put("success", false);
                    resJSON.put("msg", "You are not authorized.");
                }
                break;

            case GET_CUSTOMER_BETS:
                if (user != null && user.getClass() == Customer.class) {
                    resJSON = getCustomerBets(session);
                }
                else {
                    resJSON = new JSONObject();
                    resJSON.put("success", false);
                    resJSON.put("msg", "You are not authorized.");
                }
                break;

            case GET_ALL_RACES:
                resJSON = getAllRaces();

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

        ConnectionManager.getInstance().close();
    }
}

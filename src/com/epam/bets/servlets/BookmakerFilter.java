/*
 * BookmakerFilter
 *
 * Version 1.0
 *
 * Date 30 Jan 2020
 */
package com.epam.bets.servlets;

import com.epam.bets.horseracing.Bookmaker;
import com.epam.bets.horseracing.Person;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * The filter prevents users from accessing the page.
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
@WebFilter(urlPatterns = { "/pages/bookmaker.html", "/BookmakerHandler"})
public class BookmakerFilter implements Filter {
    /**
     * Logger.
     */
    private static final Logger log = LogManager.getLogger(AdminFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        log.info("doFilter...");
        HttpSession session =((HttpServletRequest) req).getSession();
        Person user = (Person) session.getAttribute("user");
        if (user == null || user.getClass() != Bookmaker.class) {
            log.info("Sending redirect...");
            ((HttpServletResponse) res).sendRedirect("../index.html");
            return;
        }

        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}

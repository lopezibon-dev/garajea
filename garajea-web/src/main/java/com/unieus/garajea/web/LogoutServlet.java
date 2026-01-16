package com.unieus.garajea.web;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    // -----------------------------------------------------------------
    // DOGET (Bistak erakusteko)
    // -----------------------------------------------------------------
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //String pathInfo = request.getPathInfo(); // Adib: /logout
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath() + "/");

        // if (pathInfo.equalsIgnoreCase("/logout")) {
        //     // Saioa amaitu eta hasierara berbideratu
        //     request.getSession().invalidate();
        //     response.sendRedirect(request.getContextPath() + "/");
        // } else {
        //     response.sendError(HttpServletResponse.SC_NOT_FOUND, "Bide ezezaguna.");
        // }
    }
}

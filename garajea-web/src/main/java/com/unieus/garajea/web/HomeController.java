package com.unieus.garajea.web;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class HomeController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        // Aquí se podría cargar algún dato dinámico si fuera necesario
        RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
        dispatcher.forward(request, response);
    }
}


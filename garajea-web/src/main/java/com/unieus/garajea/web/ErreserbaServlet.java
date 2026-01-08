package com.unieus.garajea.web;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import com.unieus.garajea.core.services.context.ServiceContext;
import com.unieus.garajea.core.services.context.ServiceContextFactory;
import com.unieus.garajea.model.dto.ErreserbaInfoDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/erreserbak/*") // /erreserbak/..., etab.
public class ErreserbaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo(); // Adib: /..., etab.

        ServiceContextFactory scf =
        (ServiceContextFactory) getServletContext()
            .getAttribute("serviceContextFactory");

        try (ServiceContext ctx = scf.open()) {
            switch (pathInfo) {
                case "/zerrenda":
                    // Gaurko data lortu
                    LocalDate gaur = LocalDate.now();
                    // Gaurko erreserbak lortu
                    Map<Integer, List<ErreserbaInfoDTO>> erreserbenMapa =
                            ctx.getErreserbaService().bilatuErreserbakKabinaka(gaur, gaur);
                    // Erreserbak request atributu bezala gehitu
                    request.setAttribute("erreserbenMapa", erreserbenMapa);

                    // Bista: erreserbak/zerrenda.jsp
                    request.getRequestDispatcher("/WEB-INF/views/erreserbak/zerrenda.jsp").forward(
                            request, response);
                    break;
                case "/":
                case null: 
                    response.sendRedirect(request.getContextPath() + "/erreserbak/zerrenda");  
                    break;
                default:
                    // Beste kasu batzuetan, 404 orria erakutsi
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Bide ezezaguna.");
                    break;
            }
        } catch (Exception e) {
            request.setAttribute("errorea", "Errorea aplikazioan: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/errorea.jsp").forward(request, response);
        }
    }
}

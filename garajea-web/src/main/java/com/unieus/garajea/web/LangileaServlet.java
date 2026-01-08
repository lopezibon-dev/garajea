package com.unieus.garajea.web;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import com.unieus.garajea.core.services.context.ServiceContext;
import com.unieus.garajea.core.services.context.ServiceContextFactory;
import com.unieus.garajea.core.presentation.agenda.AgendaBlokeaDTO;
import com.unieus.garajea.core.presentation.agenda.ErreserbaAgendaBuilder;
import com.unieus.garajea.model.dto.ErreserbaInfoDTO;
import com.unieus.garajea.model.entities.Langilea;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/langilea/*") // /langilea/login, /langilea/erreserbak/*, etab.
public class LangileaServlet extends HttpServlet {

    // -----------------------------------------------------------------
    // DOGET (Bistak erakusteko)
    // -----------------------------------------------------------------
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo(); // Adib: /login , /profila, etab.

        ServiceContextFactory scf =
        (ServiceContextFactory) getServletContext()
            .getAttribute("serviceContextFactory");

        try  (ServiceContext ctx = scf.open()) {
            switch (pathInfo) {
                case "/login":
                    request.getRequestDispatcher("/WEB-INF/views/langilea/login.jsp").forward(request,
                    response);
                    break;
                case "/profila":
                    if (request.getSession().getAttribute("current_user") == null) {
                        response.sendRedirect(request.getContextPath() + "/langilea/login");
                        return;
                    }
                    handleErakutsiLangileProfila(ctx, request, response);
                    break;
                case "/logout":
                    // Saioa amaitu eta langilearen login-era berbideratu
                    request.getSession().invalidate();
                    response.sendRedirect(request.getContextPath() + "/langilea/login");
                    break;
                case "/":
                case null: 
                    response.sendRedirect(request.getContextPath() + "/langilea/profila"); 
                    break;
                default:
                    // Beste kasuetan, 404 orria erakutsi
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Eragiketa ezezaguna.");
            }
        } catch (Exception e) {
            // Errore kudeaketa
            request.setAttribute("errorea", "Errorea aplikazioan: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/errorea.jsp").forward(request, response);
        }
    }

    // -----------------------------------------------------------------
    // DOPOST (Datuak kudeatzeko)
    // -----------------------------------------------------------------
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        ServiceContextFactory scf =
        (ServiceContextFactory) getServletContext()
            .getAttribute("serviceContextFactory");

        try  (ServiceContext ctx = scf.open()) {
            switch (pathInfo) {
                case "/login":
                    handleSaioaHasi(ctx, request, response);
                    break;
                case "/":
                case null: 
                    response.sendRedirect(request.getContextPath() + "/langilea/profila");  
                    break; 
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Eragiketa ezezaguna.");
            }
        } catch (Exception e) {
            // DB edo bestelako errorea
            request.setAttribute("errorea", "Errorea aplikazioan: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/errorea.jsp").forward(request, response);
        }    
    }

    private void handleErakutsiLangileProfila(
        ServiceContext ctx,
        HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Langilea langilea = (Langilea) session.getAttribute("current_user");

        // Segurtasun egiaztapena
        if (langilea == null) {
            response.sendRedirect(request.getContextPath() + "/langilea/login");
            return;
        }

        try {
            Langilea langileaEguneratua = ctx.getLangileaService()
                .findById(langilea.getLangileaId());

            LocalDate tarteHasiera = LocalDate.now();
            LocalDate tarteAmaiera = tarteHasiera.plusDays(7);
            // langileari lotutako erreserbak bilatu
            // "egoera" parametroa null izan daiteke guztiak bilatzeko, edo "zain", etab.
            // String egoeraFiltratu = request.getParameter("egoera");
            String egoeraFiltratu = null;

            List<ErreserbaInfoDTO> langilearenErreserbak = ctx.getErreserbaService()
                .bilatuLangilearenErreserbak(langileaEguneratua.getLangileaId(), egoeraFiltratu, tarteHasiera, tarteAmaiera);
            
            List<AgendaBlokeaDTO> langilearenAgenda = ctx.getErreserbaAgendaBuilder()
                .sortuAgenda(langilearenErreserbak, tarteHasiera, tarteAmaiera);

            LocalTime lanaldiHasiera =
                ctx.getKonfigurazioaService().getLanaldiHasiera();

            // Datuak eskaeran (request) gorde
            request.setAttribute("langilea", langileaEguneratua);
            request.setAttribute("langilearenErreserbak", langilearenErreserbak);
            request.setAttribute("langilearenAgenda", langilearenAgenda);

            request.setAttribute("lanaldiHasiera", lanaldiHasiera);

            // Bistara bidali
            request.getRequestDispatcher("/WEB-INF/views/langilea/profila.jsp").forward(request,
                    response);

        } catch (RuntimeException e) {
            // Errore kudeaketa
            e.printStackTrace(); // Garapenerako
            request.setAttribute("errorea",
                    "Errorea Langilearen profila kargatzean: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    private void handleSaioaHasi(ServiceContext ctx, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String emaila = request.getParameter("emaila");
        String pasahitza = request.getParameter("pasahitza");

        try {
            // Logika exekutatu: saioa hasi
            Langilea langilea = ctx.getLangileaService().saioaHasi(emaila, pasahitza);

            if (langilea != null) {
                // Saioa ondo hasi da
                HttpSession session = request.getSession(true);
                session.setAttribute("current_user", langilea);
                session.setAttribute("current_user_type", "langilea");
                response.sendRedirect(request.getContextPath() + "/langilea/profila");
            } else { // langilea == null
                // Errorea: kredentzial okerrak
                request.setAttribute("errorea", "Emaila edo pasahitza okerrak.");
                request.getRequestDispatcher("/WEB-INF/views/langilea/login.jsp").forward(request,
                        response);
            }

        } catch (RuntimeException e) {
            // Errore kudeaketa
            e.printStackTrace(); // Garapenerako
            request.setAttribute("errorea", "Errorea saioa hastean: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

}

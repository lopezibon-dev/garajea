package com.unieus.garajea.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.unieus.garajea.core.exception.ZerbitzuSalbuespena;
import com.unieus.garajea.core.services.context.ServiceContext;
import com.unieus.garajea.core.services.context.ServiceContextFactory;
import com.unieus.garajea.web.exception.InputBalidazioSalbuespena;
import com.unieus.garajea.model.entities.Bezeroa;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/bezeroa/*") // /bezeroa/erregistratu, /bezeroa/login, etab.
public class BezeroaServlet extends HttpServlet {

    // -----------------------------------------------------------------
    // DOGET (Bistak erakusteko)
    // -----------------------------------------------------------------
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo(); // Adib: /erregistratu edo /login

        if (pathInfo == null || pathInfo.equals("/")) {
            // Profila kudeatzeko ikuspegira birbideratu.
            response.sendRedirect(request.getContextPath() + "/bezeroa/profila");
        } else if (pathInfo.equalsIgnoreCase("/erregistratu")) {
            // Bista: erregistratu.jsp
            request.getRequestDispatcher("/WEB-INF/views/bezeroa/erregistratu.jsp").forward(request,
                    response);
        } else if (pathInfo.equalsIgnoreCase("/login")) {
            // Bista: login.jsp
            request.getRequestDispatcher("/WEB-INF/views/bezeroa/login.jsp").forward(request,
                    response);
        } else if (pathInfo.equalsIgnoreCase("/profila")) {
            // Egiaztatu saioa hasita dagoen
            if (request.getSession().getAttribute("current_user") == null) {
                response.sendRedirect(request.getContextPath() + "/bezeroa/login");
                return;
            }
            // Bista: profila.jsp, non bi formulario dauden
            request.getRequestDispatcher("/WEB-INF/views/bezeroa/profila.jsp").forward(request,
                    response);
        } else if (pathInfo.equalsIgnoreCase("/logout")) {
            // Saioa amaitu eta login-era berbideratu
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/bezeroa/login");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Bide ezezaguna.");
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

        if (scf == null) {
            throw new IllegalStateException(
                "ServiceContextFactory ez da hasieratu. " +
                "ServletContextListener berrikusi."
            );
        } 

        try {
            switch (pathInfo) {
                case "/erregistratu":
                    handleErregistratu(scf, request, response);
                    break;
                case "/login":
                    handleSaioaHasi(scf, request, response);
                    break;
                case "/datuakEguneratu":
                    handleDatuakEguneratu(scf, request, response);
                    break;
                case "/pasahitzaEguneratu":
                    handlePasahitzaEguneratu(scf, request, response);
                    break;  
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Eragiketa ezezaguna.");
            }
        // Negozio logikako salbuespenak kudeatu    
        } catch (ZerbitzuSalbuespena e) {

            request.setAttribute("erroreak", e.getErroreak());

            String Bista = erabakiErroreBista(pathInfo);
            request.getRequestDispatcher(Bista).forward(request, response);
        // Sarreren balidazioaren salbuespenak kudeatu
        } catch (InputBalidazioSalbuespena e) {
            request.setAttribute("erroreak", "Errorea aplikazioan: " + e.getErroreak());
            
            String Bista = erabakiErroreBista(pathInfo);
            request.getRequestDispatcher(Bista).forward(request, response);
        }
    }

    private void handleErregistratu(
        ServiceContextFactory scf, 
        HttpServletRequest request, 
        HttpServletResponse response)
            throws ServletException, IOException {

        // Sarrerak balidatu
        List<String> erroreak = new ArrayList<>();

        String izena = request.getParameter("izena");
        String emaila = request.getParameter("emaila");
        String pasahitza = request.getParameter("pasahitza");

        if (izena == null || izena.trim().isEmpty()) {
            erroreak.add("Izena derrigorrezkoa da");
        }

        if (emaila == null || !emaila.matches(".+@.+\\..+")) {
            erroreak.add("Email formatua ez da zuzena");
        }

        if (pasahitza == null) {
            erroreak.add("Pasahitza hutsa da.");
        }

        if (!erroreak.isEmpty()) {
            throw new InputBalidazioSalbuespena(erroreak);
        }

        // ServiceContext hasi eta erregistroa burutu
        try (ServiceContext ctx = scf.open()){
            Bezeroa bezeroa = new Bezeroa();
            bezeroa.setIzena(izena);
            bezeroa.setAbizenak(request.getParameter("abizenak"));
            bezeroa.setEmaila(emaila);
            bezeroa.setPasahitza(pasahitza); // Pasahitza kodetu barik
            bezeroa.setTelefonoa(request.getParameter("telefonoa"));
            
            Bezeroa bezeroaGordeta = ctx.getBezeroaService().erregistratu(bezeroa);

            HttpSession session = request.getSession(true);
            session.setAttribute("current_user", bezeroaGordeta);
            session.setAttribute("current_user_type", "bezeroa");

            response.sendRedirect(request.getContextPath() + "/bezeroa/profila");
        }
    }

    private void handleSaioaHasi(
        ServiceContextFactory scf, 
        HttpServletRequest request, 
        HttpServletResponse response)
            throws ServletException, IOException {

        String emaila = request.getParameter("emaila");
        String pasahitza = request.getParameter("pasahitza"); // Pasahitza kodetu barik

        List<String> erroreak = new ArrayList<>();

        if (emaila == null || emaila.trim().isEmpty()) {
            erroreak.add("Emaila derrigorrezkoa da");
        }

        if (pasahitza == null || pasahitza.trim().isEmpty()) {
            erroreak.add("Pasahitza derrigorrezkoa da");
        }

        if (!erroreak.isEmpty()) {
            throw new InputBalidazioSalbuespena(erroreak);
        }

        try (ServiceContext ctx = scf.open()) {
            Bezeroa bezeroa = ctx.getBezeroaService().saioaHasi(emaila, pasahitza);

            HttpSession session = request.getSession(true);
            session.setAttribute("current_user", bezeroa);
            session.setAttribute("current_user_type", "bezeroa");

            response.sendRedirect(request.getContextPath() + "/bezeroa/profila");
        } catch (ZerbitzuSalbuespena e) {
            request.setAttribute("erroreak", e.getErroreak());
            request.getRequestDispatcher("/WEB-INF/views/bezeroa/login.jsp")
                .forward(request, response);
        }
    }

    /**
     * Bezeroaren datu pertsonalak eguneratzeko eskaera kudeatzen du.
     */
    private void handleDatuakEguneratu(
        ServiceContextFactory scf, 
        HttpServletRequest request, 
        HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Bezeroa bezeroaSesioan = (Bezeroa) session.getAttribute("current_user");

        if (bezeroaSesioan == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Saioa ez dago hasita.");
            return;
        }

        // Sarrerak balidatu
        List<String> erroreak = new ArrayList<>();

        String izena = request.getParameter("izena");
        String emaila = request.getParameter("emaila");

        if (izena == null || izena.trim().isEmpty()) {
            erroreak.add("Izena derrigorrezkoa da");
        }

        if (emaila == null || !emaila.matches(".+@.+\\..+")) {
            erroreak.add("Email formatua ez da zuzena");
        }
        if (!erroreak.isEmpty()) {
            throw new InputBalidazioSalbuespena(erroreak);
        }



        try (ServiceContext ctx = scf.open()){
            // bezeroaEguneratu objektua sortu datuekin
            Bezeroa bezeroaEguneratu = new Bezeroa();
            bezeroaEguneratu.setBezeroaId(bezeroaSesioan.getBezeroaId()); // GARRANTZITSUA: IDa mantendu
            bezeroaEguneratu.setIzena(izena);
            bezeroaEguneratu.setAbizenak(request.getParameter("abizenak"));
            bezeroaEguneratu.setEmaila(emaila);
            bezeroaEguneratu.setTelefonoa(request.getParameter("telefonoa"));

            // Datuak eguneratu
            ctx.getBezeroaService().datuakEguneratu(bezeroaEguneratu);

            // Eguneratu saioa datu basean gordetako datuak eskuratuz
            Bezeroa bezeroaBerria = ctx.getBezeroaService().findById(bezeroaEguneratu.getBezeroaId());
            session.setAttribute("current_user", bezeroaBerria);

            request.setAttribute("arrakasta", "Datuak arrakastaz eguneratu dira.");
            request.getRequestDispatcher("/WEB-INF/views/bezeroa/profila.jsp").forward(request, response);
        } catch (ZerbitzuSalbuespena e) {
            request.setAttribute("errorea", "Errorea datuak eguneratzean: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/bezeroa/profila.jsp").forward(request, response);
        }
    }

    /**
     * Bezeroaren pasahitza aldatzeko eskaera kudeatzen du.
     */
    private void handlePasahitzaEguneratu(
        ServiceContextFactory scf, 
        HttpServletRequest request, 
        HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Bezeroa bezeroaSesioan = (Bezeroa) session.getAttribute("current_user");

        if (bezeroaSesioan == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Saioa ez dago hasita.");
            return;
        }

        // Sarrerak balidatu
        String pasahitzaBerria = request.getParameter("pasahitzaBerria");

        if (pasahitzaBerria == null || pasahitzaBerria.trim().isEmpty()) {
            throw new InputBalidazioSalbuespena("Pasahitza berria derrigorrezkoa da.");
        }
        // Negozio logika: Pasahitza eguneratu
        try (ServiceContext ctx = scf.open()){
            
            ctx.getBezeroaService().pasahitzaEguneratu(
                bezeroaSesioan.getBezeroaId(),
                pasahitzaBerria);

            request.setAttribute("arrakasta", "Pasahitza arrakastaz aldatu da.");

            // Saioa baliogabetu beharko litzateke segurtasunagatik 
            // (bezeroa berriro saioa hastera  behartuz)
            // Baina erraztasunagatik, profila kargatuko dugu.
            request.getRequestDispatcher("/WEB-INF/views/bezeroa/profila.jsp").forward(request, response);

        } 
    }

    private String erabakiErroreBista(String pathInfo) {
        switch (pathInfo) {
            case "/login":
                return "/WEB-INF/views/bezeroa/login.jsp";
            case "/erregistratu":
                return "/WEB-INF/views/bezeroa/erregistratu.jsp";
            case "/pasahitzaEguneratu":
                return "/WEB-INF/views/bezeroa/profila.jsp";
            default:
                return "/WEB-INF/views/errorea.jsp";
        }
    }
}

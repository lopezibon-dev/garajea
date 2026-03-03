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
            // Profila kudeatzeko ikuspegira berbideratu.
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
            request.getRequestDispatcher("/WEB-INF/views/bezeroa/profila.jsp").forward(request, response);
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

        ServiceContextFactory ZerbitzuEsparruFaktoria =
        (ServiceContextFactory) getServletContext()
            .getAttribute("serviceContextFactory");

        if (ZerbitzuEsparruFaktoria == null) {
            throw new IllegalStateException(
                "ServiceContextFactory ez da hasieratu. " +
                "ServletContextListener berrikusi."
            );
        } 

        try {
            switch (pathInfo) {
                case "/erregistratu":
                    handleErregistratu(ZerbitzuEsparruFaktoria, request, response);
                    break;
                case "/login":
                    handleSaioaHasi(ZerbitzuEsparruFaktoria, request, response);
                    break;
                case "/datuakEguneratu":
                    handleDatuakEguneratu(ZerbitzuEsparruFaktoria, request, response);
                    break;
                case "/pasahitzaEguneratu":
                    handlePasahitzaEguneratu(ZerbitzuEsparruFaktoria, request, response);
                    break;  
                default:
                    // Beste kasuetan, 404 orria erakutsi
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Eragiketa ezezaguna.");
            }
            // Sarreren balidazioaren salbuespenak kudeatu
        } catch (InputBalidazioSalbuespena e) {
            request.setAttribute("erroreak", e.getErroreak());
            String Bista = erabakiErroreBista(pathInfo);
            request.getRequestDispatcher(Bista).forward(request, response);
            // Negozio logikako salbuespenak kudeatu    
        } catch (ZerbitzuSalbuespena e) {
            request.setAttribute("erroreak", e.getErroreak());
            String Bista = erabakiErroreBista(pathInfo);
            request.getRequestDispatcher(Bista).forward(request, response);
        } catch (Exception e) {
            throw e;
        }
    }

    private void handleErregistratu(
        ServiceContextFactory ZerbitzuEsparruFaktoria, 
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

        if (pasahitza == null || pasahitza.trim().isEmpty()) {
            erroreak.add("Pasahitza hutsik dago.");
        }

        if (!erroreak.isEmpty()) {
            throw new InputBalidazioSalbuespena(erroreak);
        }

        // ServiceContext hasi eta erregistroa burutu
        try (ServiceContext ZerbitzuEsparrua = ZerbitzuEsparruFaktoria.open()){
            Bezeroa bezeroa = new Bezeroa();
            bezeroa.setIzena(izena);
            bezeroa.setAbizenak(request.getParameter("abizenak"));
            bezeroa.setEmaila(emaila);
            bezeroa.setPasahitza(pasahitza); // Pasahitza kodetu barik
            bezeroa.setTelefonoa(request.getParameter("telefonoa"));
            
            Bezeroa bezeroaGordeta = ZerbitzuEsparrua.getBezeroaService().erregistratu(bezeroa);

            HttpSession session = request.getSession(true);
            session.setAttribute("current_user", bezeroaGordeta);
            session.setAttribute("current_user_type", "bezeroa");

            // Konpondu!!!
            request.setAttribute("arrakasta", "Bezeroaren erregistroa behar bezala burutu da.");
            response.sendRedirect(request.getContextPath() + "/bezeroa/profila");
        }
    }

    private void handleSaioaHasi(
        ServiceContextFactory ZerbitzuEsparruFaktoria, 
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

        try (ServiceContext ZerbitzuEsparrua = ZerbitzuEsparruFaktoria.open()) {
            Bezeroa bezeroa = ZerbitzuEsparrua.getBezeroaService().saioaHasi(emaila, pasahitza);

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
        ServiceContextFactory ZerbitzuEsparruFaktoria, 
        HttpServletRequest request, 
        HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesioa = request.getSession(false);
        if (sesioa == null ||
            sesioa.getAttribute("current_user") == null || 
            !sesioa.getAttribute("current_user_type").equals("bezeroa")) {
                response.sendRedirect(request.getContextPath() + "/bezeroa/login");
                return;
            }
        Bezeroa bezeroaSesioan = (Bezeroa) sesioa.getAttribute("current_user");  
  
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

        try (ServiceContext ZerbitzuEsparrua = ZerbitzuEsparruFaktoria.open()){
            // bezeroaEguneratu objektua sortu datuekin
            Bezeroa bezeroa = new Bezeroa();
            bezeroa.setBezeroaId(bezeroaSesioan.getBezeroaId()); // GARRANTZITSUA: IDa mantendu
            bezeroa.setIzena(izena);
            bezeroa.setAbizenak(request.getParameter("abizenak"));
            bezeroa.setEmaila(emaila);
            bezeroa.setTelefonoa(request.getParameter("telefonoa"));

            // Datuak eguneratu
            ZerbitzuEsparrua.getBezeroaService().datuakEguneratu(bezeroa);

            // Eguneratu saioa datu basean gordetako datuak eskuratuz
            Bezeroa bezeroBerria = ZerbitzuEsparrua.getBezeroaService().findById(bezeroa.getBezeroaId());
            sesioa.setAttribute("current_user", bezeroBerria);

            request.getSession().setAttribute("flashMezua", "Datuak arrakastaz eguneratu dira.");
            response.sendRedirect(
                request.getContextPath() + "/bezeroa/profila");
        } catch (ZerbitzuSalbuespena e) {
            request.setAttribute("errorea", "Errorea datuak eguneratzean.");
            request.getRequestDispatcher("/WEB-INF/views/bezeroa/profila.jsp").forward(request, response);
        }
    }

    /**
     * Bezeroaren pasahitza aldatzeko eskaera kudeatzen du.
     */
    private void handlePasahitzaEguneratu(
        ServiceContextFactory ZerbitzuEsparruFaktoria, 
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
        try (ServiceContext ZerbitzuEsparrua = ZerbitzuEsparruFaktoria.open()){
            
            ZerbitzuEsparrua.getBezeroaService().pasahitzaEguneratu(
                bezeroaSesioan.getBezeroaId(),
                pasahitzaBerria);

            request.setAttribute("arrakasta", "Pasahitza arrakastaz aldatu da.");

            // Saioa baliogabetu beharko litzateke segurtasunagatik 
            // (bezeroa berriro saioa hastera  behartuz)
            // Baina erraztasunagatik, profila kargatuko dugu.
            request.getRequestDispatcher("/WEB-INF/views/bezeroa/profila.jsp").forward(request, response);

        } catch (ZerbitzuSalbuespena e) {
            request.setAttribute("errorea", "Errorea pasahitza eguneratzean.");
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

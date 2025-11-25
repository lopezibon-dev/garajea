package com.unieus.garajea.web;

import com.unieus.garajea.core.service.BezeroaService;
import com.unieus.garajea.model.dao.DAOFactory;
import com.unieus.garajea.model.dao.BezeroaDAO;
import com.unieus.garajea.model.entities.Bezeroa;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/bezeroa/*") // /bezeroa/erregistratu, /bezeroa/login, etab.
public class BezeroaServlet extends HttpServlet {

    /**
     * Metodo laguntzailea BezeroaService lortzeko, beti DAOFactory erabiliz.
     */
    private BezeroaService getBezeroaService(DAOFactory factory) {
        // DAOFactory-k DAO inplementazio konkretua (BezeroaDAOImpl) ematen du
        BezeroaDAO bezeroaDAO = factory.getBezeroaDAO();
        // Zerbitzuak DAO interfazekin bakarrik lan egiten du
        return new BezeroaService(bezeroaDAO);
    }
    
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
            request.getRequestDispatcher("/WEB-INF/views/bezeroa/erregistratu.jsp").forward(request, response);
        } else if (pathInfo.equalsIgnoreCase("/login")) {
            // Bista: login.jsp
            request.getRequestDispatcher("/WEB-INF/views/bezeroa/login.jsp").forward(request, response);
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
        
        if (pathInfo != null) {
            if (pathInfo.equalsIgnoreCase("/erregistratu")) {
                handleErregistratu(request, response);
            } else if (pathInfo.equalsIgnoreCase("/login")) {
                handleSaioaHasi(request, response);
            } else if (pathInfo.equalsIgnoreCase("/datuakEguneratu")) {
                handleDatuakEguneratu(request, response);
            } else if (pathInfo.equalsIgnoreCase("/pasahitzaEguneratu")) {
                handlePasahitzaEguneratu(request, response);
            } else {
                 response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Eragiketa ezezaguna.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Eragiketa ezezaguna.");
        }
    }
    

   
    private void handleErregistratu(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Datuak hartu eta Bezeroa objektua sortu        
        Bezeroa bezeroa = new Bezeroa();
        bezeroa.setIzena(request.getParameter("izena"));
        bezeroa.setAbizenak(request.getParameter("abizenak"));
        bezeroa.setEmaila(request.getParameter("emaila"));
        bezeroa.setPasahitza(request.getParameter("pasahitza")); // Pasahitza kodetu barik
        bezeroa.setTelefonoa(request.getParameter("telefonoa"));

        // 2. Zerbitzua lortu eta logika exekutatu (DAO Factory erabiliz)
        DAOFactory factory = DAOFactory.getInstance();
        try {
            BezeroaService service = getBezeroaService(factory);
            Bezeroa bezeroaGordeta = service.erregistratu(bezeroa);
            
            if (bezeroaGordeta != null) {
                // Erregistro arrakastatsua: hasi saioa automatikoki
                HttpSession session = request.getSession(true);
                session.setAttribute("current_user", bezeroaGordeta);
                session.setAttribute("current_user_type", "bezeroa");
                response.sendRedirect(request.getContextPath() + "/bezeroa/profila"); 
            } else {
                // Errorea: Emaila jada existitzen da
                request.setAttribute("errorea", "Posta elektronikoa jada erregistratuta dago.");
                request.getRequestDispatcher("/WEB-INF/views/bezeroa/erregistratu.jsp").forward(request, response);
            }
        } catch (RuntimeException e) {
            // DB errorea
            request.setAttribute("errorea", "Errorea datu basean: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/bezeroa/erregistratu.jsp").forward(request, response);
        } finally {
            factory.close(); // GARRANTZITSUA: Konexioa itxi!
        }
    }
    
    private void handleSaioaHasi(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String emaila = request.getParameter("emaila");
        String pasahitza = request.getParameter("pasahitza"); // Pasahitza kodetu barik

        DAOFactory factory = DAOFactory.getInstance();
        try {
            BezeroaService service = getBezeroaService(factory);
            
            Bezeroa bezeroa = service.saioaHasi(emaila, pasahitza);
            
            if (bezeroa != null) {
                // Login arrakastatsua
                HttpSession session = request.getSession(true);
                session.setAttribute("current_user", bezeroa);
                session.setAttribute("current_user_type", "bezeroa");
                response.sendRedirect(request.getContextPath() + "/bezeroa/profila"); 
            } else {
                // Errorea: kredentzial okerrak
                request.setAttribute("errorea", "Emaila edo pasahitza okerrak.");
                request.getRequestDispatcher("/WEB-INF/views/bezeroa/login.jsp").forward(request, response);
            }
        } catch (RuntimeException e) {
            // DB errorea.
            request.setAttribute("errorea", "Errorea datu basean: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/bezeroa/login.jsp").forward(request, response);
        } finally {
            factory.close(); // GARRANTZITSUA: Konexioa itxi!
        }
    }
    
    /**
     * Bezeroaren datu pertsonalak eguneratzeko eskaera kudeatzen du.
     */
    private void handleDatuakEguneratu(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Bezeroa bezeroaSesioan = (Bezeroa) session.getAttribute("current_user");

        if (bezeroaSesioan == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Saioa ez dago hasita.");
            return;
        }

        // 1. Datuak hartu eta Bezeroa objektua sortu
        Bezeroa bezeroaEguneratu = new Bezeroa();
        bezeroaEguneratu.setBezeroaId(bezeroaSesioan.getBezeroaId()); // GARRANTZITSUA: IDa mantendu
        bezeroaEguneratu.setIzena(request.getParameter("izena"));
        bezeroaEguneratu.setAbizenak(request.getParameter("abizenak"));
        bezeroaEguneratu.setEmaila(request.getParameter("emaila"));
        bezeroaEguneratu.setTelefonoa(request.getParameter("telefonoa"));

        DAOFactory factory = DAOFactory.getInstance();
        try {
            BezeroaService service = getBezeroaService(factory);
            
            // 2. Datuak eguneratu
            service.datuakEguneratu(bezeroaEguneratu);
            
            // 3. Eguneratu saioa datu basean gordetako datuak eskuratuz
            Bezeroa bezeroaBerria = service.findById(bezeroaEguneratu.getBezeroaId());
            session.setAttribute("current_user", bezeroaBerria);
            
            request.setAttribute("arrakasta", "Datuak arrakastaz eguneratu dira.");
            request.getRequestDispatcher("/WEB-INF/views/bezeroa/profila.jsp").forward(request, response);
        } catch (RuntimeException e) {
            request.setAttribute("errorea", "Errorea datuak eguneratzean: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/bezeroa/profila.jsp").forward(request, response);
        } finally {
            factory.close(); // GARRANTZITSUA: Konexioa itxi!
        }
    }
    
    /**
     * Bezeroaren pasahitza aldatzeko eskaera kudeatzen du.
     */
    private void handlePasahitzaEguneratu(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Bezeroa bezeroaSesioan = (Bezeroa) session.getAttribute("current_user");
        
        if (bezeroaSesioan == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Saioa ez dago hasita.");
            return;
        }
        
        // 1. Datuak hartu
        String pasahitzaBerria = request.getParameter("pasahitzaBerria");
        
        DAOFactory factory = DAOFactory.getInstance();
        try {
            BezeroaService service = getBezeroaService(factory);
            
            // 2. Pasahitza eguneratu
            boolean arrakasta = service.pasahitzaEguneratu(bezeroaSesioan.getBezeroaId(), pasahitzaBerria);
            
            if (arrakasta) {
                request.setAttribute("arrakasta", "Pasahitza arrakastaz aldatu da.");
            } else {
                request.setAttribute("errorea", "Pasahitza aldatzean errorea gertatu da.");
            }
            
            // Saioa baliogabetu beharko litzateke segurtasunagatik (bezeroa berriro saioa hastera behartuz)
            // Baina erraztasunagatik, profila kargatuko dugu.
            request.getRequestDispatcher("/WEB-INF/views/bezeroa/profila.jsp").forward(request, response);
            
        } catch (RuntimeException e) {
            request.setAttribute("errorea", "Errorea pasahitza eguneratzean: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/bezeroa/profila.jsp").forward(request, response);
        } finally {
            factory.close(); // GARRANTZITSUA: Konexioa itxi!
        } 
    }
}
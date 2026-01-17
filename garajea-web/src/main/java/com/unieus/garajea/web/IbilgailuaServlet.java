package com.unieus.garajea.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.unieus.garajea.web.exception.InputBalidazioSalbuespena;
import com.unieus.garajea.web.balidazioa.BalidazioTresnak;
import com.unieus.garajea.web.dto.IbilgailuaFormDTO;
import com.unieus.garajea.core.exception.ZerbitzuSalbuespena;
import com.unieus.garajea.core.services.context.ServiceContext;
import com.unieus.garajea.core.services.context.ServiceContextFactory;
import com.unieus.garajea.model.entities.Bezeroa;
import com.unieus.garajea.model.entities.Ibilgailua;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ibilgailua/*") // /ibilgailua/..., etab.
public class IbilgailuaServlet extends HttpServlet {

    private ServiceContextFactory zerbitzuEsparruFaktoria;

    @Override
    public void init() {
        ServiceContextFactory zerbitzuEsparruFaktoria =
        (ServiceContextFactory) getServletContext()
            .getAttribute("serviceContextFactory");

        if (zerbitzuEsparruFaktoria == null) {
            throw new IllegalStateException(
                "ServiceContextFactory ez da hasieratu. " +
                "ServletContextListener berrikusi."
            );
        }
        this.zerbitzuEsparruFaktoria = zerbitzuEsparruFaktoria;
    }

    // -----------------------------------------------------------------
    // DOGET (Bistak erakusteko)
    // -----------------------------------------------------------------
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { 

        HttpSession sesioa = request.getSession(false);
        if (sesioa == null ||
            sesioa.getAttribute("current_user") == null || 
            !sesioa.getAttribute("current_user_type").equals("bezeroa")) {
                response.sendRedirect(request.getContextPath() + "/bezeroa/login");
                return;
            }
        Bezeroa bezeroaSesioan = (Bezeroa) sesioa.getAttribute("current_user"); 

        String pathInfo = request.getPathInfo();
        try {
            switch (pathInfo) {
                case null:
                case "/":
                    handleIbilgailuenZerrenda(bezeroaSesioan, request, response);
                    break; 
                case "/berria":
                    handleFormularioBerria(bezeroaSesioan, request, response);
                    break;
                case "/editatu":
                    handleFormularioaEditatu(bezeroaSesioan, request, response);
                    break;  
                default:
                    // Beste kasu batzuetan, 404 orria erakutsi
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Eragiketa ezezaguna.");
                    break;
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
    private void handleIbilgailuenZerrenda(
        Bezeroa bezeroaSesioan,
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {

        List<String> erroreak = new ArrayList<>();

        int bezeroaId = bezeroaSesioan.getBezeroaId();

        if (!erroreak.isEmpty()) {
            throw new InputBalidazioSalbuespena(erroreak);
        }
        // ServiceContext hasi
        try (ServiceContext ZerbitzuEsparrua = 
            zerbitzuEsparruFaktoria.open()){

            List<Ibilgailua> ibilgailuak = ZerbitzuEsparrua.getIbilgailuaService().bilatuBezeroarenIbilgailuak(bezeroaId);

            request.setAttribute("ibilgailuak", ibilgailuak);
            request.setAttribute("bezeroaId", bezeroaId);

            request.getRequestDispatcher("/WEB-INF/views/ibilgailua/zerrenda.jsp")
                .forward(request, response);
        }
    }

    private void handleFormularioBerria(
            Bezeroa bezeroaSesioan,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int bezeroaId = bezeroaSesioan.getBezeroaId();

        IbilgailuaFormDTO ibilgailuaForm = new IbilgailuaFormDTO();
        ibilgailuaForm.setBezeroaId(bezeroaId);

        request.setAttribute("ibilgailuaForm", ibilgailuaForm);
        request.getRequestDispatcher("/WEB-INF/views/ibilgailua/formularioa.jsp")
            .forward(request, response);
    }

    private void handleFormularioaEditatu(
            Bezeroa bezeroaSesioan,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        List<String> erroreak = new ArrayList<>();

        Integer ibilgailuaId = BalidazioTresnak.getRequiredInt(
                request, "id", "Ibilgailua", erroreak);

        if (!erroreak.isEmpty()) {
            throw new InputBalidazioSalbuespena(erroreak);
        }

         try (ServiceContext ZerbitzuEsparrua = 
            zerbitzuEsparruFaktoria.open()){
            Ibilgailua ibilgailua =
                    ZerbitzuEsparrua.getIbilgailuaService().bilatuIbilgailua(ibilgailuaId);

            IbilgailuaFormDTO ibilgailuaForm = IbilgailuaFormDTO.fromEntity(ibilgailua);

            request.setAttribute("ibilgailuaForm", ibilgailuaForm);
            request.getRequestDispatcher("/WEB-INF/views/ibilgailua/formularioa.jsp")
                .forward(request, response);
        }
    }

    // -----------------------------------------------------------------
    // DOPOST
    // -----------------------------------------------------------------

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession sesioa = request.getSession(false);
        if (sesioa == null ||
            sesioa.getAttribute("current_user") == null || 
            !sesioa.getAttribute("current_user_type").equals("bezeroa")) {
                response.sendRedirect(request.getContextPath() + "/bezeroa/login");
                return;
            }
        Bezeroa bezeroaSesioan = (Bezeroa) sesioa.getAttribute("current_user"); 

        IbilgailuaFormDTO ibilgailuaForm = null;
        String pathInfo = request.getPathInfo();
        try {
            List<String> erroreak = new ArrayList<>();

            // Behin bakarrik irakurri eta balidatu formulariotik jasotakoa
            ibilgailuaForm =
                IbilgailuaFormDTO.fromRequest(request, erroreak);

            ibilgailuaForm.setBezeroaId(bezeroaSesioan.getBezeroaId());

            if (!erroreak.isEmpty()) {
                throw new InputBalidazioSalbuespena(erroreak);
            }

            switch (pathInfo) {
                case "/sortu":
                    handleSortu(ibilgailuaForm, request, response);
                    break;
                case "/eguneratu":
                    handleEguneratu(ibilgailuaForm, request, response);
                    break; 
                case "/ezabatu":
                    handleEzabatu(ibilgailuaForm, request, response); 
                    break;
                default:
                    // Beste kasu batzuetan, 404 orria erakutsi
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Eragiketa ezezaguna.");
                    break;
            }
            // Sarreren balidazioaren salbuespenak kudeatu
        } catch (InputBalidazioSalbuespena e) {
            request.setAttribute("erroreak", e.getErroreak());
            request.setAttribute("ibilgailuaForm", ibilgailuaForm);

            String Bista = erabakiErroreBista(pathInfo);
            request.getRequestDispatcher(Bista).forward(request, response);

            // Negozio logikako salbuespenak kudeatu    
        } catch (ZerbitzuSalbuespena e) {
            request.setAttribute("erroreak", e.getErroreak());
            request.setAttribute("ibilgailuaForm", ibilgailuaForm);

            String Bista = erabakiErroreBista(pathInfo);
            request.getRequestDispatcher(Bista).forward(request, response);

        } catch (Exception e) {
            throw e;
        }                
    }

    private void handleSortu(
        IbilgailuaFormDTO ibilgailuaForm, 
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {
        
        int bezeroaId = ibilgailuaForm.getBezeroaId();
        
        try (ServiceContext ZerbitzuEsparrua = 
            zerbitzuEsparruFaktoria.open()){

            Ibilgailua ibilgailua = ibilgailuaForm.toEntity();
            ZerbitzuEsparrua.getIbilgailuaService().sortuIbilgailua(bezeroaId, ibilgailua);

            response.sendRedirect(
                request.getContextPath() +
                "/ibilgailua");
        }
    }

    private void handleEguneratu(
        IbilgailuaFormDTO ibilgailuaForm,
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {
        
        List<String> erroreak = new ArrayList<>();

        if (ibilgailuaForm.getIbilgailuaId() == null) {
            erroreak.add("Ibilgailua identifikatzailea falta da");
        }

        if (!erroreak.isEmpty()) {
            throw new InputBalidazioSalbuespena(erroreak);
        }

        int bezeroaId = ibilgailuaForm.getBezeroaId();

        try (ServiceContext ZerbitzuEsparrua = 
            zerbitzuEsparruFaktoria.open()){

            ZerbitzuEsparrua.getIbilgailuaService().eguneratuIbilgailua(bezeroaId, ibilgailuaForm.toEntity());

            response.sendRedirect(
                request.getContextPath() +
                "/ibilgailua");
        }
    }

    private void handleEzabatu(
        IbilgailuaFormDTO ibilgailuaForm,
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {
        
        List<String> erroreak = new ArrayList<>();

        if (ibilgailuaForm.getIbilgailuaId() == null) {
            erroreak.add("Ibilgailua identifikatzailea falta da");
        }

        if (!erroreak.isEmpty()) {
            throw new InputBalidazioSalbuespena(erroreak);
        }

        int bezeroaId = ibilgailuaForm.getBezeroaId();
  
        try (ServiceContext ZerbitzuEsparrua = 
            zerbitzuEsparruFaktoria.open()){

            ZerbitzuEsparrua.getIbilgailuaService().ezabatuIbilgailua(
                bezeroaId, 
                ibilgailuaForm.getIbilgailuaId()
            );

            response.sendRedirect(
                request.getContextPath() +
                "/ibilgailua");
        }
    }    

    private String erabakiErroreBista(String pathInfo) {
        switch (pathInfo) {
            case "/sortu":
            case "/eguneratu": 
                return "/WEB-INF/views/ibilgailua/formularioa.jsp"; 
                
            case "/ezabatu": 
                return "/WEB-INF/views/ibilgailua/zerrenda.jsp";
   
            default:
                return "/WEB-INF/views/errorea.jsp";
        }
    }

}

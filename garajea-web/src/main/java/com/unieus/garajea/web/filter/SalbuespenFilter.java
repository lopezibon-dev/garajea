package com.unieus.garajea.web.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter(urlPatterns = {
    "/bezeroa/*",      // Bezeroaren eremua
    "/langilea/*",     // Langileen eremua
    "/erreserbak/*",   // Erreserben eremua
})
public class SalbuespenFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(SalbuespenFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            handleException((HttpServletRequest) request, response, e);
        }
    }

    private void handleException(HttpServletRequest request, ServletResponse response, Exception e) 
            throws ServletException, IOException {
        
        String requestUri = request.getRequestURI();
        
        // Logging teknikoa: salbuespena eta bidea
        LOG.error("Salbuespena atzeman da URI honetan: {}. Mezua: {}", requestUri, e.getMessage(), e);

        // Bistan mezua erakusteko atributua
        request.setAttribute("errorea", "Barkatu, errore tekniko bat gertatu da. Mesedez, saiatu beranduago.");
        
        // errore-orrirako barne berbideraketa (nabigatzailearen URL aldatu gabe)
        request.getRequestDispatcher("/WEB-INF/views/errorea.jsp").forward(request, response);
    }
    
}

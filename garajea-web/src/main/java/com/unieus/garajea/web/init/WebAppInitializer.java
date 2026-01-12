package com.unieus.garajea.web.init;
import java.util.Properties;
import org.slf4j.LoggerFactory;
import com.unieus.garajea.model.infraestructure.InfraConfig;
import com.unieus.garajea.model.util.PropertiesLoader;
import com.unieus.garajea.core.config.KonfigurazioaService;
import com.unieus.garajea.core.config.impl.KonfigurazioaServiceStaticImpl;
import com.unieus.garajea.core.services.context.ServiceContextFactory;
import com.unieus.garajea.core.services.context.impl.JDBCServiceContextFactory;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class WebAppInitializer implements ServletContextListener {
    private static final Logger LOG =
        LoggerFactory.getLogger(WebAppInitializer.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();

        try {
            // Azpiegiturako konfigurazia kargatu
            Properties infraProperties =
                PropertiesLoader.loadFromClasspath("infrastructure.properties");

            InfraConfig infra = InfraConfig.from(infraProperties);

            // Konfigurazioa service
            KonfigurazioaService konfigurazioaService =
                new KonfigurazioaServiceStaticImpl();

            // ServiceContextFactory
            ServiceContextFactory scf =
                new JDBCServiceContextFactory(
                    infra, 
                    konfigurazioaService);

            ctx.setAttribute("serviceContextFactory", scf);

            LOG.info("WebAppInitializer: aplikazioaren hasieratzea (bootstrap) burutu da.");

        } catch (Exception e) {
            LOG.error("ERRORE KRITIKOA apliakzioaren hasieratzean (bootstrap).");
            throw new RuntimeException(
                "Aplikazioa ezin da hasieratu.", e
            );
        }
    }
}
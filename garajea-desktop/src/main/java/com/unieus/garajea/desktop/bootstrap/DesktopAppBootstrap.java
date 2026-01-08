package com.unieus.garajea.desktop.bootstrap;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unieus.garajea.model.infraestructure.InfraConfig;
import com.unieus.garajea.model.util.PropertiesLoader;
import com.unieus.garajea.core.config.KonfigurazioaService;
import com.unieus.garajea.core.config.impl.KonfigurazioaServiceStaticImpl;
import com.unieus.garajea.core.services.context.ServiceContextFactory;
import com.unieus.garajea.core.services.context.impl.JDBCServiceContextFactory;

public class DesktopAppBootstrap {

    private static final Logger LOG =
        LoggerFactory.getLogger(DesktopAppBootstrap.class);

    private static ServiceContextFactory serviceContextFactory;

    public static void init() {
        try {
            Properties infraProperties =
                PropertiesLoader.loadFromClasspath("infrastructure.properties");

            InfraConfig infra = InfraConfig.from(infraProperties);

            KonfigurazioaService konfigurazioaService =
                new KonfigurazioaServiceStaticImpl();

            serviceContextFactory =
                new JDBCServiceContextFactory(infra, konfigurazioaService);

            LOG.info("Desktop bootstrap ongi hasieratua.");

        } catch (Exception e) {
            LOG.error("Errore kritikoa desktop bootstrap-ean.", e);
            throw new RuntimeException(
                "Desktop aplikazioa ezin izan da hasieratu.", e
            );
        }
    }

    public static ServiceContextFactory getServiceContextFactory() {
        return serviceContextFactory;
    }
}


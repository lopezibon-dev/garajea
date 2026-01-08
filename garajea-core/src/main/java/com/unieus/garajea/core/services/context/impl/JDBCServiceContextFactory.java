package com.unieus.garajea.core.services.context.impl;

import com.unieus.garajea.model.dao.DAOFactory;
import com.unieus.garajea.model.dao.impl.JDBCDAOFactory;
import com.unieus.garajea.core.config.KonfigurazioaService;
import com.unieus.garajea.core.services.context.ServiceContext;
import com.unieus.garajea.core.services.context.ServiceContextFactory;
import com.unieus.garajea.model.infraestructure.InfraConfig;

public class JDBCServiceContextFactory implements ServiceContextFactory {

    private final InfraConfig infra;
    private final KonfigurazioaService konfigurazioaService;

    public JDBCServiceContextFactory(InfraConfig infra, KonfigurazioaService konfigurazioaService) {
        this.infra = infra;
        this.konfigurazioaService = konfigurazioaService;
    }
    @Override
    public ServiceContext open() {
        DAOFactory daoFactory = new JDBCDAOFactory(infra);
        return new JDBCServiceContext(daoFactory, konfigurazioaService);
    }
}
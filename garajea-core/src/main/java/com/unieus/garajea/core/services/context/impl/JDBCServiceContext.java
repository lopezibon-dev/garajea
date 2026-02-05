package com.unieus.garajea.core.services.context.impl;

import com.unieus.garajea.core.config.KonfigurazioaService;
import com.unieus.garajea.core.services.context.ServiceContext;
import com.unieus.garajea.core.util.PythonEsportazioExecutor;
import com.unieus.garajea.core.services.BezeroaService;
import com.unieus.garajea.core.services.DatubaseaService;
import com.unieus.garajea.core.services.LangileaService;
import com.unieus.garajea.core.services.ErreserbaService;
import com.unieus.garajea.core.services.IbilgailuaService;
import com.unieus.garajea.core.presentation.agenda.ErreserbaAgendaBuilder;
import com.unieus.garajea.model.dao.DAOFactory;

public class JDBCServiceContext implements ServiceContext {

    private final DAOFactory daoFactory;
    private final KonfigurazioaService konfigurazioaService;

    public JDBCServiceContext(DAOFactory daoFactory, KonfigurazioaService konfigurazioaService) {
        this.daoFactory = daoFactory;
        this.konfigurazioaService = konfigurazioaService;
    }

    @Override
    public KonfigurazioaService getKonfigurazioaService() {
        return konfigurazioaService;
    }

    @Override
    public BezeroaService getBezeroaService() {
        return new BezeroaService(daoFactory.getBezeroaDAO());
    }

     @Override
    public LangileaService getLangileaService() {
        return new LangileaService(daoFactory.getLangileaDAO());
    }

    @Override
    public ErreserbaService getErreserbaService() {
        return new ErreserbaService(daoFactory.getErreserbaDAO(), konfigurazioaService);
    }

    @Override
    public IbilgailuaService getIbilgailuaService() {
        return new IbilgailuaService(daoFactory.getIbilgailuaDAO(), daoFactory.getBezeroaDAO());
    }

    @Override 
    public DatubaseaService getDatubaseaService() { 
        return new DatubaseaService(daoFactory.getDatubaseaMetaDAO(), new PythonEsportazioExecutor()); 
    }

    @Override
    public ErreserbaAgendaBuilder getErreserbaAgendaBuilder() {
        return new ErreserbaAgendaBuilder(konfigurazioaService);
    }

    @Override
    public void executeInTransaction(
            DAOFactory.TransactionVoidCallback callback) {
        daoFactory.executeInTransaction(callback);
    }

    @Override
    public <T> T executeInTransaction(
            DAOFactory.TransactionCallback<T> callback) {
        return daoFactory.executeInTransaction(callback);
    }

    @Override
    public void close() {
        try {
            daoFactory.close();
        } catch (Exception e) {
            throw new RuntimeException(
                "Errorea ServiceContext ixtean", e
            );
        }
    }
}

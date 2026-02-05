package com.unieus.garajea.core.services.context;

import com.unieus.garajea.core.config.KonfigurazioaService;
import com.unieus.garajea.core.services.BezeroaService;
import com.unieus.garajea.core.services.DatubaseaService;
import com.unieus.garajea.core.services.ErreserbaService;
import com.unieus.garajea.core.services.IbilgailuaService;
import com.unieus.garajea.core.services.LangileaService;
import com.unieus.garajea.model.dao.DAOFactory;
import com.unieus.garajea.core.presentation.agenda.ErreserbaAgendaBuilder;
/**
 * Zerbitzuen testuingurua.
 */
public interface ServiceContext extends AutoCloseable {

    KonfigurazioaService getKonfigurazioaService();
    
    BezeroaService getBezeroaService();

    LangileaService getLangileaService();

    ErreserbaService getErreserbaService();

    IbilgailuaService getIbilgailuaService();

    ErreserbaAgendaBuilder getErreserbaAgendaBuilder();

    DatubaseaService getDatubaseaService();

    void executeInTransaction(DAOFactory.TransactionVoidCallback callback);

    <T> T executeInTransaction(DAOFactory.TransactionCallback<T> callback);

    @Override
    void close(); 
}

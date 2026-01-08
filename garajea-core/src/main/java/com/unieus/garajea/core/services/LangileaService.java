package com.unieus.garajea.core.services;

import com.unieus.garajea.core.exception.ZerbitzuSalbuespena;
import com.unieus.garajea.model.dao.LangileaDAO;
import com.unieus.garajea.model.entities.Langilea;
/**
 * Langilea entitatearen negozio logika kudeatzen duen zerbitzua.
 */
public class LangileaService {

    // DAOFactory-k sortutako LangileaDAO erabiliko dugu
    private final LangileaDAO langileaDAO; 

    public LangileaService(LangileaDAO langileaDAO) {
        this.langileaDAO = langileaDAO;
    }

    /**
     * 1. Pasahitza hasheatu barik.
     * 2. Langilea bilatu datu basean.
     * @return Langilea objektua.
     * @throws ZerbitzuSalbuespena pasahitz edo emaila okerra bada.
     */
    public Langilea saioaHasi(String emaila, String pasahitza) {

        Langilea langilea =
            langileaDAO.getByEmailaPasahitza(emaila, pasahitza);

        if (langilea == null) {
            throw new ZerbitzuSalbuespena(
                "Emaila edo pasahitza ez dira zuzenak."
            );
        }

        return langilea;
    }

    /**
     * Langilea IDaren arabera bilatu.
     * @return Langilea objektua edo null.
     */
    public Langilea findById(int langileaId) {
         // Eskaera azpitik dagoen DAO-ri pasatu
        return langileaDAO.findById(langileaId);
    }
}

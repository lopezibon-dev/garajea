package com.unieus.garajea.model.dao;

import com.unieus.garajea.model.entities.Erreminta;
import java.util.List;

/**
 * Erreminta (Herramienta) entitatearen datu sarbidearen interfazea.
 */
public interface ErremintaDAO {
    
    void save(Erreminta erreminta);
    void update(Erreminta erreminta);
    void delete(int erremintaId);
    
    Erreminta findById(int erremintaId);
    List<Erreminta> findAll();

    /**
     * Erremintak motaren arabera aurkitzen ditu.
     * @param mota bilatu beharreko mota (Adib: "Giltza", "Destorlojogailua")
     * @return Erreminta zerrenda.
     */
    List<Erreminta> findByMota(String mota);

}

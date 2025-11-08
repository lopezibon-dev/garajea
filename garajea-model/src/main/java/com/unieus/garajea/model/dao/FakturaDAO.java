package com.unieus.garajea.model.dao;

import com.unieus.garajea.model.entities.Faktura;
import java.util.List;

/**
 * Faktura entitatearen datu sarbidearen interfazea.
 */
public interface FakturaDAO {
    
    void save(Faktura faktura);
    
    Faktura findById(int fakturaId);
    List<Faktura> findAll();

    /**
     * Erreserba bati dagokion faktura aurkitzen du.
     * @param erreserbaId bilatu beharreko erreserbaren IDa.
     * @return Faktura objektua edo null.
     */
    Faktura findByErreserbaId(int erreserbaId);
}

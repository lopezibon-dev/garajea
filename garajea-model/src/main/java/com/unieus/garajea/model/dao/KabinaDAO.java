package com.unieus.garajea.model.dao;

import com.unieus.garajea.model.entities.Kabina;
import java.util.List;

/**
 * Kabina entitatearen datu sarbidearen interfazea.
 */
public interface KabinaDAO {
    
    void save(Kabina kabina);
    void update(Kabina kabina);
    void delete(int kabinaId);
    
    Kabina findById(int kabinaId);
    List<Kabina> findAll();
    List<Integer> findAllIds();

    /**
     * Kabina bat izenaren arabera aurkitzen du.
     * @param izena bilatu beharreko izena.
     * @return Kabina objektua edo null.
     */
    Kabina findByIzena(String izena);
}

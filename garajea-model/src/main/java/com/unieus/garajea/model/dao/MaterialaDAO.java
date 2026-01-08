package com.unieus.garajea.model.dao;

import com.unieus.garajea.model.entities.Materiala;
import java.util.List;

/**
 * Materiala (Material) entitatearen datu sarbidearen interfazea.
 */
public interface MaterialaDAO {
    
    void save(Materiala materiala);
    void update(Materiala materiala);
    void delete(int materialaId);
    
    Materiala findById(int materialaId);
    List<Materiala> findAll();

    /**
     * Material bat izenaren arabera bilatzen du.
     * @param izena bilatu beharreko materialaren izena.
     * @return Materiala objektua edo null.
     */
    Materiala findByIzena(String izena);

}
package com.unieus.garajea.model.dao;

import com.unieus.garajea.model.entities.Langilea;
import java.util.List;

/**
 * Langilea (Empleado) entitatearen datu sarbidearen interfazea.
 */
public interface LangileaDAO {
    
    void save(Langilea langilea);
    void update(Langilea langilea);
    void delete(int langileaId);
    
    Langilea findById(int langileaId);
    List<Langilea> findAll();

    /**
     * Lanpostuaren arabera langile guztiak aurkitzen ditu.
     * @param lanpostua bilatu beharreko lanpostua.
     * @return Langile zerrenda.
     */
    List<Langilea> findByLanpostua(String lanpostua);
    
    /**
     * Erabiltzaile izenaren arabera langile bat aurkitzen du autentifikazioa egiteko.
     * @param erabiltzailea login izena.
     * @return Langilea objektua edo null.
     */
    Langilea findByErabiltzailea(String erabiltzailea);
}
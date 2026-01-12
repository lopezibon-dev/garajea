package com.unieus.garajea.model.dao;

import com.unieus.garajea.model.entities.Langilea;
import java.util.List;

/**
 * Langilea entitatearen datu atzipenerako interfazea.
 */
public interface LangileaDAO {
    
    void save(Langilea langilea);
    void update(Langilea langilea);
    void delete(int langileaId);
    
    Langilea findById(int langileaId);
    List<Langilea> findAll();
    List<Integer> findMekanikariIds();

    /**
     * Kategoriaren arabera langile guztiak bilatzen ditu.
     * @param kategoria bilatu beharreko kategoria.
     * @return Langile zerrenda.
     */
    List<Langilea> findByKategoria(String kategoria);
    
    /**
     * Emaila eta pasahitzaren arabera langile bat bilatzen du autentifikazioa egiteko.
     * @param emaila login emaila.
     * @param pasahitza login pasahitza.
     * @return Langilea objektua edo null.
     */
    Langilea getByEmailaPasahitza(String emaila, String pasahitza);
}
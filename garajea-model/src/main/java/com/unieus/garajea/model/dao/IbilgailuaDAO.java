package com.unieus.garajea.model.dao;

import com.unieus.garajea.model.entities.Ibilgailua;
import java.util.List;

/**
 * Ibilgailua entitatearen datu sarbidearen interfazea.
 */
public interface IbilgailuaDAO {
    
    void save(Ibilgailua ibilgailua);
    void update(Ibilgailua ibilgailua);
    void delete(int ibilgailuaId);
    
    Ibilgailua findById(int ibilgailuaId);
    List<Ibilgailua> findAll();
    
    /**
     * Matrikularen arabera ibilgailu bat aurkitzen du.
     * @param matrikula bilatu beharreko matrikula.
     * @return Ibilgailua objektua edo null.
     */
    Ibilgailua findByMatrikula(String matrikula);

    /**
     * Bezero baten ibilgailu guztiak aurkitzen ditu.
     * @param bezeroaId jabearen IDa.
     * @return Ibilgailu zerrenda.
     */
    List<Ibilgailua> findByBezeroa(int bezeroaId);
}

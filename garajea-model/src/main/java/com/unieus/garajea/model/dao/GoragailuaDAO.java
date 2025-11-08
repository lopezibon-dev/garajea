package com.unieus.garajea.model.dao;

import com.unieus.garajea.model.entities.Goragailua;
import java.util.List;

/**
 * Goragailua entitatearen datu sarbidearen interfazea.
 */
public interface GoragailuaDAO {
    
    void save(Goragailua goragailua);
    void update(Goragailua goragailua);
    void delete(int goragailuaId);
    
    Goragailua findById(int goragailuaId);
    List<Goragailua> findAll();

    /**
     * Goragailuak egoeraren arabera aurkitzen ditu.
     * @param egoera bilatu beharreko egoera (Adib: "Libre", "Laneran", "Hondatuta").
     * @return Goragailu zerrenda.
     */
    List<Goragailua> findByEgoera(String egoera);

    /**
     * Kabina bati esleitutako goragailua aurkitzen du (1:1 harremana dela suposatuz).
     * @param kabinaId kabinaren IDa.
     * @return Goragailua objektua edo null.
     */
    Goragailua findByKabinaId(int kabinaId);
}

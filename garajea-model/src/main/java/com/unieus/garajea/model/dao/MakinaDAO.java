package com.unieus.garajea.model.dao;

import com.unieus.garajea.model.entities.Makina;
import java.util.List;

/**
 * Makina entitatearen datu sarbidearen interfazea.
 */
public interface MakinaDAO {
    
    void save(Makina makina);
    void update(Makina makina);
    void delete(int makinaId);
    
    Makina findById(int makinaId);
    List<Makina> findAll();

    /**
     * Makinak motaren arabera aurkitzen ditu.
     * @param mota bilatu beharreko mota (Adib: "Konpresorea", "Pieza-Garbitzailea").
     * @return Makina zerrenda.
     */
    List<Makina> findByMota(String mota);

    /**
     * Makinak egoeraren arabera aurkitzen ditu.
     * @param egoera bilatu beharreko egoera (Adib: "Libre", "Lanean", "Hondatuta").
     * @return Makina zerrenda.
     */
    List<Makina> findByEgoera(String egoera);
    
    /**
     * Kabina bati esleitutako makina guztiak aurkitzen ditu (1:N harremana).
     * @param kabinaId kabinaren IDa.
     * @return Makina zerrenda.
     */
    List<Makina> findByKabinaId(int kabinaId);
}
package com.unieus.garajea.model.dao;

import com.unieus.garajea.model.entities.Erreserba;
import com.unieus.garajea.model.entities.Materiala;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Erreserba entitatearen datu sarbidearen interfazea.
 */
public interface ErreserbaDAO {
    
    // CRUD Nagusia
    void save(Erreserba erreserba);
    void update(Erreserba erreserba);
    void delete(int erreserbaId);
    
    Erreserba findById(int erreserbaId);
    List<Erreserba> findAll();

    // Metodo espezifikoak
    
    /**
     * Bezero batek egindako erreserba guztiak aurkitzen ditu.
     * @param bezeroaId bezeroaren IDa.
     * @return Erreserba zerrenda.
     */
    List<Erreserba> findByBezeroa(int bezeroaId);

    /**
     * Data eta ordu tarte batean kabina bat erabilgarri dagoen egiaztatzen du.
     * @param kabinaId egiaztatu beharreko kabinaren IDa.
     * @param hasiera hasiera ordua.
     * @param amaiera amaiera ordua.
     * @return true erabilgarri badago, false bestela.
     */
    boolean isKabinaErabilgarri(int kabinaId, LocalDateTime hasiera, LocalDateTime amaiera);
    
    // Harremanak

    /**
     * Erreserba batean erosi diren materialak gehitzen ditu.
     * @param erreserbaId erreserbaren IDa.
     * @param materialaId erositako materialaren IDa.
     * @param kopurua erositako kopurua.
     */
    void addMateriala(int erreserbaId, int materialaId, int kopurua);

    /**
     * Erreserba baten erositako materialak lortzen ditu (Map<Materiala, Kopurua> moduan).
     * @param erreserbaId erreserbaren IDa.
     * @return Materialak eta haien kopurua.
     */
    Map<Materiala, Integer> getMaterialakByErreserba(int erreserbaId);
}

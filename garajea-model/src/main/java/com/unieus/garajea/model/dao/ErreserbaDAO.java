package com.unieus.garajea.model.dao;

import com.unieus.garajea.model.dto.ErreserbaInfoDTO;
import com.unieus.garajea.model.entities.Erreserba;
import com.unieus.garajea.model.entities.Materiala;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Erreserba entitatearen datu atzipenaren interfazea.
 */
public interface ErreserbaDAO {
    
    // CRUD Nagusia
    void save(Erreserba erreserba);
    void update(Erreserba erreserba);
    void delete(int erreserbaId);
    
    Erreserba findByErreserbaId(int erreserbaId);
    List<Erreserba> findAll();
    // Metodo espezifikoak
    
    /**
     * Erreserbak bilatzen ditu filtro ezberdinen arabera.
     * @param langileId Langile baten IDa.
     * @param kabinaId Kabina baten IDa.      
     * @param egoera Erreserbaren egoera: "Zain", "Martxan", "Burutua", "Ezeztatua"
     * @param hasiera hasiera data eta ordua.
     * @param amaiera amaiera data eta ordua.
     * @return Erreserba zerrenda.
     */
    List<Erreserba> bilatuErreserbaLista(Integer langileId, Integer kabinaId, String egoera, LocalDate hasiera, LocalDate amaiera);

    /**
     * ErreserbaInfo bilatzen du filtro ezberdinen arabera.
     * @param langileId Langile baten IDa.
     * @param kabinaId Kabina baten IDa.      
     * @param egoera Erreserbaren egoera: "Zain", "Martxan", "Burutua", "Ezeztatua"
     * @param hasiera hasiera data.
     * @param amaiera amaiera data.
     * @return ErreserbaInfo zerrenda.
     */
    List<ErreserbaInfoDTO> bilatuErreserbaInfoLista(Integer langileId, Integer kabinaId, String egoera, LocalDate hasiera, LocalDate amaiera);

    /**
     * Bezero batek egindako erreserba guztiak aurkitzen ditu.
     * @param bezeroaId bezeroaren IDa.
     * @return Erreserba zerrenda.
     */
    List<Erreserba> findByBezeroa(int bezeroaId);

    /**
     * Data eta ordu tarte batean kabina bat erabilgarri dagoen egiaztatzen du.
     * @param kabinaId egiaztatu beharreko kabinaren IDa.
     * @param hasiera hasiera data eta ordua.
     * @param amaiera amaiera data eta ordua.
     * @return true erabilgarri badago, false bestela.
     */
    boolean isKabinaErabilgarri(int kabinaId, LocalDateTime hasiera, LocalDateTime amaiera);
    
    // -----------------------------------------------------------------
    // N:M Harremana (ERRESERBA_MATERIALA)
    // -----------------------------------------------------------------

    /**
     * Erreserba batean erosi diren materialak gehitzen ditu taula laguntzailera.
     * @param erreserbaId erreserbaren IDa.
     * @param materialaId erositako materialaren IDa.
     * @param kopurua erositako kopurua.
     */
    void materialaGehitu(int erreserbaId, int materialaId, int kopurua);

    /**
     * Erreserba baten erositako materialak lortzen ditu (Map<Materiala, Kopurua> moduan).
     * @param erreserbaId erreserbaren IDa.
     * @return Materialak eta haien kopurua.
     */
    Map<Materiala, Integer> getMaterialakByErreserba(int erreserbaId);
}

package com.unieus.garajea.core.services;

import com.unieus.garajea.core.config.KonfigurazioaService;
import com.unieus.garajea.model.entities.Erreserba;
import com.unieus.garajea.model.dao.ErreserbaDAO;
import com.unieus.garajea.model.dto.ErreserbaInfoDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Zerbitzua: ErreserbaService
 * Erreserba entitatearen negozio logika kudeatzen du.
 */
public class ErreserbaService {

    // Interfazea erabili, ez inplementazioa
    private final ErreserbaDAO erreserbaDAO;
    private final KonfigurazioaService konfigurazioaService;

    // Dependentzia-injekzioa konstruktorearen bidez
    public ErreserbaService(ErreserbaDAO erreserbaDAO, KonfigurazioaService konfigurazioaService) {
        this.erreserbaDAO = erreserbaDAO;
        this.konfigurazioaService = konfigurazioaService;
    }

    /**
     * Erreserba baten iraupena kalkulatzen du minututan, lanaldiko orduak soilik kontuan hartuta.
     * @param e Erreserba objektua.
     * @return Erreserba iraupena minututan.
     */
    public long kalkulatuErreserbaIraupena(Erreserba e) {
        return konfigurazioaService.kalkulatuLanIraupenaMinututan(e.getHasiera(), e.getAmaiera());
    }
    
    /**
     * ErreserbaInfo objektu baten iraupena kalkulatzen du minututan, lanaldiko orduak soilik kontuan hartuta.
     * @param info ErreserbaInfo objektua.
     * @return ErreserbaInfo iraupena minututan.
     */
    public long kalkulatuErreserbaInfoIraupena(ErreserbaInfoDTO info) {
        return konfigurazioaService.kalkulatuLanIraupenaMinututan(info.getHasiera(), info.getAmaiera());
    }

    /**
     * Erreserbak bilatzen ditu filtro ezberdinen arabera.
     * @param langileId Langile baten IDa.
     * @param kabinaId Kabina baten IDa.      
     * @param egoera Erreserbaren egoera: "Zain", "Martxan", "Burutua", "Ezeztatua"
     * @param hasiera hasiera data.
     * @param amaiera amaiera data.
     * @return Erreserben Lista.
     */
    public List<Erreserba> bilatuErreserbaLista(int langileId, int kabinaId, String egoera, LocalDate hasiera, LocalDate amaiera) {
        return erreserbaDAO.bilatuErreserbaLista(langileId, kabinaId, egoera, hasiera, amaiera);
    }

    /**
     * Langile bati lotutako erreserbaInfo objektuak lortzen ditu, egoera zehatz batekin. Egoera null bada, guztiak itzuliko dira.
     * @param langileId
     * @param egoera
     * @return ErreserbaInfo zerrenda.
     */
    public List<ErreserbaInfoDTO> bilatuLangilearenErreserbak(int langileId, String egoera, LocalDate tarteaHasiera, LocalDate tarteaAmaiera) {
        return erreserbaDAO.bilatuErreserbaInfoLista(langileId, null, egoera, tarteaHasiera, tarteaAmaiera);
    }

    /**
     * Data jakin bateko erreserben informazioa lortzen du (kabina guztiena)
     * @return ErreserbaInfo zerrenda.
     */
    public List<ErreserbaInfoDTO> bilatuDatakoErreserbak(LocalDate data) {
        return erreserbaDAO.bilatuErreserbaInfoLista(null, null, null, data, data);
    }

    /**
     * Data jakin bateko erreserben informazioa kabina_id-aren arabera taldekatuak lortzen ditu
     * @param hasieraData
     * @param amaieraData
     * @return Map<Integer, List<ErreserbaInfo>> non gakoak kabina_id-ak diren.
     */
    public Map<Integer, List<ErreserbaInfoDTO>> bilatuErreserbakKabinaka(LocalDate hasieraData, LocalDate amaieraData) {
        // DAOari deitu, egun bereko hasiera eta amaiera erabiliz
        List<ErreserbaInfoDTO> erreserbaInfoZerrenda = erreserbaDAO.bilatuErreserbaInfoLista(null, null, null, hasieraData, amaieraData);

        // BERTSIO TRADIZIONALA (Streams gabe):
        //
        // Map<Integer, List<ErreserbaInfo>> ErreserbaMapa = new HashMap<>();
        // for (ErreserbaInfo eri : zerrenda) {
        //     Integer kabinaId = eri.getKabinaId();
        //     List<ErreserbaInfo> z = ErreserbaMapa.get(kabinaId);
        //     if (z == null) {
        //         z = new ArrayList<>();
        //         ErreserbaMapa.put(kabinaId, z);
        //     }
        //     z.add(eri);
        // }
        // return ErreserbaMapa;

        // Javako Streams eta Collectors.groupingBy erabiliz (kabina_id-aren arabera taldekatu)
          return erreserbaInfoZerrenda.stream()
                .collect(Collectors.groupingBy(ErreserbaInfoDTO::getKabinaId));
    }

    /**
     * Kabina jakin bateko erreserben informazioa, data batetik hasita, hurrengo 7 eguneko denbora-tartean
     * @param kabinaId
     * @param hasieraData
     * @return ErreserbaInfo zerrenda
     */
    public List<ErreserbaInfoDTO> bilatuKabinakoErreserbak(int kabinaId, LocalDate hasieraData) {
        LocalDate amaieraData = hasieraData.plusDays(7);
        return erreserbaDAO.bilatuErreserbaInfoLista(null, kabinaId, null, hasieraData, amaieraData);
    }

}

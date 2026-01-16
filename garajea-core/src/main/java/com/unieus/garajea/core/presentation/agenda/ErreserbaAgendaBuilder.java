package com.unieus.garajea.core.presentation.agenda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import com.unieus.garajea.core.config.KonfigurazioaService;
import com.unieus.garajea.model.dto.ErreserbaInfoDTO;

/**
 * Erreserba agendak prestatzeko laguntzailea.
 */
public class ErreserbaAgendaBuilder {
    
    private final KonfigurazioaService konfigurazioaService;
    
    /**
     * Eraikitzailea.
     * 
     * @param konfigurazioaService Konfigurazio zerbitzua
     */
    public ErreserbaAgendaBuilder(KonfigurazioaService konfigurazioaService) {
        this.konfigurazioaService = konfigurazioaService;
    }

    /**
     * Erreserba agendako blokeak sortzen ditu emandako erreserba informazioen eta data-tartearen arabera.
     * 
     * @param erreserbak Erreserba informazioen zerrenda
     * @param hasieraData Agenda hasierako data
     * @param amaieraData Agenda amaierako data
     * @return AgendaBlokeaDTO objektuen zerrenda, ordenatuta data eta orduaren arabera
     */    
    public List<AgendaBlokeaDTO> sortuAgenda(
        List<ErreserbaInfoDTO> erreserbak,
        LocalDate hasieraData,
        LocalDate amaieraData) {
       
        List<AgendaBlokeaDTO> blokeak = new ArrayList<>();
        LocalTime lanaldiHasiera = konfigurazioaService.getLanaldiHasiera();
        LocalTime lanaldiAmaiera = konfigurazioaService.getLanaldiAmaiera();
       
		// erreserba lista korritzeko indizea
        int erreserbaIndex = 0;
       
		// data-tarteko egunak korritu, uneko egunaren kurtsorea erabiliz
        for (LocalDate egunKurtsorea = hasieraData;
            !egunKurtsorea.isAfter(amaieraData);
            egunKurtsorea = egunKurtsorea.plusDays(1)) {
           
			// Egunaren banatzailea gehitu agendari
            blokeak.add(AgendaBlokeaDTO.sortuEgunBanatzailea(egunKurtsorea,
                egunKurtsorea.getDayOfMonth() + " (" +
                egunKurtsorea.getDayOfWeek().getDisplayName(
                    TextStyle.FULL,
                    Locale.of("eu")
                ) + ")"));
           
			// Uneko eguna lanaldi tartera mugatu
            LocalDateTime egunHasiera = egunKurtsorea.atTime(lanaldiHasiera);
            LocalDateTime egunAmaiera = egunKurtsorea.atTime(lanaldiAmaiera);
           
			// uneko egunaren ordua kurtsorea, lanaldiHasiera-ra hasieratua
            LocalTime orduaKurtsorea = lanaldiHasiera;
           
			// uneko egunarekin interserktatzen duten erreserbak prozesatu
            while (erreserbaIndex < erreserbak.size()) {
                ErreserbaInfoDTO erreserba = erreserbak.get(erreserbaIndex);
                LocalDateTime erreserbaHasiera = erreserba.getHasiera();
                LocalDateTime erreserbaAmaiera = erreserba.getAmaiera();
               
				// uneko erreserba bukatzen bada uneko eguna baino lehenago, erreserba listaren indizea inkrementatu
                if (erreserbaAmaiera.isBefore(egunHasiera)) {
                    erreserbaIndex++;
                    continue;
                }
               
				// uneko erreserba hasten bada uneko egunaren ondoren, uneko eguna amaitutzat eman
                if (erreserbaHasiera.isAfter(egunAmaiera)) {
                    break;
                }
               
				// Erreserba eta uneko egunaren arteko intersekzioa kalkulatu
                LocalDateTime intersectHasiera =
                    erreserbaHasiera.isBefore(egunHasiera) ? egunHasiera : erreserbaHasiera;
                LocalDateTime intersectAmaiera =
                    erreserbaAmaiera.isAfter(egunAmaiera) ? egunAmaiera : erreserbaAmaiera;
               
                LocalTime intersekzioHasieraOrdua = intersectHasiera.toLocalTime();
                LocalTime intersekzioAmaieraOrdua = intersectAmaiera.toLocalTime();
               
				// Baldin eta tarte librerik badago orduaKurtsorearen eta intersekzioaren hasieraren artean, tarte librea sartu agendan
                if (orduaKurtsorea.isBefore(intersekzioHasieraOrdua)) {
                    blokeak.add(AgendaBlokeaDTO.sortuBlokeLibrea(
                            egunKurtsorea,
                            orduaKurtsorea,
                            intersekzioHasieraOrdua,
                            konfigurazioaService.kalkulatuLanIraupenaMinututan(
                            orduaKurtsorea, intersekzioHasieraOrdua)
                        )
                    );
                }
               
				// Erreserba intersekzioaren blokea agendan sartu
                long iraupena = konfigurazioaService.kalkulatuLanIraupenaMinututan(
                    intersekzioHasieraOrdua, intersekzioAmaieraOrdua);
                blokeak.add(AgendaBlokeaDTO.sortuErreserbaBlokea(
                    erreserba, egunKurtsorea, intersekzioHasieraOrdua, intersekzioAmaieraOrdua, iraupena));
               
				// orduaKurtsorea eguneratu
                orduaKurtsorea = intersekzioAmaieraOrdua;
               
				// Erreserba uneko egunaren amaieran edo lehenago bukatzen bada, erreserba listaren indizea inkrementatu
                if (erreserbaAmaiera.isBefore(egunAmaiera) || erreserbaAmaiera.equals(egunAmaiera)) {
                    erreserbaIndex++;
                } else {
					// Erreserba hurrengo egunean jarraitzen du, beraz erreserba listaren indizea EZ inkrementatu
					// eta uneko egunaren erreserbak korritzeko begiztatik atera.
                    break;
                }
            }
           
			// Uneko egunaren erreserbak prozesatu ondoren, baldin eta tarte librerik badago eguneko amaierararte 
            if (orduaKurtsorea.isBefore(lanaldiAmaiera)) {
                blokeak.add(AgendaBlokeaDTO.sortuBlokeLibrea(
                    egunKurtsorea,
                    orduaKurtsorea,
                    lanaldiAmaiera
                    , konfigurazioaService.kalkulatuLanIraupenaMinututan(orduaKurtsorea, lanaldiAmaiera)
                    )
                );
            }
        }
       
        return blokeak;
    }

    /**
     * Erreserba-agendako blokeak sortzen ditu emandako erreserba informazioen eta data-tartearen arabera, kabina bakoitzeko, horrela kabina bakoitzeko agenda bat sortuz.
     * 
     * @param erreserbak Erreserba informazioen zerrenda
     * @param hasieraData Agenda hasierako data
     * @param amaieraData Agenda amaierako data
     * @return AgendaBlokeaDTO objektuen zerrenda, ordenatuta data eta orduaren arabera
     */    
    public Map<String, List<AgendaBlokeaDTO>> sortuAgendakKabinaka(
        List<ErreserbaInfoDTO> erreserbaInfoZerrenda,
        LocalDate hasieraData,
        LocalDate amaieraData) {
        Map<String, List<ErreserbaInfoDTO>> ErreserbaZerrendaMapa = new TreeMap<>();
        for (ErreserbaInfoDTO erreserba : erreserbaInfoZerrenda) {
             String kabinaIzena = erreserba.getKabinaIzena();
             ErreserbaZerrendaMapa.computeIfAbsent(kabinaIzena, k -> new ArrayList<>()).add(erreserba);
        }

	    Map<String, List<AgendaBlokeaDTO>> AgendaMapa = new TreeMap<>();
        for (Map.Entry<String, List<ErreserbaInfoDTO>> entry : ErreserbaZerrendaMapa.entrySet()) {
            AgendaMapa.computeIfAbsent(entry.getKey(), k -> sortuAgenda(entry.getValue(), hasieraData, amaieraData));
        }

        return AgendaMapa;
    } 


}

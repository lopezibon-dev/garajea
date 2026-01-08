package com.unieus.garajea.core.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import com.unieus.garajea.model.dto.ErreserbaInfoDTO;

/**
 * Konfigurazio zerbitzua: negozio parametroak eskaintzen ditu.
 * Gainera, parametro horietan oinarritutako kalkulu-utilizateak eskaintzen ditu.
 */
public interface KonfigurazioaService {
    /**
     * Lanaldiaren hasiera ordua itzultzen du.
     * @return Lanaldiaren hasiera ordua (HH:mm formatuan)
     */
    LocalTime getLanaldiHasiera();

    /**
     * Lanaldiaren amaiera ordua itzultzen du.
     * @return Lanaldiaren amaiera ordua (HH:mm formatuan)
     */
    LocalTime getLanaldiAmaiera();

    /**
     * Bi data eta orduen arteko lan iraupena kalkulatzen du minututan,
     * lanaldiko orduak soilik kontuan hartuta.
     *
     * @param hasiera Hasiera data eta ordua.
     * @param amaiera Amaiera data eta ordua.
     * @return Lan iraupena minututan.
     */
    default long kalkulatuLanIraupenaMinututan(LocalDateTime hasiera, LocalDateTime amaiera) {
        long guztiraMinutuak = 0;
        
        LocalTime lanaldiHasiera = getLanaldiHasiera();
        LocalTime lanaldiAmaiera = getLanaldiAmaiera();
        
        LocalDate hasieraData = hasiera.toLocalDate();
        LocalDate amaieraData = amaiera.toLocalDate();
        
        for (LocalDate data = hasieraData; !data.isAfter(amaieraData); data = data.plusDays(1)) {
            LocalDateTime egunekoHasieraMuga = data.atTime(lanaldiHasiera);
            LocalDateTime egunekoAmaieraMuga = data.atTime(lanaldiAmaiera);
            
            LocalDateTime tarteHasiera = hasiera.isAfter(egunekoHasieraMuga) 
                ? hasiera : egunekoHasieraMuga;
            LocalDateTime tarteAmaiera = amaiera.isBefore(egunekoAmaieraMuga) 
                ? amaiera : egunekoAmaieraMuga;
            
            if (tarteHasiera.isBefore(tarteAmaiera)) {
                guztiraMinutuak += java.time.Duration.between(tarteHasiera, tarteAmaiera).toMinutes();
            }
        }
        return guztiraMinutuak;
    }
    
    /**
     * Bi orduen arteko lan iraupena kalkulatzen du minututan,
     * lanaldiko orduen barruan bakarrik.
     *
     * @param hasieraOrdua Hasiera ordua.
     * @param amaieraOrdua Amaiera ordua.
     * @return Lan iraupena minututan.
     */
    default long kalkulatuLanIraupenaMinututan(LocalTime hasieraOrdua, LocalTime amaieraOrdua) {
        LocalTime lanaldiHasiera = getLanaldiHasiera();
        LocalTime lanaldiAmaiera = getLanaldiAmaiera();
        
        // Lanaldiko mugen barruan egon behar dute
        LocalTime hasiera = hasieraOrdua.isBefore(lanaldiHasiera) 
            ? lanaldiHasiera : hasieraOrdua;
        LocalTime amaiera = amaieraOrdua.isAfter(lanaldiAmaiera) 
            ? lanaldiAmaiera : amaieraOrdua;
        
        if (hasiera.isBefore(amaiera)) {
            return java.time.Duration.between(hasiera, amaiera).toMinutes();
        }
        return 0;
    }
    
    /**
     * Denbora-tarte baten eta egun jakin baten intersekzioaren iraupena kalkulatzen 
     * du minututan, lanaldiko orduak soilik kontuan hartuta.
     * 
     * @param tarteHasiera Denbora-tartearen hasiera
     * @param tarteAmaiera Denbora-tartearen amaiera
     * @param eguna Kalkulatu nahi den eguna
     * @return intersekzioaren iraupena minututan
     */
    default long kalkulatuEgunekoLanIraupena(LocalDateTime tarteHasiera, LocalDateTime tarteAmaiera, ErreserbaInfoDTO erreserba, LocalDate eguna) {
        LocalTime lanaldiHasiera = getLanaldiHasiera();
        LocalTime lanaldiAmaiera = getLanaldiAmaiera();
        
        // Denbora-tarteak egun hori barne hartzen ez badu, iraupena 0
        if (tarteAmaiera.toLocalDate().isBefore(eguna) || 
            tarteHasiera.toLocalDate().isAfter(eguna)) {
            return 0;
        }
        
        // Egunerako hasiera eta amaiera orduak zehaztu
        LocalTime hasieraOrdua = (tarteHasiera.toLocalDate().equals(eguna))
            ? tarteHasiera.toLocalTime()
            : lanaldiHasiera;
            
        LocalTime amaieraOrdua = (tarteAmaiera.toLocalDate().equals(eguna))
            ? tarteAmaiera.toLocalTime()
            : lanaldiAmaiera;
        
        // Kalkulatu iraupena (lanaldiko orduak bakarrik)
        return kalkulatuLanIraupenaMinututan(hasieraOrdua, amaieraOrdua);
    }

    /**
     * Emandako ordua lanaldian dagoen ala ez egiaztatzen du.
     *
     * @param ordua Egiaztatu nahi den ordua.
     * @return true ordua lanaldian badago, bestela false.
     */
    default boolean daLanOrdua(LocalTime ordua) {
        return !ordua.isBefore(getLanaldiHasiera()) && !ordua.isAfter(getLanaldiAmaiera());
    }

    /**
     * Emandako ordua lanaldian dagoen egiaztatzen du eta,
     * beharrezkoa bada, lanaldiko mugara egokitzen du.
     * 
     * @param ordua Egokitu nahi den ordua
     * @return Lanaldiko mugen barruko ordua
     */
    default LocalTime egokituLanOrdura(LocalTime ordua) {
        LocalTime lanaldiHasiera = getLanaldiHasiera();
        LocalTime lanaldiAmaiera = getLanaldiAmaiera();
        
        if (ordua.isBefore(lanaldiHasiera)) {
            return lanaldiHasiera;
        } else if (ordua.isAfter(lanaldiAmaiera)) {
            return lanaldiAmaiera;
        }
        return ordua;
    }
}


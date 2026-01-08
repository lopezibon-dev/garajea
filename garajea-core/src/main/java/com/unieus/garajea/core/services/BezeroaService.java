package com.unieus.garajea.core.services;

import java.util.List;
import java.util.ArrayList;
import com.unieus.garajea.core.exception.ZerbitzuSalbuespena;
import com.unieus.garajea.core.util.SecurityUtil;
import com.unieus.garajea.model.dao.BezeroaDAO;
import com.unieus.garajea.model.entities.Bezeroa;
/**
 * Bezeroa entitatearen negozio logika kudeatzen duen zerbitzua.
 */
public class BezeroaService {

    // DAOFactory-k sortutako BezeroaDAO erabiliko dugu
    private final BezeroaDAO bezeroaDAO; 

    public BezeroaService(BezeroaDAO bezeroaDAO) {
        this.bezeroaDAO = bezeroaDAO;
    }

    /**
     * 1. Balidazioa: Emaila ez dagoela erregistratuta.
     * 2. Pasahitza hasheatu.
     * 3. Bezeroa gorde.
     * @return Bezeroa gordeta (IDarekin) edo null balidazioak huts egiten badu.
     */
    public Bezeroa erregistratu(Bezeroa bezeroa) {

        List<String> erroreak = new ArrayList<>();

        // Balidazioa: Emaila ez dago erregistratuta
        if (bezeroaDAO.existitzenDaEmaila(bezeroa.getEmaila())) {
            erroreak.add("Email helbidea jada erregistratuta dago.");
        }

         if (!erroreak.isEmpty()) {
            throw new ZerbitzuSalbuespena(erroreak);
         }
        // Pasahitza hasheatu
        String hashedPasahitza = SecurityUtil.hashPasahitza(bezeroa.getPasahitza());
        bezeroa.setPasahitza(hashedPasahitza);
        
        // Bezeroa gorde datu basean; ID-a esleituko zaio bezeroa objektuari, eta metodoak itzuliko du
        bezeroaDAO.save(bezeroa);
        return bezeroa;
    }

    /**
     * 1. Pasahitza hasheatu.
     * 2. Bezeroa bilatu datu basean.
     * @return Bezeroa objektua (saioa hasteko) edo null.
     */
    public Bezeroa saioaHasi(String emaila, String pasahitza) {
        String hashedPasahitza = SecurityUtil.hashPasahitza(pasahitza);
        Bezeroa bezeroa = bezeroaDAO.getByEmailaPasahitza(emaila, hashedPasahitza);
        // saioa hasteko kredentzialak okerrak badira, bezeroa null izango da, beraz salbuespena jaurti
        if (bezeroa == null) {
            throw new ZerbitzuSalbuespena("Email edo pasahitza okerrak.");
        }
        return bezeroa;
    }

    /**
     * Bezeroaren datu pertsonalak eguneratzen ditu (Emaila, Izena, Abizenak, Telefonoa).
     * Oharra: Pasahitza ez da hemen kudeatzen.
     */
    public boolean datuakEguneratu(Bezeroa bezeroa) {
        bezeroaDAO.update(bezeroa);
        return true;
    }

    /**
     * Bezeroaren pasahitza aldatu.
     */
    public void pasahitzaEguneratu(int bezeroaId, String pasahitzaBerria) {

        List<String> erroreak = new ArrayList<>();

        // if (pasahitzaBerria.length() < 8) {
        //     erroreak.add("Pasahitzak gutxienez 8 karaktere izan behar ditu");
        // }

        if (!erroreak.isEmpty()) {
        throw new ZerbitzuSalbuespena(erroreak);
        }

        String hashedPasahitza = SecurityUtil.hashPasahitza(pasahitzaBerria);
        
        bezeroaDAO.updatePasahitza(bezeroaId, hashedPasahitza);
    }

    public Bezeroa findById(int bezeroaId) {
         // Eskaera azpitik dagoen DAO-ri pasatu
        return bezeroaDAO.findById(bezeroaId);
    }
}

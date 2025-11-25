package com.unieus.garajea.core.service;

import com.unieus.garajea.model.dao.BezeroaDAO;
import com.unieus.garajea.model.dao.DAOFactory;
import com.unieus.garajea.model.entities.Bezeroa;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Zerbitzua: BezeroaService
 * Negozio logika kudeatzen du (hashing, balidazioak, etab.).
 */
public class BezeroaService {

    // DAOFactory-k sortutako BezeroaDAO erabiliko dugu
    private final BezeroaDAO bezeroaDAO; 

    public BezeroaService(BezeroaDAO bezeroaDAO) {
        this.bezeroaDAO = bezeroaDAO;
    }

    /**
     * Pasahitza SHA-256 erabiliz kodetzen du.
     */
    private String hashPasahitza(String pasahitza) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(pasahitza.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Errorea pasahitza kodetzean (Hashing)", e);
        }
    }

    /**
     * 1. Balidazioa: Emaila ez dagoela erregistratuta.
     * 2. Pasahitza hasheatu.
     * 3. Bezeroa gorde.
     * @return Bezeroa gordeta (IDarekin) edo null balidazioak huts egiten badu.
     */
    public Bezeroa erregistratu(Bezeroa bezeroa) {
        if (bezeroaDAO.existitzenDaEmaila(bezeroa.getEmaila())) {
            // Logika: Jada badago erabiltzailea
            return null; 
        }

        String pasahitzaHasheatua = hashPasahitza(bezeroa.getPasahitza());
        bezeroa.setPasahitza(pasahitzaHasheatua);
        
        bezeroaDAO.save(bezeroa);
        return bezeroa;
    }

    /**
     * 1. Pasahitza hasheatu.
     * 2. Bezeroa bilatu datu basean.
     * @return Bezeroa objektua (saioa hasita) edo null.
     */
    public Bezeroa saioaHasi(String emaila, String pasahitzaArgia) {
        String pasahitzaHasheatu = hashPasahitza(pasahitzaArgia);
        return bezeroaDAO.getByEmailaPasahitza(emaila, pasahitzaHasheatu);
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
    public boolean pasahitzaEguneratu(int bezeroaId, String pasahitzaBerria) {

        if (pasahitzaBerria == null || pasahitzaBerria.trim().isEmpty()) {
            return false; // Pasahitza ezin da hutsa izan.
        }

        String pasahitzaHasheatua = hashPasahitza(pasahitzaBerria);
        
        bezeroaDAO.updatePasahitza(bezeroaId, pasahitzaHasheatua);
        return true;
    }

    public Bezeroa findById(int bezeroaId) {
         // Eskaera azpitik dagoen DAO-ri pasatu
        return bezeroaDAO.findById(bezeroaId);
    }
}

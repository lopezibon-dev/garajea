package com.unieus.garajea.model.entities;

/**
 * Entitatea: Kabina
 * Taula: KABINA
 */
public class Kabina {
    
    private int kabinaId; // PK: kabina_id
    private String izena; // UK: izena
    
    // -----------------------------------------------------------------
    // Eraikitzaileak
    // -----------------------------------------------------------------
    public Kabina() {
    }

    public Kabina(int kabinaId, String izena) {
        this.kabinaId = kabinaId;
        this.izena = izena;
    }
    
    // -----------------------------------------------------------------
    // Getters eta Setters
    // -----------------------------------------------------------------

    public int getKabinaId() {
        return kabinaId;
    }

    public void setKabinaId(int kabinaId) {
        this.kabinaId = kabinaId;
    }

    public String getIzena() {
        return izena;
    }

    public void setIzena(String izena) {
        this.izena = izena;
    }

    @Override
    public String toString() {
        return "Kabina [id=" + kabinaId + ", izena=" + izena + "]";
    }
}
package com.unieus.garajea.model.entities;

/**
 * Entitatea: Erreminta (Herramienta)
 * Taula: ERREMINTA
 */
public class Erreminta {
    
    private int erremintaId; // PK: erreminta_id
    private String izena;    
    private String mota;     // (Adib: "Giltza", "Bihurkina")
    
    // -----------------------------------------------------------------
    // Eraikitzaileak
    // -----------------------------------------------------------------
    public Erreminta() {
    }

    public Erreminta(int erremintaId, String izena, String mota, String egoera) {
        this.erremintaId = erremintaId;
        this.izena = izena;
        this.mota = mota;
    }

    // -----------------------------------------------------------------
    // Getters eta Setters
    // -----------------------------------------------------------------

    public int getErremintaId() {
        return erremintaId;
    }

    public void setErremintaId(int erremintaId) {
        this.erremintaId = erremintaId;
    }

    public String getIzena() {
        return izena;
    }

    public void setIzena(String izena) {
        this.izena = izena;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    @Override
    public String toString() {
        return "Erreminta [id=" + erremintaId + ", izena=" + izena + ", mota=" + mota + "]";
    }
}
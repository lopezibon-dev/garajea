package com.unieus.garajea.model.entities;

/**
 * Entitatea: Makina
 * Taula: MAKINA
 */
public class Makina {
    
    private int makinaId;    // PK: makina_id
    private String izena;    
    private String mota;      // (Adib: "Konpresorea", "Pieza-Garbitzailea")
    private String egoera;    // (Adib: "Libre", "Lanean", "Hondatuta")
    private Integer kabinaId; // FK: kabina_id 
    
    // -----------------------------------------------------------------
    // Eraikitzaileak
    // -----------------------------------------------------------------
    public Makina() {
    }
    public Makina(int makinaId, String izena, String mota, String egoera, Integer kabinaId) {
        this.makinaId = makinaId;
        this.izena = izena;
        this.mota = mota;
        this.egoera = egoera;
        this.kabinaId = kabinaId;
    }

    // -----------------------------------------------------------------
    // Getters eta Setters
    // -----------------------------------------------------------------

    public int getMakinaId() {
        return makinaId;
    }

    public void setMakinaId(int makinaId) {
        this.makinaId = makinaId;
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

    public String getEgoera() {
        return egoera;
    }

    public void setEgoera(String egoera) {
        this.egoera = egoera;
    }

    public Integer getKabinaId() {
        return kabinaId;
    }

    public void setKabinaId(Integer kabinaId) {
        this.kabinaId = kabinaId;
    }

    @Override
    public String toString() {
        return "Makina [id=" + makinaId + ", izena=" + izena + ", mota=" + mota + ", egoera=" + egoera + ", kabinaId=" + kabinaId + "]";
    }
}
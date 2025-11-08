package com.unieus.garajea.model.entities;

/**
 * Entitatea: Goragailua (Elevador / Gato hidráulico)
 * Taula: GORAGAILUA
 */
public class Goragailua {
    
    private int goragailuaId; // PK: goragailua_id
    private String izena;     
    private String egoera;    // Adib.: "Libre", "Lanean", "Hondatuta"
    private Integer kabinaId;
    
    // -----------------------------------------------------------------
    // Eraikitzaileak
    // -----------------------------------------------------------------
    public Goragailua() {
    }

    public Goragailua(int goragailuaId, String izena, String egoera, Integer kabinaId) {
        this.goragailuaId = goragailuaId;
        this.izena = izena;
        this.egoera = egoera;
        this.kabinaId = kabinaId;
    }

    // -----------------------------------------------------------------
    // Getters eta Setters
    // -----------------------------------------------------------------

    public int getGoragailuaId() {
        return goragailuaId;
    }

    public void setGoragailuaId(int goragailuaId) {
        this.goragailuaId = goragailuaId;
    }

    public String getIzena() {
        return izena;
    }

    public void setIzena(String izena) {
        this.izena = izena;
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
        return "Goragailua [id=" + goragailuaId + ", izena=" + izena + ", egoera=" + egoera + ", kabinaId=" + kabinaId + "]";
    }
}
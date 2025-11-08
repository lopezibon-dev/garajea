package com.unieus.garajea.model.entities;

/**
 * Entitatea: Langilea
 * Taula: LANGILEA
 */
public class Langilea {
    
    private int langileaId; // PK: langilea_id
    private String izena;
    private String abizena;
    private String lanpostua;
    private String erabiltzailea;
    private String pasahitza;     
    
    // -----------------------------------------------------------------
    // Eraikitzaileak
    // -----------------------------------------------------------------
    public Langilea() {
    }
    public Langilea(int langileaId, String izena, String abizena, String lanpostua, String erabiltzailea, String pasahitza) {
        this.langileaId = langileaId;
        this.izena = izena;
        this.abizena = abizena;
        this.lanpostua = lanpostua;
        this.erabiltzailea = erabiltzailea;
        this.pasahitza = pasahitza;
    }

    // -----------------------------------------------------------------
    // Getters eta Setters
    // -----------------------------------------------------------------

    public int getLangileaId() {
        return langileaId;
    }

    public void setLangileaId(int langileaId) {
        this.langileaId = langileaId;
    }

    public String getIzena() {
        return izena;
    }

    public void setIzena(String izena) {
        this.izena = izena;
    }

    public String getAbizena() {
        return abizena;
    }

    public void setAbizena(String abizena) {
        this.abizena = abizena;
    }

    public String getLanpostua() {
        return lanpostua;
    }

    public void setLanpostua(String lanpostua) {
        this.lanpostua = lanpostua;
    }
    
    public String getErabiltzailea() {
        return erabiltzailea;
    }

    public void setErabiltzailea(String erabiltzailea) {
        this.erabiltzailea = erabiltzailea;
    }

    public String getPasahitza() {
        return pasahitza;
    }

    public void setPasahitza(String pasahitza) {
        this.pasahitza = pasahitza;
    }

    @Override
    public String toString() {
        return "Langilea [id=" + langileaId + ", izena=" + izena + ", abizena=" + abizena + ", lanpostua=" + lanpostua + ", erabiltzailea=" + erabiltzailea + "]";
    }
    // Oharra: pasahitza ez da toString() metodoan sartzen segurtasuna bermatzeko.
}
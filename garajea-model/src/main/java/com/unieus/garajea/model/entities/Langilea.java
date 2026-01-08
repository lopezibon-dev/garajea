package com.unieus.garajea.model.entities;

/**
 * Entitatea: Langilea
 * Taula: LANGILEA
 */
public class Langilea {
    
    private int langileaId; // PK: langilea_id
    private String izena;
    private String abizenak;
    private String kategoria;
    private String telefonoa;
    private String emaila;
    private String pasahitza;     
    
    // -----------------------------------------------------------------
    // Eraikitzaileak
    // -----------------------------------------------------------------
    public Langilea() {
    }
    public Langilea(int langileaId, String izena, String abizenak, String kategoria, String emaila, String pasahitza) {
        this.langileaId = langileaId;
        this.izena = izena;
        this.abizenak = abizenak;
        this.kategoria = kategoria;
        this.emaila = emaila;
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

    public String getAbizenak() {
        return abizenak;
    }

    public void setAbizenak(String abizenak) {
        this.abizenak = abizenak;
    }

    public String getKategoria() {
        return kategoria;
    }

    public void setKategoria(String kategoria) {
        this.kategoria = kategoria;
    }

    public String getTelefonoa() {
        return telefonoa;
    }
    
    public void setTelefonoa(String telefonoa) {
        this.telefonoa = telefonoa;
    }

    public String getEmaila() {
        return emaila;
    }

    public void setEmaila(String emaila) {
        this.emaila = emaila;
    }

    public String getPasahitza() {
        return pasahitza;
    }

    public void setPasahitza(String pasahitza) {
        this.pasahitza = pasahitza;
    }

    @Override
    public String toString() {
        return "Langilea [id=" + langileaId + ", izena=" + izena + ", abizenak=" + abizenak + ", kategoria=" + kategoria + ", emaila=" + emaila + "]";
    }
    // Oharra: pasahitza ez da toString() metodoan sartzen segurtasuna bermatzeko.
}
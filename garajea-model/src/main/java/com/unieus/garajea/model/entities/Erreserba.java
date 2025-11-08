package com.unieus.garajea.model.entities;

import java.time.LocalDateTime;

/**
 * Entitatea: Erreserba
 * Taula: ERRESERBA
 */
public class Erreserba {
    
    private int erreserbaId;          // PK: erreserba_id
    private int bezeroaId;            // FK: bezeroa_id
    private int ibilgailuaId;         // FK: ibilgailua_id
    private int kabinaId;             // FK: kabina_id
    private Integer langileaId;       // FK: langilea_id (hautazkoa, null izan daiteke)
    private LocalDateTime hasieraDataOrdua; // Data eta ordua: hasiera_data_ordua
    private LocalDateTime amaieraDataOrdua; // Data eta ordua: amaiera_data_ordua
    private String oharrak;
    private String egoera;            // Adib.: "Zain", "Martxan", "Burutua", "Ezeztatua"
    private Integer fakturaId;        // FK: faktura_id (hautazkoa, erreserba burutua izan arte null izango da)
    
    // -----------------------------------------------------------------
    // Eraikitzaileak
    // -----------------------------------------------------------------
    public Erreserba() {
    }

    public Erreserba(int erreserbaId, int bezeroaId, int ibilgailuaId, int kabinaId, Integer langileaId,
            LocalDateTime hasieraDataOrdua, LocalDateTime amaieraDataOrdua, String oharrak, String egoera,
            Integer fakturaId) {
        this.erreserbaId = erreserbaId;
        this.bezeroaId = bezeroaId;
        this.ibilgailuaId = ibilgailuaId;
        this.kabinaId = kabinaId;
        this.langileaId = langileaId;
        this.hasieraDataOrdua = hasieraDataOrdua;
        this.amaieraDataOrdua = amaieraDataOrdua;
        this.oharrak = oharrak;
        this.egoera = egoera;
        this.fakturaId = fakturaId;
    }

    // -----------------------------------------------------------------
    // Getters eta Setters
    // -----------------------------------------------------------------

    public int getErreserbaId() {
        return erreserbaId;
    }

    public void setErreserbaId(int erreserbaId) {
        this.erreserbaId = erreserbaId;
    }

    public int getBezeroaId() {
        return bezeroaId;
    }

    public void setBezeroaId(int bezeroaId) {
        this.bezeroaId = bezeroaId;
    }

    public int getIbilgailuaId() {
        return ibilgailuaId;
    }

    public void setIbilgailuaId(int ibilgailuaId) {
        this.ibilgailuaId = ibilgailuaId;
    }

    public int getKabinaId() {
        return kabinaId;
    }

    public void setKabinaId(int kabinaId) {
        this.kabinaId = kabinaId;
    }

    public Integer getLangileaId() {
        return langileaId;
    }

    public void setLangileaId(Integer langileaId) {
        this.langileaId = langileaId;
    }

    public LocalDateTime getHasieraDataOrdua() {
        return hasieraDataOrdua;
    }

    public void setHasieraDataOrdua(LocalDateTime hasieraDataOrdua) {
        this.hasieraDataOrdua = hasieraDataOrdua;
    }

    public LocalDateTime getAmaieraDataOrdua() {
        return amaieraDataOrdua;
    }

    public void setAmaieraDataOrdua(LocalDateTime amaieraDataOrdua) {
        this.amaieraDataOrdua = amaieraDataOrdua;
    }

    public String getOharrak() {
        return oharrak;
    }

    public void setOharrak(String oharrak) {
        this.oharrak = oharrak;
    }

    public String getEgoera() {
        return egoera;
    }

    public void setEgoera(String egoera) {
        this.egoera = egoera;
    }

    public Integer getFakturaId() {
        return fakturaId;
    }

    public void setFakturaId(Integer fakturaId) {
        this.fakturaId = fakturaId;
    }

    @Override
    public String toString() {
        return "Erreserba [id=" + erreserbaId + ", bezeroaId=" + bezeroaId + ", kabinaId=" + kabinaId + ", hasiera=" + hasieraDataOrdua + ", egoera=" + egoera + "]";
    }
}

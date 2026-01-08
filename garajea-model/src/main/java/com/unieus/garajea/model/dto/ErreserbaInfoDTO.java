package com.unieus.garajea.model.dto;

import java.time.LocalDateTime;
/**
 * Erreserba entitatearen informazio osagarria, beste entitate batzuekin elkartutako datuekin.
 */
public class ErreserbaInfoDTO {
    private int erreserbaId;
    private int bezeroaId;           
    private int ibilgailuaId;         
    private int kabinaId;             
    private Integer langileaId; // Null izan daiteke
    private LocalDateTime hasiera;
    private LocalDateTime amaiera;
    private String egoera;

    // Elkartutako datuak
    private String bezeroIzenAbizenak; 
    private String langileIzena;        
    private String kabinaIzena; 
    private String ibilgailuInfo;

    // -----------------------------------------------------------------

    // Eraikitzaileak
    public ErreserbaInfoDTO() {
    }

    public ErreserbaInfoDTO(int erreserbaId, int bezeroaId, int ibilgailuaId, int kabinaId, Integer langileaId,
            LocalDateTime hasiera, LocalDateTime amaiera, String egoera, String oharrak, String bezeroIzenAbizenak,
            String langileIzena, String kabinaIzena, String ibilgailuInfo) {
        this.erreserbaId = erreserbaId;
        this.bezeroaId = bezeroaId;
        this.ibilgailuaId = ibilgailuaId;
        this.kabinaId = kabinaId;
        this.langileaId = langileaId;
        this.hasiera = hasiera;
        this.amaiera = amaiera;
        this.egoera = egoera;
        this.bezeroIzenAbizenak = bezeroIzenAbizenak;
        this.langileIzena = langileIzena;
        this.kabinaIzena = kabinaIzena;
        this.ibilgailuInfo = ibilgailuInfo;
    }

    // Getter eta Setter metodoak
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

    public LocalDateTime getHasiera() {
        return hasiera;
    }

    public void setHasiera(LocalDateTime hasiera) {
        this.hasiera = hasiera;
    }

    public LocalDateTime getAmaiera() {
        return amaiera;
    }

    public void setAmaiera(LocalDateTime amaiera) {
        this.amaiera = amaiera;
    }

    public String getEgoera() {
        return egoera;
    }

    public void setEgoera(String egoera) {
        this.egoera = egoera;
    }

    public String getBezeroIzenAbizenak() {
        return bezeroIzenAbizenak;
    }

    public void setBezeroIzenAbizenak(String bezeroIzenAbizenak) {
        this.bezeroIzenAbizenak = bezeroIzenAbizenak;
    }

    public String getLangileIzena() {
        return langileIzena;
    }

    public void setLangileIzena(String langileIzena) {
        this.langileIzena = langileIzena;
    }

    public String getKabinaIzena() {
        return kabinaIzena;
    }

    public void setKabinaIzena(String kabinaIzena) {
        this.kabinaIzena = kabinaIzena;
    }

    public String getIbilgailuInfo() {
        return ibilgailuInfo;
    }

    public void setIbilgailuInfo(String ibilgailuInfo) {
        this.ibilgailuInfo = ibilgailuInfo;
    }  
    
}
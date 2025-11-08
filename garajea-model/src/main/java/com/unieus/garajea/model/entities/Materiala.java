package com.unieus.garajea.model.entities;

/**
 * Entitatea: Materiala
 * Taula: MATERIALA
 */
public class Materiala {
    
    private int materialaId; // PK: materiala_id
    private String izena;
    private String mota;
    private double prezioa;    
    private int stockKopurua; 
    
    // -----------------------------------------------------------------
    // Eraikitzaileak
    // -----------------------------------------------------------------
    public Materiala() {
    }
    
    public Materiala(int materialaId, String izena, String mota, double prezioa, int stockKopurua) {
        this.materialaId = materialaId;
        this.izena = izena;
        this.mota = mota;
        this.prezioa = prezioa;
        this.stockKopurua = stockKopurua;
    }

    // -----------------------------------------------------------------
    // Getters eta Setters
    // -----------------------------------------------------------------

    public int getMaterialaId() {
        return materialaId;
    }

    public void setMaterialaId(int materialaId) {
        this.materialaId = materialaId;
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
    
    // GETTERS ETA SETTERS BERRIAK
    public double getPrezioa() {
        return prezioa;
    }

    public void setPrezioa(double prezioa) {
        this.prezioa = prezioa;
    }

    public int getStockKopurua() {
        return stockKopurua;
    }

    public void setStockKopurua(int stockKopurua) {
        this.stockKopurua = stockKopurua;
    }

    @Override
    public String toString() {
        return "Materiala [id=" + materialaId + ", izena=" + izena + ", mota=" + mota + ", prezioa=" + prezioa + ", stock=" + stockKopurua + "]";
    }
}
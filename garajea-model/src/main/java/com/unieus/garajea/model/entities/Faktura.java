package com.unieus.garajea.model.entities;

import java.time.LocalDate;

/**
 * Entitatea: Faktura
 * Taula: FAKTURA
 */
public class Faktura {
    
    private int fakturaId;       // PK: faktura_id
    private int erreserbaId;     // FK: erreserba_id 
    private double zenbatekoa;   
    private LocalDate data;      // igortze-data
    
    // -----------------------------------------------------------------
    // Eraikitzaileak
    // -----------------------------------------------------------------
    public Faktura() {
    }

    public Faktura(int fakturaId, int erreserbaId, double zenbatekoa, LocalDate data) {
        this.fakturaId = fakturaId;
        this.erreserbaId = erreserbaId;
        this.zenbatekoa = zenbatekoa;
        this.data = data;
    }
    // -----------------------------------------------------------------
    // Getters eta Setters
    // -----------------------------------------------------------------

    public int getFakturaId() {
        return fakturaId;
    }

    public void setFakturaId(int fakturaId) {
        this.fakturaId = fakturaId;
    }

    public int getErreserbaId() {
        return erreserbaId;
    }

    public void setErreserbaId(int erreserbaId) {
        this.erreserbaId = erreserbaId;
    }

    public double getZenbatekoa() {
        return zenbatekoa;
    }

    public void setZenbatekoa(double zenbatekoa) {
        this.zenbatekoa = zenbatekoa;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Faktura [id=" + fakturaId + ", erreserbaId=" + erreserbaId + ", zenbatekoa=" + zenbatekoa + ", data=" + data + "]";
    }
}
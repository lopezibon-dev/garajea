package com.unieus.garajea.model.entities;

/**
 * Entitatea: Ibilgailua
 * Taula: IBILGAILUA
 */
public class Ibilgailua {
    
    private int ibilgailuaId; // PK: ibilgailua_id
    private String matrikula; // UK: matrikula
    private String marka;
    private String modeloa;
    private int urtea;
    private int bezeroaId; // FK: bezeroa_id
    
    // -----------------------------------------------------------------
    // Eraikitzaileak
    // -----------------------------------------------------------------
    public Ibilgailua() {
    }

    public Ibilgailua(int ibilgailuaId, String matrikula, String marka, String modeloa, int urtea, int bezeroaId) {
        this.ibilgailuaId = ibilgailuaId;
        this.matrikula = matrikula;
        this.marka = marka;
        this.modeloa = modeloa;
        this.urtea = urtea;
        this.bezeroaId = bezeroaId;
    }

    // -----------------------------------------------------------------
    // Getters eta Setters
    // -----------------------------------------------------------------
    
    public int getIbilgailuaId() {
        return ibilgailuaId;
    }

    public void setIbilgailuaId(int ibilgailuaId) {
        this.ibilgailuaId = ibilgailuaId;
    }

    public String getMatrikula() {
        return matrikula;
    }

    public void setMatrikula(String matrikula) {
        this.matrikula = matrikula;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getModeloa() {
        return modeloa;
    }

    public void setModeloa(String modeloa) {
        this.modeloa = modeloa;
    }

    public int getUrtea() {
        return urtea;
    }

    public void setUrtea(int urtea) {
        this.urtea = urtea;
    }

    public int getBezeroaId() {
        return bezeroaId;
    }

    public void setBezeroaId(int bezeroaId) {
        this.bezeroaId = bezeroaId;
    }
    
    @Override
    public String toString() {
        return "Ibilgailua [id=" + ibilgailuaId + ", matrikula=" + matrikula + ", marka=" + marka + ", modeloa=" + modeloa + ", urtea=" + urtea + ", bezeroaId=" + bezeroaId + "]";
    }
}

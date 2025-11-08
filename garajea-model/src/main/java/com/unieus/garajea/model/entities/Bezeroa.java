package com.unieus.garajea.model.entities;

public class Bezeroa {
    private int bezeroaId;
    private String izena;
    private String abizenak;
    private String emaila;
    private String telefonoa;
    private String pasahitza;

    public Bezeroa() {
    }

    public Bezeroa(int bezeroaId, String izena, String abizenak, String emaila, String telefonoa, String pasahitza) {
        this.bezeroaId = bezeroaId;
        this.izena = izena;
        this.abizenak = abizenak;
        this.emaila = emaila;
        this.telefonoa = telefonoa;
        this.pasahitza = pasahitza;
    }

    public int getBezeroaId() {
        return bezeroaId;
    }

    public void setBezeroaId(int bezeroaId) {
        this.bezeroaId = bezeroaId;
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

    public String getEmaila() {
        return emaila;
    }

    public void setEmaila(String emaila) {
        this.emaila = emaila;
    }

    public String getTelefonoa() {
        return telefonoa;
    }

    public void setTelefonoa(String telefonoa) {
        this.telefonoa = telefonoa;
    }

    public String getPasahitza() {
        return pasahitza;
    }

    public void setPasahitza(String pasahitza) {
        this.pasahitza = pasahitza;
    }

    @Override
    public String toString() {
        return "Bezeroa [bezeroaId=" + bezeroaId + ", izena=" + izena + ", abizenak=" + abizenak + ", emaila=" + emaila
                + ", telefonoa=" + telefonoa + ", pasahitza=" + pasahitza + "]";
    }

}

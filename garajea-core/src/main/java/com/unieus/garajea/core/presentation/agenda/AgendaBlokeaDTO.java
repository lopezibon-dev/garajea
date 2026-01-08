package com.unieus.garajea.core.presentation.agenda;

import java.time.LocalDate;
import java.time.LocalTime;
import com.unieus.garajea.model.dto.ErreserbaInfoDTO;

public class AgendaBlokeaDTO {
    public enum Mota {
        ERRESERBA,
        LIBREA,
        EGUNBANATZAILEA
    }
    
    private Mota mota;
    private long iraupenaMinututan; // erreserba/librea motentzat
    private LocalTime hasieraOrdua; // erreserba/librea motentzat
    private LocalTime amaieraOrdua; // erreserba/librea motentzat
    private LocalDate data;         // mota guztientzat
    
    // ERRESERBA-rako soilik
    private ErreserbaInfoDTO erreserbaInfo;
    
    // EGUNBANATZAILEA-rako soilik
    private String egunarenEtiketa; // Adib.: "11 (asteartea)"
    
    // Eraikitzaile estatikoak
    public static AgendaBlokeaDTO sortuErreserbaBlokea(
            ErreserbaInfoDTO erreserbaInfo, 
            LocalDate data,
            LocalTime hasiera,
            LocalTime amaiera,
            long iraupena) {
        
        AgendaBlokeaDTO blokea = new AgendaBlokeaDTO();
        blokea.mota = Mota.ERRESERBA;
        blokea.data = data;
        blokea.hasieraOrdua = hasiera;
        blokea.amaieraOrdua = amaiera;
        blokea.iraupenaMinututan = iraupena;
        blokea.erreserbaInfo = erreserbaInfo;
        return blokea;
    }
    
    public static AgendaBlokeaDTO sortuBlokeLibrea(
        LocalDate data,
        LocalTime hasiera,
        LocalTime amaiera,
        long iraupena) {
    
        AgendaBlokeaDTO blokea = new AgendaBlokeaDTO();
        blokea.setMota(Mota.LIBREA);
        blokea.setData(data);
        blokea.setHasieraOrdua(hasiera);
        blokea.setAmaieraOrdua(amaiera);
        blokea.setIraupenaMinututan(iraupena);
        return blokea;
    }

    public static AgendaBlokeaDTO sortuEgunBanatzailea(
        LocalDate data, 
        String etiketa) {

        AgendaBlokeaDTO blokea = new AgendaBlokeaDTO();
        blokea.setMota(Mota.EGUNBANATZAILEA);
        blokea.setData(data);
        blokea.setEgunarenEtiketa(etiketa);
        return blokea;
    }

    // Getters/setters...
    public Mota getMota() {
        return mota;
    }

    public void setMota(Mota mota) {
        this.mota = mota;
    }

    public long getIraupenaMinututan() {
        return iraupenaMinututan;
    }

    public void setIraupenaMinututan(long iraupenaMinututan) {
        this.iraupenaMinututan = iraupenaMinututan;
    }

    public LocalTime getHasieraOrdua() {
        return hasieraOrdua;
    }

    public void setHasieraOrdua(LocalTime hasieraOrdua) {
        this.hasieraOrdua = hasieraOrdua;
    }

    public LocalTime getAmaieraOrdua() {
        return amaieraOrdua;
    }

    public void setAmaieraOrdua(LocalTime amaieraOrdua) {
        this.amaieraOrdua = amaieraOrdua;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public ErreserbaInfoDTO getErreserbaInfo() {
        return erreserbaInfo;
    }

    public void setErreserbaInfo(ErreserbaInfoDTO erreserbaInfo) {
        this.erreserbaInfo = erreserbaInfo;
    }

    public String getEgunarenEtiketa() {
        return egunarenEtiketa;
    }

    public void setEgunarenEtiketa(String egunarenEtiketa) {
        this.egunarenEtiketa = egunarenEtiketa;
    }
    

    
}



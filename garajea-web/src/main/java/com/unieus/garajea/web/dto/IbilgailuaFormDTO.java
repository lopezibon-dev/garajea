package com.unieus.garajea.web.dto;

import java.util.List;
import com.unieus.garajea.model.entities.Ibilgailua;
import com.unieus.garajea.web.balidazioa.BalidazioTresnak;
import jakarta.servlet.http.HttpServletRequest;

public class IbilgailuaFormDTO {

    private Integer ibilgailuaId;
    private String matrikula; 
    private String marka;
    private String modeloa;
    private int urtea;
    private int bezeroaId;

    public IbilgailuaFormDTO() {
    }

    public IbilgailuaFormDTO(Integer ibilgailuaId, String matrikula, String marka, String modeloa,
            int urtea, int bezeroaId) {
        this.ibilgailuaId = ibilgailuaId;
        this.matrikula = matrikula;
        this.marka = marka;
        this.modeloa = modeloa;
        this.urtea = urtea;
        this.bezeroaId = bezeroaId;
    }

    public Integer getIbilgailuaId() {
        return ibilgailuaId;
    }

    public void setIbilgailuaId(Integer ibilgailuaId) {
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

    public static IbilgailuaFormDTO fromEntity(Ibilgailua ibilgailua) {
        IbilgailuaFormDTO dto = new IbilgailuaFormDTO();

        dto.setIbilgailuaId(ibilgailua.getIbilgailuaId());
        dto.setMatrikula(ibilgailua.getMatrikula());
        dto.setMarka(ibilgailua.getMarka());
        dto.setModeloa(ibilgailua.getModeloa());
        dto.setUrtea(ibilgailua.getUrtea());
        dto.setBezeroaId(ibilgailua.getBezeroaId());

        return dto;
    }
    
    public static IbilgailuaFormDTO fromRequest(
        HttpServletRequest request,
        List<String> erroreak) {

        IbilgailuaFormDTO dto = new IbilgailuaFormDTO();

        // hautazkoa, bi kasutan erabiltzeko: ibilgailu berria sortzean, edota sisteman dagoen ibilgailua editatzeko 
        dto.setIbilgailuaId(
            BalidazioTresnak.getOptionalInt(
                request, "ibilgailuaId", "Ibilgailua", erroreak
            )
        );
        dto.setMatrikula(
            BalidazioTresnak.getRequiredString(
                request, "matrikula", 20, "Matrikula", erroreak
            )
        );
        dto.setMarka(
            BalidazioTresnak.getRequiredString(
                request, "marka", 50, "Marka", erroreak
            )
        );
        dto.setModeloa(
            BalidazioTresnak.getRequiredString(
                request, "modeloa", 50, "Modeloa", erroreak
            )
        );
        dto.setUrtea(
            BalidazioTresnak.getOptionalIntInRange(
                request, "urtea", "Urtea",
                1900, java.time.LocalDate.now().getYear(),
                erroreak
            )
        );

        return dto;
    }

    public Ibilgailua toEntity() {
        Ibilgailua ibilgailua = new Ibilgailua();

        if (this.ibilgailuaId != null) {
            ibilgailua.setIbilgailuaId(this.ibilgailuaId);
        }

        ibilgailua.setMatrikula(this.matrikula);
        ibilgailua.setMarka(this.marka);
        ibilgailua.setModeloa(this.modeloa);
        ibilgailua.setUrtea(this.urtea);
        ibilgailua.setBezeroaId(this.bezeroaId);

        return ibilgailua;
    }

}
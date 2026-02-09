package com.unieus.garajea.web.dto;

import java.util.List;
import com.unieus.garajea.web.balidazioa.BalidazioTresnak;
import jakarta.servlet.http.HttpServletRequest;

public class IbilgailuaEzabatuFormDTO {

    private Integer ibilgailuaId;
    private Integer bezeroaId;

    public IbilgailuaEzabatuFormDTO() {
    }

    public IbilgailuaEzabatuFormDTO(Integer ibilgailuaId, Integer bezeroaId) {
        this.ibilgailuaId = ibilgailuaId;
        this.bezeroaId = bezeroaId;
    }

    public Integer getIbilgailuaId() {
        return ibilgailuaId;
    }

    public void setIbilgailuaId(Integer ibilgailuaId) {
        this.ibilgailuaId = ibilgailuaId;
    }

    public int getBezeroaId() {
        return bezeroaId;
    }

    public void setBezeroaId(int bezeroaId) {
        this.bezeroaId = bezeroaId;
    }    
    
    public static IbilgailuaEzabatuFormDTO fromRequest(
        HttpServletRequest request,
        List<String> erroreak) {

        IbilgailuaEzabatuFormDTO dto = new IbilgailuaEzabatuFormDTO();

        // derrigorrezkoa
        dto.setIbilgailuaId(
            BalidazioTresnak.getRequiredInt(
                request, "ibilgailuaId", "Ibilgailua", erroreak
            )
        );

        return dto;
    }

}
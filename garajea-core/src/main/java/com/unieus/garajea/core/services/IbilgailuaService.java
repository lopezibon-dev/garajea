package com.unieus.garajea.core.services;

import java.util.ArrayList;
import java.util.List;

import com.unieus.garajea.core.exception.ZerbitzuSalbuespena;
import com.unieus.garajea.model.dao.BezeroaDAO;
import com.unieus.garajea.model.dao.IbilgailuaDAO;
import com.unieus.garajea.model.entities.Bezeroa;
import com.unieus.garajea.model.entities.Ibilgailua;

public class IbilgailuaService {

    private final IbilgailuaDAO ibilgailuaDao;
    private final BezeroaDAO bezeroaDao;

    public IbilgailuaService(IbilgailuaDAO ibilgailuaDao, BezeroaDAO bezeroaDao) {
        this.ibilgailuaDao = ibilgailuaDao;
        this.bezeroaDao = bezeroaDao;
    }

    /**
     * Bezero baten ibilgailu guztiak lortzen ditu.
     */
    public List<Ibilgailua> bilatuBezeroarenIbilgailuak(int bezeroaId) {
        egiaztatuBezeroaExistitzenDela(bezeroaId);
        return ibilgailuaDao.findByBezeroa(bezeroaId);
    }

    /**
     * Ibilgailu bat bere IDaren arabera bilatzen du.
     */
    public Ibilgailua bilatuIbilgailua(int ibilgailuaId) {
        Ibilgailua ibilgailua = ibilgailuaDao.findById(ibilgailuaId);

        if (ibilgailua == null) {
            throw new ZerbitzuSalbuespena("Ibilgailua ez da existitzen");
        }

        return ibilgailua;
    }

    /**
     * Bezero bati lotutako ibilgailu berri bat sortzen du.
     */
    public void sortuIbilgailua(int bezeroaId, Ibilgailua ibilgailua) {
        List<String> erroreak = new ArrayList<>();

        Bezeroa bezeroa = bezeroaDao.findById(bezeroaId);
        if (bezeroa == null) {
            erroreak.add("Bezeroa ez da existitzen");
        }

        if (!erroreak.isEmpty()) {
            throw new ZerbitzuSalbuespena(erroreak);
        }

        ibilgailua.setBezeroaId(bezeroaId);
        ibilgailuaDao.save(ibilgailua);
    }

    /**
     * Bezero baten ibilgailu baten datuak eguneratzen ditu.
     */
    public void eguneratuIbilgailua(int bezeroaId, Ibilgailua ibilgailua) {
        List<String> erroreak = new ArrayList<>();

        Bezeroa bezeroa = bezeroaDao.findById(bezeroaId);
        if (bezeroa == null) {
            erroreak.add("Bezeroa ez da existitzen");
        }

        Ibilgailua existitzenDenIbilgailua =
                ibilgailuaDao.findById(ibilgailua.getIbilgailuaId());

        if (existitzenDenIbilgailua == null) {
            erroreak.add("Ibilgailua ez da existitzen");
        } else if (existitzenDenIbilgailua.getBezeroaId() != bezeroaId) {
            erroreak.add("Ibilgailua ez dagokio adierazitako bezeroari");
        }

        if (!erroreak.isEmpty()) {
            throw new ZerbitzuSalbuespena(erroreak);
        }

        ibilgailua.setBezeroaId(bezeroaId);
        ibilgailuaDao.update(ibilgailua);
    }

    /**
     * Bezero baten ibilgailu bat ezabatzen du.
     */
    public void ezabatuIbilgailua(int bezeroaId, int ibilgailuaId) {
        List<String> erroreak = new ArrayList<>();

        Bezeroa bezeroa = bezeroaDao.findById(bezeroaId);
        if (bezeroa == null) {
            erroreak.add("Bezeroa ez da existitzen");
        }

        Ibilgailua ibilgailua = ibilgailuaDao.findById(ibilgailuaId);
        if (ibilgailua == null) {
            erroreak.add("Ibilgailua ez da existitzen");
        } else if (ibilgailua.getBezeroaId() != bezeroaId) {
            erroreak.add("Ezin da beste bezero baten ibilgailua ezabatu");
        }

        if (!erroreak.isEmpty()) {
            throw new ZerbitzuSalbuespena(erroreak);
        }

        ibilgailuaDao.delete(ibilgailuaId);
    }

    private void egiaztatuBezeroaExistitzenDela(int bezeroaId) {
        if (bezeroaDao.findById(bezeroaId) == null) {
            throw new ZerbitzuSalbuespena("Bezeroa ez da existitzen");
        }
    }
}

package com.unieus.garajea.core.services;

import java.nio.file.Path;
import java.util.List;

import com.unieus.garajea.core.util.PythonEsportazioExecutor;
import com.unieus.garajea.core.exception.ZerbitzuSalbuespena;
import com.unieus.garajea.model.dao.DatubaseaMetaDAO;

public class DatubaseaService {

    private final DatubaseaMetaDAO datubaseaMetaDAO;
    private final PythonEsportazioExecutor pythonEsportazioExecutor;

    public DatubaseaService(DatubaseaMetaDAO datubaseaMetaDAO, PythonEsportazioExecutor pythonEsportazioExecutor) {
        this.datubaseaMetaDAO = datubaseaMetaDAO;
        this.pythonEsportazioExecutor = pythonEsportazioExecutor;
    }

    public List<String> getTaulaIzenak() throws ZerbitzuSalbuespena {
        try {
            return datubaseaMetaDAO.findTaulaIzenak();
        } catch (Exception e) {
            throw new ZerbitzuSalbuespena(
                "Errorea datu-baseko taulen zerrenda eskuratzean"
            );
        }
    }

    public void esportatuTaula(
            String taulaIzena,
            Path csvIrteera
    ) throws ZerbitzuSalbuespena {

        if (taulaIzena == null || taulaIzena.isBlank()) {
            throw new ZerbitzuSalbuespena("Taularen izena ezin da hutsik egon");
        }

        if (csvIrteera == null) {
            throw new ZerbitzuSalbuespena("CSV irteerako bidea ezin da null izan");
        }

        int exitCode;

        try {
            exitCode = pythonEsportazioExecutor.exekutatu(
                taulaIzena,
                csvIrteera
            );
        } catch (Exception e) {
            throw new ZerbitzuSalbuespena(
                "Errorea Python esportazio-prozesua exekutatzean"
            );
        }

        if (exitCode != 0) {
            throw new ZerbitzuSalbuespena(
                "Errorea taula esportatzean (exit code = " + exitCode + ")"
            );
        }
    }
}

package com.unieus.garajea.core.services;

import java.nio.file.Path;
import java.util.List;

import com.unieus.garajea.core.util.PythonEsportazioExecutor;
import com.unieus.garajea.core.util.PythonInportazioExecutor;
import com.unieus.garajea.core.exception.ZerbitzuSalbuespena;
import com.unieus.garajea.model.dao.DatubaseaMetaDAO;

public class DatubaseaService {

    private final DatubaseaMetaDAO datubaseaMetaDAO;
    private final PythonEsportazioExecutor pythonEsportazioExecutor;
    private final PythonInportazioExecutor pythonInportazioExecutor;

    public DatubaseaService(DatubaseaMetaDAO datubaseaMetaDAO, PythonEsportazioExecutor pythonEsportazioExecutor, PythonInportazioExecutor pythonInportazioExecutor) {
        this.datubaseaMetaDAO = datubaseaMetaDAO;
        this.pythonEsportazioExecutor = pythonEsportazioExecutor;
        this.pythonInportazioExecutor = pythonInportazioExecutor;
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
            throw new ZerbitzuSalbuespena("Irteerako CSV-ren izena ezin da null izan");
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

    public void inportatuTaula(
            String taulaIzena,
            Path csvSarrera
        ) throws ZerbitzuSalbuespena {

        if (taulaIzena == null || taulaIzena.isBlank()) {
            throw new ZerbitzuSalbuespena("Taularen izena ezin da hutsik egon");
        }

        if (csvSarrera == null) {
            throw new ZerbitzuSalbuespena("Sarrerako CSV-ren izena ezin da null izan");
        }

        int exitCode;

        try {
            exitCode = pythonInportazioExecutor.exekutatu(
                taulaIzena,
                csvSarrera
            );
        } catch (Exception e) {
            throw new ZerbitzuSalbuespena(
                "Errorea Python inportazio-prozesua exekutatzean"
            );
        }

        switch (exitCode) {
            case 0:
                return;
            case 2:
                throw new ZerbitzuSalbuespena(taulaIzena + 
                    " taula ez da existitzen edo ez du zutaberik"
                );
            case 6:
                throw new ZerbitzuSalbuespena(csvSarrera + 
                    " fitxategia ez da aurkitu"
            );
            default:
                throw new ZerbitzuSalbuespena(
                    "Errorea taula inportatzean (exit code = " + exitCode + ")"
            );
        }
    }    
}

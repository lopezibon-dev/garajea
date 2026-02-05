package com.unieus.garajea.core.util;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class PythonEsportazioExecutor {

    private final Path pythonExePath;
    private final Path scriptPath;

    public PythonEsportazioExecutor() {
        Path baseDir = Paths.get(System.getProperty("user.dir"));
        this.pythonExePath = baseDir.resolve(
            Paths.get("scripts", ".venv", "Scripts", "python.exe")
        );

        this.scriptPath = baseDir.resolve(
            Paths.get("scripts", "esportatu_taula.py")
        );

        if (!Files.exists(pythonExePath)) {
            throw new IllegalStateException(
                "Ez da aurkitu Python exekutagarria: " + pythonExePath
            );
        }

        if (!Files.exists(scriptPath)) {
            throw new IllegalStateException(
                "Ez da aurkitu Python script-a: " + scriptPath
            );
        }        
    }    

    public int exekutatu(String taulaIzena, Path csvIrteera) {

        if (taulaIzena == null || taulaIzena.isBlank()) {
            throw new IllegalArgumentException(
                "Taularen izena ezin da hutsik egon"
            );
        }

        if (csvIrteera == null) {
            throw new IllegalArgumentException(
                "CSV irteerako bidea ezin da null izan"
            );
        }

        List<String> command = new ArrayList<>();
        command.add(pythonExePath.toString());
        command.add(scriptPath.toString());
        command.add(taulaIzena);
        command.add(csvIrteera.toString());

        ProcessBuilder processBuilder = new ProcessBuilder(command);

        // Java aplikazioaren laneko karpeta mantentzen du
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();

            // Itxarote blokeatzailea -> EDT-tik kanpo deitu behar da
            int exitCode = process.waitFor();

            return exitCode;

        } catch (IOException e) {
            throw new RuntimeException(
                "Ezin izan da Python prozesua abiarazi. "
                + "Egiaztatu python.exe eta script-aren bideak.",
                e
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            throw new RuntimeException(
                "Python esportazio-prozesua eten da",
                e
            );
        }
    }
}


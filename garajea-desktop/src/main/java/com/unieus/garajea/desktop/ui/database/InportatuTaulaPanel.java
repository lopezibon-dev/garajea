package com.unieus.garajea.desktop.ui.database;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;
import com.unieus.garajea.desktop.bootstrap.DesktopAppBootstrap;
import com.unieus.garajea.core.services.context.ServiceContext;
import com.unieus.garajea.core.services.context.ServiceContextFactory;
import com.unieus.garajea.core.exception.ZerbitzuSalbuespena;
import com.unieus.garajea.core.services.DatubaseaService;

public class InportatuTaulaPanel extends JPanel {

    private JComboBox<String> taulaCombo;
    private JButton inportatuBotoia;
    private JLabel egoeraLabel;

    public InportatuTaulaPanel() {
        initUI();
        kargatuTaulak();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel goikoPanela = new JPanel(new FlowLayout(FlowLayout.LEFT));

        taulaCombo = new JComboBox<>();
        inportatuBotoia = new JButton("Inportatu");
        egoeraLabel = new JLabel(" ");

        inportatuBotoia.addActionListener(e -> inportatuTaula());

        goikoPanela.add(new JLabel("Taula:"));
        goikoPanela.add(taulaCombo);
        goikoPanela.add(inportatuBotoia);

        add(goikoPanela, BorderLayout.NORTH);
        add(egoeraLabel, BorderLayout.SOUTH);
    }

    private void kargatuTaulak() {
        ServiceContextFactory zerbitzuEsparruFaktoria =
            DesktopAppBootstrap.getServiceContextFactory();

        try (ServiceContext zerbitzuEsparrua =
                 zerbitzuEsparruFaktoria.open()) {

            DatubaseaService datubaseaService =
                zerbitzuEsparrua.getDatubaseaService();

            List<String> taulak =
                datubaseaService.getTaulaIzenak();

            for (String taulaIzena : taulak) {
                taulaCombo.addItem(taulaIzena);
            }

        } catch (ZerbitzuSalbuespena e) {
            egoeraLabel.setText(
                "Errorea taulen zerrenda kargatzean"
            );
        }
    }


    private void inportatuTaula() {
        ServiceContextFactory zerbitzuEsparruFaktoria =
            DesktopAppBootstrap.getServiceContextFactory();

        String hautatutakoTaula =
            (String) taulaCombo.getSelectedItem();

        if (hautatutakoTaula == null) {
            egoeraLabel.setText("Ez da taularik hautatu");
            return;
        }

        inportatuBotoia.setEnabled(false);
        egoeraLabel.setText("Inportatzen...");

        SwingWorker<Void, Void> worker =
            new SwingWorker<>() {

                @Override
                protected Void doInBackground() throws Exception {

                    Path csvSarrera = Paths.get(
                        hautatutakoTaula + ".csv"
                    );

                    try (ServiceContext zerbitzuEsparrua =
                             zerbitzuEsparruFaktoria.open()) {

                        zerbitzuEsparrua.getDatubaseaService()
                           .inportatuTaula(
                               hautatutakoTaula,
                               csvSarrera
                           );
                    }

                    return null;
                }

                @Override
                protected void done() {
                    inportatuBotoia.setEnabled(true);

                    try {
                        get(); // ExecutionException jaurtiko du salbuespena gertatzen bada
                        egoeraLabel.setText(
                            "Taula ondo inportatu da"
                        );
                    } catch (ExecutionException e) {

                        Throwable kausa = e.getCause();

                        if (kausa instanceof ZerbitzuSalbuespena) {
                            ZerbitzuSalbuespena zs =
                                (ZerbitzuSalbuespena) kausa;

                            egoeraLabel.setText(
                                String.join(
                                    " | ",
                                    zs.getErroreak()
                                )
                            );
                        } else {
                            egoeraLabel.setText(
                                "Errorea taula inportatzean"
                            );
                        }

                    } catch (InterruptedException e) {

                        egoeraLabel.setText(
                            "Inportazioa eten da"
                        );
                        Thread.currentThread().interrupt();
                    }
                }
            };

        worker.execute();
    }    

}
package com.unieus.garajea.desktop.ui.database;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.unieus.garajea.desktop.bootstrap.DesktopAppBootstrap;
import com.unieus.garajea.core.services.context.ServiceContext;
import com.unieus.garajea.core.services.context.ServiceContextFactory;
import com.unieus.garajea.core.exception.ZerbitzuSalbuespena;
import com.unieus.garajea.core.services.DatubaseaService;

public class ExportatuTaulaPanel extends JPanel {

    private JComboBox<String> taulaCombo;
    private JButton esportatuBotoia;
    private JLabel egoeraLabel;

    public ExportatuTaulaPanel() {
        setLayout(new BorderLayout());

        initUI();
        kargatuTaulak();
    }

    private void initUI() {

        JPanel goikoPanela = new JPanel(new FlowLayout(FlowLayout.LEFT));

        taulaCombo = new JComboBox<>();
        esportatuBotoia = new JButton("Esportatu");
        egoeraLabel = new JLabel(" ");

        esportatuBotoia.addActionListener(e -> esportatuTaula());

        goikoPanela.add(new JLabel("Taula:"));
        goikoPanela.add(taulaCombo);
        goikoPanela.add(esportatuBotoia);

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

    private void esportatuTaula() {
        ServiceContextFactory zerbitzuEsparruFaktoria =
            DesktopAppBootstrap.getServiceContextFactory();

        String hautatutakoTaula =
            (String) taulaCombo.getSelectedItem();

        if (hautatutakoTaula == null) {
            egoeraLabel.setText("Ez da taularik hautatu");
            return;
        }

        esportatuBotoia.setEnabled(false);
        egoeraLabel.setText("Esportatzen...");

        SwingWorker<Void, Void> worker =
            new SwingWorker<>() {

                @Override
                protected Void doInBackground() throws Exception {

                    Path csvIrteera = Paths.get(
                        hautatutakoTaula + ".csv"
                    );

                    try (ServiceContext zerbitzuEsparrua =
                             zerbitzuEsparruFaktoria.open()) {

                        zerbitzuEsparrua.getDatubaseaService()
                           .esportatuTaula(
                               hautatutakoTaula,
                               csvIrteera
                           );
                    }

                    return null;
                }

                @Override
                protected void done() {
                    esportatuBotoia.setEnabled(true);

                    try {
                        get(); // salbuespenak jasotzen ditu
                        egoeraLabel.setText(
                            "Taula ondo esportatu da"
                        );
                    } catch (Exception e) {
                        egoeraLabel.setText(
                            "Errorea taula esportatzean"
                        );
                    }
                }
            };

        worker.execute();
    }
}

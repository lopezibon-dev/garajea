package com.unieus.garajea.desktop.ui.profila;

import java.util.List;
import java.time.LocalDate;

import com.unieus.garajea.desktop.bootstrap.DesktopAppBootstrap;
import com.unieus.garajea.core.services.context.ServiceContext;
import com.unieus.garajea.core.services.context.ServiceContextFactory;
import com.unieus.garajea.core.services.ErreserbaService;
import com.unieus.garajea.model.entities.Langilea;
import com.unieus.garajea.model.dto.ErreserbaInfoDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class LangileErreserbakTablePanel extends JPanel {

    private final Langilea langilea;
    private JTable taula;

    public LangileErreserbakTablePanel(Langilea langilea) {
        this.langilea = langilea;
        setLayout(new BorderLayout());
        sortuUI();
        kargatuDatuak();
    }

    private void sortuUI() {
        taula = new JTable();
        add(new JScrollPane(taula), BorderLayout.CENTER);
    }

    public void kargatuDatuak() {
        ServiceContextFactory zerbitzuEsparruFaktoria =
            DesktopAppBootstrap.getServiceContextFactory();

        LocalDate gaur = LocalDate.now();
        LocalDate amaiera = gaur.plusDays(7);

        try (ServiceContext ZerbitzuEsparrua = zerbitzuEsparruFaktoria.open()) {

            ErreserbaService erreserbaZerbitzua = ZerbitzuEsparrua.getErreserbaService();

            List<ErreserbaInfoDTO> erreserbak =
                erreserbaZerbitzua.bilatuLangilearenErreserbak(
                    langilea.getLangileaId(),
                    "zain", // edo null
                    gaur,
                    amaiera
                );

            eguneratuTaula(erreserbak);

        } catch (Exception e) {
            // Errore teknikoa edo ZerbitzuSalbuespena
            JOptionPane.showMessageDialog(
                this,
                "Errore tekniko bat gertatu da erreserbak kargatzean.",
                "Errorea",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eguneratuTaula(List<ErreserbaInfoDTO> erreserbak) {

        DefaultTableModel model = new DefaultTableModel(
            new Object[] {
                "Data",
                "Hasiera",
                "Amaiera",
                "Kabina",
                "Egoera",
                "Bezeroa",
                "Ibilgailua"
            },
            0
        );

        for (ErreserbaInfoDTO e : erreserbak) {
            model.addRow(new Object[] {
                e.getHasiera().toLocalDate(),
                e.getHasiera().toLocalTime(),
                e.getAmaiera().toLocalTime(),
                e.getKabinaIzena(),
                e.getEgoera(),
                e.getBezeroIzenAbizenak(),
                e.getIbilgailuInfo()
            });
        }

        taula.setModel(model);
    }
}

package com.unieus.garajea.desktop.ui.profila;

import java.util.List;
import java.time.LocalDate;

import com.unieus.garajea.desktop.bootstrap.DesktopAppBootstrap;
import com.unieus.garajea.core.services.context.ServiceContext;
import com.unieus.garajea.core.services.ErreserbaService;
import com.unieus.garajea.core.presentation.agenda.ErreserbaAgendaBuilder;
import com.unieus.garajea.core.presentation.agenda.AgendaBlokeaDTO;
import com.unieus.garajea.model.entities.Langilea;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Component;

public class ErreserbakAgendaPanel extends JPanel {

    private final Langilea langilea;

    private JPanel agendaContentPanel;
    private JScrollPane scrollPane;

    public ErreserbakAgendaPanel(Langilea langilea) {
        this.langilea = langilea;
        initUI();
        kargatuAgenda();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        agendaContentPanel = new JPanel();
        agendaContentPanel.setLayout(
            new BoxLayout(agendaContentPanel, BoxLayout.Y_AXIS)
        );

        scrollPane = new JScrollPane(agendaContentPanel);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void kargatuAgenda() {

        LocalDate gaur = LocalDate.now();
        LocalDate amaiera = gaur.plusDays(7);

        try (ServiceContext ZerbitzuEsparrua =
                 DesktopAppBootstrap.getServiceContextFactory().open()) {

            ErreserbaService erreserbaZerbitzua = ZerbitzuEsparrua.getErreserbaService();

            var erreserbak =
                erreserbaZerbitzua.bilatuLangilearenErreserbak(
                    langilea.getLangileaId(),
                    null,
                    gaur,
                    amaiera
                );

            ErreserbaAgendaBuilder builder =
                new ErreserbaAgendaBuilder(ZerbitzuEsparrua.getKonfigurazioaService());

            List<AgendaBlokeaDTO> agenda =
                builder.sortuAgenda(erreserbak, gaur, amaiera);

            erakutsiAgenda(agenda);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Errore tekniko bat gertatu da agenda kargatzean.",
                "Errorea",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void erakutsiAgenda(List<AgendaBlokeaDTO> agenda) {

        agendaContentPanel.removeAll();

        for (AgendaBlokeaDTO b : agenda) {

            if (b.getMota() == AgendaBlokeaDTO.Mota.EGUNBANATZAILEA) {
                JLabel label = new JLabel(
                    "=== " + b.getEgunarenEtiketa() + " ==="
                );
                label.setAlignmentX(Component.LEFT_ALIGNMENT);
                agendaContentPanel.add(label);
            }

            if (b.getMota() == AgendaBlokeaDTO.Mota.ERRESERBA) {
                JLabel label = new JLabel(
                    b.getHasieraOrdua() + " - " +
                    b.getAmaieraOrdua() + " | " +
                    b.getErreserbaInfo().getKabinaIzena() + " | " +
                    b.getErreserbaInfo().getBezeroIzenAbizenak() + " - " +
                    b.getErreserbaInfo().getIbilgailuInfo()
                );
                label.setAlignmentX(Component.LEFT_ALIGNMENT);
                agendaContentPanel.add(label);

            }
        }
        agendaContentPanel.revalidate();
        agendaContentPanel.repaint();
    }
}

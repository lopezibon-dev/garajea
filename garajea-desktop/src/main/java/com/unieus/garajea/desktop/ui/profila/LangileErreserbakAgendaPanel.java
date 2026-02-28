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
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

public class LangileErreserbakAgendaPanel extends JPanel {

    private final Langilea langilea;

    private JPanel agendaContentPanel;
    private JScrollPane scrollPane;

    public LangileErreserbakAgendaPanel(Langilea langilea) {
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

        for (AgendaBlokeaDTO blokea : agenda) {

            if (blokea.getMota() == AgendaBlokeaDTO.Mota.EGUNBANATZAILEA) {
                JLabel label = new JLabel(
                    "= " + blokea.getEgunarenEtiketa() + " ="
                );
                label.setAlignmentX(Component.LEFT_ALIGNMENT);
				label.setOpaque(true); // GARRANTZIZKOA: JLabel-aren atzealdeko kolorea ikusteko beharrezkoa da
				label.setBackground(new Color(60, 63, 65)); // gris 'pizarra'
				label.setForeground(new Color(220, 220, 220)); // txuri suabea
				label.setFont(label.getFont().deriveFont(Font.BOLD));
				label.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
                agendaContentPanel.add(label);
            }

            if (blokea.getMota() == AgendaBlokeaDTO.Mota.ERRESERBA) {
				String html = """
					<html>
						<b>%s - %s</b> | %s<br>
						| %s<br>
						<i>%s</i>
					</html>
					""".formatted(
						blokea.getHasieraOrdua(),
						blokea.getAmaieraOrdua(),
						blokea.getErreserbaInfo().getKabinaIzena(),
						blokea.getErreserbaInfo().getBezeroIzenAbizenak(),
						blokea.getErreserbaInfo().getIbilgailuInfo()
					);                
                JLabel label = new JLabel(html);
                label.setAlignmentX(Component.LEFT_ALIGNMENT);
				// padding eman labelari, eta beheko borderra gehitu, labelen arteko bereizketa hobetzeko
				label.setBorder(
					BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder(4, 6, 4, 6), 
						BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY)
				));

                agendaContentPanel.add(label);
            }
        }
        agendaContentPanel.revalidate();
        agendaContentPanel.repaint();
    }
}

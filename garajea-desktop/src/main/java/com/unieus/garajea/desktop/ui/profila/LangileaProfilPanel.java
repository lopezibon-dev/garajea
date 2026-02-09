package com.unieus.garajea.desktop.ui.profila;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import com.unieus.garajea.model.entities.Langilea;

public class LangileaProfilPanel extends JPanel {

    private final Langilea langilea;

    private LangileErreserbakTablePanel tablePanel;
    private LangileErreserbakAgendaPanel agendaPanel;

    public LangileaProfilPanel(Langilea langilea) {
        this.langilea = langilea;
        initUI();
    }

    private void initUI() {

        setLayout(new BorderLayout());

        JLabel izenaLabel = new JLabel(
            "Langilea: " + langilea.getIzena() + " " + langilea.getAbizenak()
        );
        izenaLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
// top, left, bottom, right

        JTabbedPane tabs = new JTabbedPane();

        tablePanel = new LangileErreserbakTablePanel(langilea);
        agendaPanel = new LangileErreserbakAgendaPanel(langilea);

        tabs.add("Agenda", agendaPanel);
        tabs.add("Taula", tablePanel);

        JButton berrizKargatuBtn = new JButton("Berriz kargatu");
        berrizKargatuBtn.addActionListener(e -> berrizKargatu());

        JPanel oinekoPanela = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        oinekoPanela.add(berrizKargatuBtn);

        add(izenaLabel, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        add(oinekoPanela, BorderLayout.SOUTH);
    }

    private void berrizKargatu() {
        tablePanel.kargatuDatuak();
        agendaPanel.kargatuAgenda();
    }
}
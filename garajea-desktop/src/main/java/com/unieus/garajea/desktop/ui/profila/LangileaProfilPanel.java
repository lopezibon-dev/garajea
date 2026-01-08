package com.unieus.garajea.desktop.ui.profila;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import com.unieus.garajea.model.entities.Langilea;

public class LangileaProfilPanel extends JPanel {

    private final Langilea langilea;

    private ErreserbakTablePanel tablePanel;
    private ErreserbakAgendaPanel agendaPanel;

    public LangileaProfilPanel(Langilea langilea) {
        this.langilea = langilea;
        initUI();
    }

    private void initUI() {

        setLayout(new BorderLayout());

        JLabel izenaLabel = new JLabel(
            langilea.getIzena() + " " + langilea.getAbizenak()
        );

        JTabbedPane tabs = new JTabbedPane();

        tablePanel = new ErreserbakTablePanel(langilea);
        agendaPanel = new ErreserbakAgendaPanel(langilea);

        tabs.add("Taula", tablePanel);
        tabs.add("Agenda", agendaPanel);

        JButton berrizKargatuBtn = new JButton("Berriz kargatu");
        berrizKargatuBtn.addActionListener(e -> berrizKargatu());

        JPanel goikoBarra = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        goikoBarra.add(berrizKargatuBtn);

        add(izenaLabel, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        add(goikoBarra, BorderLayout.SOUTH);
    }

    private void berrizKargatu() {
        tablePanel.kargatuDatuak();
        agendaPanel.kargatuAgenda();
    }
}

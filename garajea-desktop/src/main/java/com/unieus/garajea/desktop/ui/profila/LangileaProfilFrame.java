package com.unieus.garajea.desktop.ui.profila;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import com.unieus.garajea.model.entities.Langilea;


public class LangileaProfilFrame extends JFrame {

    private final Langilea langilea;

    private ErreserbakTablePanel tablePanel;
    private ErreserbakAgendaPanel agendaPanel;

    public LangileaProfilFrame(Langilea langilea) {
        this.langilea = langilea;

        setTitle("Langilearen profila");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {
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

        setLayout(new BorderLayout());
        add(izenaLabel, BorderLayout.NORTH);
        add(goikoBarra, BorderLayout.SOUTH);
        add(tabs, BorderLayout.CENTER);
    }
    
    private void berrizKargatu() {
        tablePanel.kargatuDatuak();
        agendaPanel.kargatuAgenda();
    }
}


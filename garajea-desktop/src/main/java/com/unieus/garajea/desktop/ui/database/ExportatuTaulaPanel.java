package com.unieus.garajea.desktop.ui.database;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class ExportatuTaulaPanel extends JPanel {

    public ExportatuTaulaPanel() {
        setLayout(new BorderLayout());

        add(
            new JLabel("Exportatu taula – funtzionalitatea garatzen ari da"),
            BorderLayout.CENTER
        );
    }
}

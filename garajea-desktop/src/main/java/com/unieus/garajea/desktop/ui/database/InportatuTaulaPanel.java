package com.unieus.garajea.desktop.ui.database;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class InportatuTaulaPanel extends JPanel {

    public InportatuTaulaPanel() {
        setLayout(new BorderLayout());

        add(
            new JLabel("Inportatu taula – funtzionalitatea garatzen ari da"),
            BorderLayout.CENTER
        );
    }
}
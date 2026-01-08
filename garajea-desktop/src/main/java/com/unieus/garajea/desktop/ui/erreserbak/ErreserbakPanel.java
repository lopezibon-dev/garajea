package com.unieus.garajea.desktop.ui.erreserbak;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class ErreserbakPanel extends JPanel {

    public ErreserbakPanel() {
        setLayout(new BorderLayout());

        add(
            new JLabel("Erreserbak - funtzionalitatea garapen prozesuan."),
            BorderLayout.CENTER
        );
    }
}

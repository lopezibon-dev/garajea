package com.unieus.garajea.desktop;

import javax.swing.SwingUtilities;
import com.unieus.garajea.desktop.bootstrap.DesktopAppBootstrap;
import com.unieus.garajea.desktop.ui.login.LoginFrame;

public class DesktopMain {

    public static void main(String[] args) {

        DesktopAppBootstrap.init();

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
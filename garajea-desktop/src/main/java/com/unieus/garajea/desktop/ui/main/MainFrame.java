package com.unieus.garajea.desktop.ui.main;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import com.unieus.garajea.desktop.ui.database.ExportatuTaulaPanel;
import com.unieus.garajea.desktop.ui.database.InportatuTaulaPanel;
import com.unieus.garajea.desktop.ui.erreserbak.ErreserbakPanel;
import com.unieus.garajea.desktop.ui.login.LoginFrame;
import com.unieus.garajea.desktop.ui.profila.LangileaProfilPanel;
import com.unieus.garajea.model.entities.Langilea;

public class MainFrame extends JFrame {

    private final Langilea langilea;

    private JPanel contentPanel;
    private LangileaProfilPanel profilaPanel;

    public MainFrame(Langilea langilea) {
        this.langilea = langilea;
        // Interfazearen egitura finkoa
        initUI();
        // Edukin dinamikoa
        erakutsiProfila();
    }

    private void initUI() {
        setTitle("DIY Garajea");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        setJMenuBar(sortuMenuBar());

        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
    }

    private JMenuBar sortuMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuNagusia = new JMenu("Menua");

        JMenuItem profilaItem = new JMenuItem("Profila");
        JMenuItem erreserbakItem = new JMenuItem("Erreserbak");
        JMenuItem datuBaseaMenu = new JMenu("Datu-Basea");
        JMenuItem exportatuTaulaItem = new JMenuItem("Exportatu taula");
        JMenuItem inportatuTaulaItem = new JMenuItem("Inportatu taula");
        datuBaseaMenu.add(exportatuTaulaItem);
        datuBaseaMenu.add(inportatuTaulaItem);
        JMenuItem itxiSaioaItem = new JMenuItem("Itxi saioa");

        profilaItem.addActionListener(e -> erakutsiProfila());
        erreserbakItem.addActionListener(e -> erakutsiErreserbak());
        exportatuTaulaItem.addActionListener(e -> erakutsiExportatuTaula());
        itxiSaioaItem.addActionListener(e -> itxiSaioa());
        inportatuTaulaItem.addActionListener(e -> erakutsiInportatuTaula());

        menuNagusia.add(profilaItem);
        menuNagusia.add(erreserbakItem);
        menuNagusia.add(datuBaseaMenu);
        menuNagusia.addSeparator();
        menuNagusia.add(itxiSaioaItem);

        menuBar.add(menuNagusia);

        return menuBar;
    }

    /* ====== Menu ekintzak ====== */

    private void erakutsiProfila() {
        contentPanel.removeAll();

        if (profilaPanel == null) {
            profilaPanel = new LangileaProfilPanel(langilea);
        }

        contentPanel.add(profilaPanel, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void erakutsiErreserbak() {
        contentPanel.removeAll();
        contentPanel.add(new ErreserbakPanel(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void erakutsiExportatuTaula() {
        contentPanel.removeAll();
        contentPanel.add(new ExportatuTaulaPanel(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void erakutsiInportatuTaula() {
        contentPanel.removeAll();
        contentPanel.add(new InportatuTaulaPanel(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }


    private void itxiSaioa() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}


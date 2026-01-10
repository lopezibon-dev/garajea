package com.unieus.garajea.desktop.ui.login;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import com.unieus.garajea.desktop.bootstrap.DesktopAppBootstrap;
import com.unieus.garajea.desktop.ui.main.MainFrame;
import com.unieus.garajea.core.exception.ZerbitzuSalbuespena;
import com.unieus.garajea.core.services.context.ServiceContext;
import com.unieus.garajea.core.services.LangileaService;
import com.unieus.garajea.model.entities.Langilea;
import org.slf4j.LoggerFactory;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField pasahitzaField;

    public LoginFrame() {
        setTitle("DIY Garajea - Saioa hasi");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {

        JPanel panel = new JPanel(new GridBagLayout());
        // GridBagLayout-i aginduak emateko osagai bakoitza kokatzeko eta neurriak emateko
        GridBagConstraints gbc = new GridBagConstraints();

        // Insets: osagaiei padding-a aplikatzeko (px)
        gbc.insets = new Insets(8, 12, 8, 12);

        // Osagaia gelaxkan non kokatzeko (osagaia dagoen espazioa baino txikiagoa denean): defektuzko balioa
        gbc.anchor = GridBagConstraints.WEST;

        // --- Irudi korporatiboa ---
        ImageIcon logo = new ImageIcon(
            getClass().getResource("/images/desktop-logo1.png")
        );

        JLabel logoLabel = new JLabel(logo);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(logoLabel, gbc);

        // reset-a, osagaien defektuzko balioak berreskuratuz 
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // --- Emaila ---
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Emaila:"), gbc);

        emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(emailField, gbc);

        // --- Pasahitza ---
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(new JLabel("Pasahitza:"), gbc);

        pasahitzaField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(pasahitzaField, gbc);

        // --- Sartu botoia ---
        JButton loginBtn = new JButton("Sartu");
        loginBtn.addActionListener(e -> saioaHasi());

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(loginBtn, gbc);

        add(panel);
    }


    private void saioaHasi() {
        String emaila = emailField.getText();
        String pasahitza = new String(pasahitzaField.getPassword());

        try (ServiceContext sc =
            DesktopAppBootstrap.getServiceContextFactory().open()) {

            LangileaService langileaService =
                sc.getLangileaService();

            Langilea langilea =
                langileaService.saioaHasi(emaila, pasahitza);

            loginOndo(langilea);
        } catch (ZerbitzuSalbuespena ex) {
            String mezua = String.join("\n", ex.getErroreak());
            JOptionPane.showMessageDialog(
                this,
                mezua,
                "Errorea",
                JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            LoggerFactory.getLogger(getClass())
                .error("Errore teknikoa login-ean", ex);

            JOptionPane.showMessageDialog(
                this,
                "Errore tekniko bat gertatu da.",
                "Errorea",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loginOndo(Langilea langilea) {

        dispose();

        MainFrame mainFrame = new MainFrame(langilea);
        mainFrame.setVisible(true);
    }
}
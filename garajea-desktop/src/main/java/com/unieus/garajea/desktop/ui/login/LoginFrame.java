package com.unieus.garajea.desktop.ui.login;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.GridLayout;

import com.unieus.garajea.desktop.bootstrap.DesktopAppBootstrap;
import com.unieus.garajea.desktop.ui.profila.LangileaProfilFrame;
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
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(3, 2));

        panel.add(new JLabel("Emaila:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Pasahitza:"));
        pasahitzaField = new JPasswordField();
        panel.add(pasahitzaField);

        JButton loginBtn = new JButton("Sartu");
        loginBtn.addActionListener(e -> saioaHasi());

        panel.add(new JLabel());
        panel.add(loginBtn);

        add(panel);
    }

    private void saioaHasi() {
        String emaila = emailField.getText();
        String pasahitza = new String(pasahitzaField.getPassword());

        try (ServiceContext sc =
            DesktopAppBootstrap.getServiceContextFactory().open()) {

            LangileaService service =
                sc.getLangileaService();

            Langilea langilea =
                service.saioaHasi(emaila, pasahitza);

            dispose();
            new LangileaProfilFrame(langilea).setVisible(true);

        } catch (ZerbitzuSalbuespena ex) {
            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
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
}


package ma.gestionreparation.presentation.ui;

import ma.gestionreparation.dao.User;
import ma.gestionreparation.dao.enums.Role;
import ma.gestionreparation.exception.AuthException;
import ma.gestionreparation.presentation.controller.LoginController;
import ma.gestionreparation.presentation.ui.util.AppNavigator;
import ma.gestionreparation.presentation.ui.util.UiTheme;

import javax.swing.*;
import java.awt.*;

public class FrameLogin extends JFrame {

    private final LoginController controller;
    private final AppNavigator navigator;

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnCodeClient;
    private JLabel lblStatus;

    public FrameLogin(LoginController controller, AppNavigator navigator) {
        this.controller = controller;
        this.navigator = navigator;

        setTitle("Gestion Réparation - Connexion");
        setSize(520, 360);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        initEvents();
    }

    private void initUI() {
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(UiTheme.BG);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(UiTheme.CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        JLabel title = UiTheme.title("Connexion");
        JLabel sub = UiTheme.subtitle("Connectez-vous à votre tableau de bord");

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        card.add(title, c);

        c.gridy = 1;
        card.add(sub, c);

        c.gridwidth = 1;

        JLabel lblEmail = new JLabel("Email");
        JLabel lblPassword = new JLabel("Mot de passe");

        txtEmail = new JTextField();
        txtPassword = new JPasswordField();

        btnLogin = UiTheme.primaryButton("Se connecter");
        btnCodeClient = UiTheme.secondaryButton("Code client");

        lblStatus = new JLabel(" ");
        lblStatus.setForeground(Color.RED);

        // Email
        c.gridx = 0;
        c.gridy = 2;
        card.add(lblEmail, c);

        c.gridx = 1;
        card.add(txtEmail, c);

        // Password
        c.gridx = 0;
        c.gridy = 3;
        card.add(lblPassword, c);

        c.gridx = 1;
        card.add(txtPassword, c);

        // Login button
        c.gridx = 1;
        c.gridy = 4;
        card.add(btnLogin, c);

        // Code client button
        c.gridy = 5;
        card.add(btnCodeClient, c);

        // Status
        c.gridy = 6;
        card.add(lblStatus, c);

        root.add(card);
        setContentPane(root);
    }

    private void initEvents() {
        btnLogin.addActionListener(e -> doLogin());
        txtPassword.addActionListener(e -> doLogin());

        btnCodeClient.addActionListener(e -> doClientAccess());
    }

    private void doLogin() {
        lblStatus.setText(" ");

        String email = txtEmail.getText() == null ? "" : txtEmail.getText().trim();
        String mdp = new String(txtPassword.getPassword());

        if (email.isBlank() || mdp.isBlank()) {
            lblStatus.setText("Email et mot de passe obligatoires.");
            return;
        }

        try {
            User u = controller.login(email, mdp);
            dispose();

            if (u.getRole() == Role.PROPRIETAIRE) {
                navigator.showProprietaire(u);
            } else if (u.getRole() == Role.REPARATEUR) {
                navigator.showReparateur(u);
            } else {
                navigator.showLogin();
            }

        } catch (AuthException ex) {
            lblStatus.setText(ex.getMessage());
        } catch (Exception ex) {
            lblStatus.setText("Erreur technique. Voir console.");
            ex.printStackTrace();
        }
    }

    private void doClientAccess() {
        String code = JOptionPane.showInputDialog(
                this,
                "Entrez votre code de suivi",
                "Accès client",
                JOptionPane.PLAIN_MESSAGE
        );

        if (code == null || code.isBlank()) {
            return;
        }

        try {
            dispose();
            new FrameClientSuivi(code.trim(), navigator).setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

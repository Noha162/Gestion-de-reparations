package ma.gestionreparation.presentation.ui;

import ma.gestionreparation.presentation.controller.ClientSuiviController;
import ma.gestionreparation.metier.impl.ClientSuiviMetierImpl;
import ma.gestionreparation.presentation.ui.panels.ClientSuiviPanel;
import ma.gestionreparation.presentation.ui.util.AppNavigator;
import ma.gestionreparation.presentation.ui.util.UiTheme;

import javax.swing.*;
import java.awt.*;

public class FrameClientSuivi extends JFrame {

    public FrameClientSuivi(String code, AppNavigator navigator) {

        setTitle("Suivi réparation - Client");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ClientSuiviController controller =
                new ClientSuiviController(new ClientSuiviMetierImpl());

        JButton btnLogout = UiTheme.secondaryButton("Déconnexion");
        btnLogout.addActionListener(e -> {
            dispose();
            navigator.showLogin();
        });

        JPanel header = new JPanel(new BorderLayout());
        header.add(UiTheme.title("Suivi de réparation"), BorderLayout.WEST);
        header.add(btnLogout, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
        add(new ClientSuiviPanel(controller, code), BorderLayout.CENTER);
    }
}

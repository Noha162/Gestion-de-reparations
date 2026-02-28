package ma.gestionreparation.presentation.ui;

import ma.gestionreparation.dao.User;
import ma.gestionreparation.metier.impl.*;
import ma.gestionreparation.presentation.controller.*;
import ma.gestionreparation.presentation.ui.panels.*;
import ma.gestionreparation.presentation.ui.util.AppNavigator;
import ma.gestionreparation.presentation.ui.util.UiTheme;

import javax.swing.*;
import java.awt.*;

public class FrameReparateur extends JFrame {

    public FrameReparateur(User reparateur, AppNavigator appNavigator) {

        setTitle("Tableau de bord - Réparateur");
        setSize(1150, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        // Controllers
        ClientController clientController = new ClientController(new ClientMetierImpl());
        AppareilController appareilController = new AppareilController(new AppareilMetierImpl());
        ReparationController reparationController = new ReparationController(new ReparationMetierImpl());
        TransactionController transactionController = new TransactionController(new TransactionMetierImpl());

        // Panels
        ReparationPanel reparationPanel = new ReparationPanel(reparationController, reparateur);

        tabs.addTab("Clients", new ClientPanel(clientController));
        tabs.addTab("Appareils", new AppareilPanel(appareilController));
        tabs.addTab("Créer réparation", new CreateReparationPanel(reparationController, reparateur));
        tabs.addTab("Réparations", reparationPanel);
        tabs.addTab("Transactions", new TransactionPanel(transactionController, reparateur, reparationPanel));

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UiTheme.BG);
        root.add(tabs, BorderLayout.CENTER);

        setContentPane(root);
    }
}

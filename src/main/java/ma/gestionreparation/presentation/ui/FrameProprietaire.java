package ma.gestionreparation.presentation.ui;

import ma.gestionreparation.dao.User;
import ma.gestionreparation.metier.impl.ProprietaireMetierImpl;
import ma.gestionreparation.metier.interfaces.IProprietaireMetier;
import ma.gestionreparation.presentation.controller.ProprietaireController;
import ma.gestionreparation.presentation.ui.panels.BoutiquePanel;
import ma.gestionreparation.presentation.ui.panels.ReparateurPanel;

import ma.gestionreparation.presentation.ui.util.AppNavigator;
import ma.gestionreparation.presentation.ui.util.UiTheme;

import javax.swing.*;
import java.awt.*;
import ma.gestionreparation.presentation.ui.panels.StatsProprietairePanel;



public class FrameProprietaire extends JFrame {

    private final User proprietaire;
    private final AppNavigator navigator;

    public FrameProprietaire(User proprietaire, AppNavigator navigator) {
        this.proprietaire = proprietaire;
        this.navigator = navigator;

        setTitle("Tableau de bord - Propriétaire");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(buildUI());
    }

    private JPanel buildUI() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        root.setBackground(UiTheme.BG);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UiTheme.CARD);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JPanel left = new JPanel(new GridLayout(2,1));
        left.setOpaque(false);
        left.add(UiTheme.title("Dashboard Propriétaire"));
        left.add(UiTheme.subtitle("Bienvenue " + proprietaire.getPrenom() + " " + proprietaire.getNom()));

        JButton btnLogout = UiTheme.secondaryButton("Déconnexion");
        btnLogout.addActionListener(e -> {
            dispose();
            navigator.showLogin();
        });

        header.add(left, BorderLayout.WEST);
        header.add(btnLogout, BorderLayout.EAST);

        // Controller
        IProprietaireMetier metier = new ProprietaireMetierImpl();
        ProprietaireController controller = new ProprietaireController(metier);

        // Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Boutiques", new BoutiquePanel(controller, proprietaire));
        tabs.addTab("Réparateurs", new ReparateurPanel(controller, proprietaire));
        tabs.addTab("Statistiques", new StatsProprietairePanel(controller, proprietaire));


        root.add(header, BorderLayout.NORTH);
        root.add(tabs, BorderLayout.CENTER);

        return root;
    }
}

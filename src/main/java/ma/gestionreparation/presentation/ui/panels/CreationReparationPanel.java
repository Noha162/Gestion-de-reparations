package ma.gestionreparation.presentation.ui.panels;

import ma.gestionreparation.dao.User;
import ma.gestionreparation.presentation.controller.ClientController;
import ma.gestionreparation.presentation.controller.ReparationController;
import ma.gestionreparation.presentation.ui.util.UiTheme;

import javax.swing.*;
import java.awt.*;

public class CreationReparationPanel extends JPanel {

    private final ReparationController reparationController;
    private final ClientController clientController;
    private final User reparateur;

    public CreationReparationPanel(
            ReparationController reparationController,
            ClientController clientController,
            User reparateur
    ) {
        this.reparationController = reparationController;
        this.clientController = clientController;
        this.reparateur = reparateur;

        setLayout(new BorderLayout(12, 12));
        setBackground(UiTheme.BG);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        add(UiTheme.card(new JLabel(
                "Formulaire création réparation (déjà fonctionnel chez toi)"
        )), BorderLayout.CENTER);
    }
}

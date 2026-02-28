package ma.gestionreparation.presentation.ui.panels;

import ma.gestionreparation.dao.Reparation;
import ma.gestionreparation.dao.User;
import ma.gestionreparation.presentation.controller.ReparationController;
import ma.gestionreparation.presentation.ui.util.UiTheme;

import javax.swing.*;
import java.awt.*;

public class CreateReparationPanel extends JPanel {

    private final ReparationController controller;
    private final User reparateur;

    private JTextField txtNom, txtPrenom, txtTel;
    private JTextField txtType, txtMarque, txtModele, txtRam, txtStockage;
    private JTextArea txtDescription;
    private JTextField txtCout;
    private JLabel lblCode;

    public CreateReparationPanel(ReparationController controller, User reparateur) {
        this.controller = controller;
        this.reparateur = reparateur;

        setLayout(new BorderLayout(10,10));
        setBackground(UiTheme.BG);
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        add(buildForm(), BorderLayout.CENTER);
    }

    private JComponent buildForm() {
        JPanel panel = new JPanel(new GridLayout(0,2,8,8));

        txtNom = new JTextField();
        txtPrenom = new JTextField();
        txtTel = new JTextField();
        txtType = new JTextField();
        txtMarque = new JTextField();
        txtModele = new JTextField();
        txtRam = new JTextField();
        txtStockage = new JTextField();
        txtDescription = new JTextArea(3,20);
        txtCout = new JTextField();
        lblCode = new JLabel("Code : -");

        panel.add(new JLabel("Nom client")); panel.add(txtNom);
        panel.add(new JLabel("Prénom client")); panel.add(txtPrenom);
        panel.add(new JLabel("Téléphone")); panel.add(txtTel);
        panel.add(new JLabel("Type appareil")); panel.add(txtType);
        panel.add(new JLabel("Marque")); panel.add(txtMarque);
        panel.add(new JLabel("Modèle")); panel.add(txtModele);
        panel.add(new JLabel("RAM")); panel.add(txtRam);
        panel.add(new JLabel("Stockage")); panel.add(txtStockage);
        panel.add(new JLabel("Description panne")); panel.add(new JScrollPane(txtDescription));
        panel.add(new JLabel("Coût")); panel.add(txtCout);

        JButton btnCreate = UiTheme.primaryButton("Créer réparation");
        btnCreate.addActionListener(e -> creer());

        panel.add(btnCreate);
        panel.add(lblCode);

        return UiTheme.card(panel);
    }

    private void creer() {
        try {
            Reparation r = controller.creerReparationComplete(
                    reparateur.getId(),
                    reparateur.getBoutique().getId(),
                    txtNom.getText(),
                    txtPrenom.getText(),
                    txtTel.getText(),
                    txtType.getText(),
                    txtMarque.getText(),
                    txtModele.getText(),
                    txtRam.getText(),
                    txtStockage.getText(),
                    txtDescription.getText(),
                    Double.parseDouble(txtCout.getText()),
                    "Création"
            );

            lblCode.setText("Code : " + r.getCodeSuivi());
            JOptionPane.showMessageDialog(this, "Réparation créée");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}

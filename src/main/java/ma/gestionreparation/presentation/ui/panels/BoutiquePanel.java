package ma.gestionreparation.presentation.ui.panels;

import ma.gestionreparation.dao.Boutique;
import ma.gestionreparation.dao.User;
import ma.gestionreparation.exception.ValidationException;
import ma.gestionreparation.presentation.controller.ProprietaireController;
import ma.gestionreparation.presentation.ui.util.UiTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BoutiquePanel extends JPanel {

    private final ProprietaireController controller;
    private final User proprietaire;

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtNom;
    private JTextField txtAdresse;
    private JTextField txtPatente;

    private JLabel lblStatus;

    public BoutiquePanel(ProprietaireController controller, User proprietaire) {
        this.controller = controller;
        this.proprietaire = proprietaire;

        setLayout(new BorderLayout(12, 12));
        setBackground(UiTheme.BG);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);

        refreshTable();
    }

    private JComponent buildHeader() {
        JPanel header = new JPanel(new BorderLayout(8, 8));
        header.setBackground(UiTheme.CARD);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JPanel left = new JPanel(new GridLayout(2, 1));
        left.setOpaque(false);
        left.add(UiTheme.title("Gestion des boutiques"));
        left.add(UiTheme.subtitle("Lister, ajouter et supprimer les boutiques du propriétaire."));

        JButton btnRefresh = UiTheme.secondaryButton("Rafraîchir");
        btnRefresh.addActionListener(e -> refreshTable());

        header.add(left, BorderLayout.WEST);
        header.add(btnRefresh, BorderLayout.EAST);

        return header;
    }

    private JComponent buildCenter() {
        JPanel center = new JPanel(new GridLayout(1, 2, 12, 12));
        center.setOpaque(false);

        center.add(buildTableCard());
        center.add(buildFormCard());

        return center;
    }

    private JComponent buildTableCard() {
        model = new DefaultTableModel(new Object[]{"ID", "Nom", "Adresse", "Patente"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(24);

        JScrollPane sp = new JScrollPane(table);
        JPanel content = new JPanel(new BorderLayout(8, 8));
        content.setOpaque(false);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);

        JButton btnDelete = UiTheme.secondaryButton("Supprimer sélection");
        btnDelete.addActionListener(e -> deleteSelected());

        actions.add(btnDelete);

        content.add(actions, BorderLayout.NORTH);
        content.add(sp, BorderLayout.CENTER);

        return UiTheme.card(content);
    }

    private JComponent buildFormCard() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel t = UiTheme.title("Ajouter une boutique");
        JLabel s = UiTheme.subtitle("Saisir les champs puis cliquer sur Ajouter.");

        txtNom = new JTextField();
        txtAdresse = new JTextField();
        txtPatente = new JTextField();

        JButton btnAdd = UiTheme.primaryButton("Ajouter");
        btnAdd.addActionListener(e -> addBoutique());

        lblStatus = new JLabel(" ");
        lblStatus.setForeground(Color.RED);

        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        form.add(t, c);

        c.gridy = 1;
        form.add(s, c);

        c.gridwidth = 1;

        c.gridx = 0; c.gridy = 2;
        form.add(new JLabel("Nom"), c);
        c.gridx = 1;
        form.add(txtNom, c);

        c.gridx = 0; c.gridy = 3;
        form.add(new JLabel("Adresse"), c);
        c.gridx = 1;
        form.add(txtAdresse, c);

        c.gridx = 0; c.gridy = 4;
        form.add(new JLabel("Numéro patente"), c);
        c.gridx = 1;
        form.add(txtPatente, c);

        c.gridx = 1; c.gridy = 5;
        form.add(btnAdd, c);

        c.gridx = 1; c.gridy = 6;
        form.add(lblStatus, c);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(form, BorderLayout.NORTH);

        return UiTheme.card(wrapper);
    }

    private void refreshTable() {
        lblStatus.setText(" ");
        model.setRowCount(0);

        List<Boutique> boutiques = controller.listerBoutiques(proprietaire.getId());
        for (Boutique b : boutiques) {
            model.addRow(new Object[]{
                    b.getId(),
                    b.getNom(),
                    b.getAdresse(),
                    b.getNumeroPatente()
            });
        }
    }

    private void addBoutique() {
        lblStatus.setText(" ");

        String nom = txtNom.getText() == null ? "" : txtNom.getText().trim();
        String adresse = txtAdresse.getText() == null ? "" : txtAdresse.getText().trim();
        String patente = txtPatente.getText() == null ? "" : txtPatente.getText().trim();

        try {
            controller.ajouterBoutique(nom, adresse, patente, proprietaire);

            txtNom.setText("");
            txtAdresse.setText("");
            txtPatente.setText("");

            refreshTable();

        } catch (ValidationException ex) {
            lblStatus.setText(ex.getMessage());
        } catch (Exception ex) {
            lblStatus.setText("Erreur technique. Voir console.");
            ex.printStackTrace();
        }
    }

    private void deleteSelected() {
        lblStatus.setText(" ");

        int row = table.getSelectedRow();
        if (row < 0) {
            lblStatus.setText("Veuillez sélectionner une boutique.");
            return;
        }

        Long id = (Long) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Confirmer la suppression de la boutique ID=" + id + " ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            controller.supprimerBoutique(id);
            refreshTable();
        } catch (ValidationException ex) {
            lblStatus.setText(ex.getMessage());
        } catch (Exception ex) {
            lblStatus.setText("Erreur technique. Voir console.");
            ex.printStackTrace();
        }
    }
}

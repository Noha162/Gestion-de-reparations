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

public class ReparateurPanel extends JPanel {

    private final ProprietaireController controller;
    private final User proprietaire;

    private JComboBox<BoutiqueItem> cmbBoutiques;

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JTextField txtNom;
    private JTextField txtPrenom;
    private JTextField txtTelephone;
    private JTextField txtPourcentage;

    private JLabel lblStatus;

    public ReparateurPanel(ProprietaireController controller, User proprietaire) {
        this.controller = controller;
        this.proprietaire = proprietaire;

        setLayout(new BorderLayout(12, 12));
        setBackground(UiTheme.BG);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);

        loadBoutiques();
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
        left.add(UiTheme.title("Gestion des réparateurs"));
        left.add(UiTheme.subtitle("Lister, ajouter et supprimer les réparateurs par boutique."));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        cmbBoutiques = new JComboBox<>();
        cmbBoutiques.setPreferredSize(new Dimension(260, 28));
        cmbBoutiques.addActionListener(e -> refreshTable());

        JButton btnRefresh = UiTheme.secondaryButton("Rafraîchir");
        btnRefresh.addActionListener(e -> {
            loadBoutiques();
            refreshTable();
        });

        right.add(new JLabel("Boutique:"));
        right.add(cmbBoutiques);
        right.add(btnRefresh);

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
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
        model = new DefaultTableModel(new Object[]{"ID", "Email", "Nom", "Prénom", "Téléphone", "%"}, 0) {
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

        JLabel t = UiTheme.title("Ajouter un réparateur");
        JLabel s = UiTheme.subtitle("Rôle REPARATEUR, % et boutique obligatoires.");

        txtEmail = new JTextField();
        txtPassword = new JPasswordField();
        txtNom = new JTextField();
        txtPrenom = new JTextField();
        txtTelephone = new JTextField();
        txtPourcentage = new JTextField();

        JButton btnAdd = UiTheme.primaryButton("Ajouter");
        btnAdd.addActionListener(e -> addReparateur());

        lblStatus = new JLabel(" ");
        lblStatus.setForeground(Color.RED);

        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        form.add(t, c);

        c.gridy = 1;
        form.add(s, c);

        c.gridwidth = 1;

        c.gridx = 0; c.gridy = 2; form.add(new JLabel("Email"), c);
        c.gridx = 1; form.add(txtEmail, c);

        c.gridx = 0; c.gridy = 3; form.add(new JLabel("Mot de passe"), c);
        c.gridx = 1; form.add(txtPassword, c);

        c.gridx = 0; c.gridy = 4; form.add(new JLabel("Nom"), c);
        c.gridx = 1; form.add(txtNom, c);

        c.gridx = 0; c.gridy = 5; form.add(new JLabel("Prénom"), c);
        c.gridx = 1; form.add(txtPrenom, c);

        c.gridx = 0; c.gridy = 6; form.add(new JLabel("Téléphone"), c);
        c.gridx = 1; form.add(txtTelephone, c);

        c.gridx = 0; c.gridy = 7; form.add(new JLabel("Pourcentage"), c);
        c.gridx = 1; form.add(txtPourcentage, c);

        c.gridx = 1; c.gridy = 8;
        form.add(btnAdd, c);

        c.gridx = 1; c.gridy = 9;
        form.add(lblStatus, c);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(form, BorderLayout.NORTH);

        return UiTheme.card(wrapper);
    }

    private void loadBoutiques() {
        cmbBoutiques.removeAllItems();
        lblStatus.setText(" ");

        List<Boutique> boutiques = controller.listerBoutiques(proprietaire.getId());
        for (Boutique b : boutiques) {
            cmbBoutiques.addItem(new BoutiqueItem(b.getId(), b.getNom()));
        }
    }

    private Long getSelectedBoutiqueId() {
        BoutiqueItem item = (BoutiqueItem) cmbBoutiques.getSelectedItem();
        return item == null ? null : item.id();
    }

    private void refreshTable() {
        lblStatus.setText(" ");
        model.setRowCount(0);

        Long boutiqueId = getSelectedBoutiqueId();
        if (boutiqueId == null) return;

        List<User> reps = controller.listerReparateursParBoutique(boutiqueId);
        for (User u : reps) {
            model.addRow(new Object[]{
                    u.getId(),
                    u.getEmail(),
                    u.getNom(),
                    u.getPrenom(),
                    u.getTelephone(),
                    u.getPourcentage()
            });
        }
    }

    private void addReparateur() {
        lblStatus.setText(" ");

        Long boutiqueId = getSelectedBoutiqueId();
        if (boutiqueId == null) {
            lblStatus.setText("Veuillez sélectionner une boutique.");
            return;
        }

        try {
            String email = txtEmail.getText().trim();
            String mdp = new String(txtPassword.getPassword());
            String nom = txtNom.getText().trim();
            String prenom = txtPrenom.getText().trim();
            String tel = txtTelephone.getText().trim();

            Double pct;
            try {
                pct = Double.parseDouble(txtPourcentage.getText().trim());
            } catch (NumberFormatException e) {
                throw new ValidationException("Pourcentage invalide (ex: 30).");
            }

            controller.ajouterReparateur(email, mdp, nom, prenom, tel, pct, boutiqueId);

            txtEmail.setText("");
            txtPassword.setText("");
            txtNom.setText("");
            txtPrenom.setText("");
            txtTelephone.setText("");
            txtPourcentage.setText("");

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
            lblStatus.setText("Veuillez sélectionner un réparateur.");
            return;
        }

        Long id = (Long) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Confirmer la suppression du réparateur ID=" + id + " ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            controller.supprimerReparateur(id);
            refreshTable();
        } catch (ValidationException ex) {
            lblStatus.setText(ex.getMessage());
        } catch (Exception ex) {
            lblStatus.setText("Erreur technique. Voir console.");
            ex.printStackTrace();
        }
    }

    // Petit record interne pour afficher les boutiques dans le ComboBox
    private record BoutiqueItem(Long id, String nom) {
        @Override public String toString() { return nom; }
    }
}

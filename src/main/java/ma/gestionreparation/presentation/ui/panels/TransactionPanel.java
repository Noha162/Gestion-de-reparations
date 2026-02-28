package ma.gestionreparation.presentation.ui.panels;

import ma.gestionreparation.dao.Reparation;
import ma.gestionreparation.dao.Transaction;
import ma.gestionreparation.dao.User;
import ma.gestionreparation.dao.enums.TypeCaisse;
import ma.gestionreparation.dao.enums.TypeOperation;
import ma.gestionreparation.presentation.controller.TransactionController;
import ma.gestionreparation.presentation.ui.util.UiTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TransactionPanel extends JPanel {

    private final TransactionController controller;
    private final User reparateur;
    private final ReparationPanel reparationPanel;

    private JTable table;
    private DefaultTableModel model;
    private Transaction selected;

    private JTextField txtDesc, txtMontant;
    private JComboBox<TypeOperation> cbOperation;
    private JComboBox<TypeCaisse> cbCaisse;

    public TransactionPanel(
            TransactionController controller,
            User reparateur,
            ReparationPanel reparationPanel
    ) {
        this.controller = controller;
        this.reparateur = reparateur;
        this.reparationPanel = reparationPanel;

        setLayout(new BorderLayout(10, 10));
        setBackground(UiTheme.BG);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildForm(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);

        load();
    }

    private JComponent buildForm() {
        JPanel panel = new JPanel(new GridLayout(3, 4, 8, 8));

        txtDesc = new JTextField();
        txtMontant = new JTextField();
        cbOperation = new JComboBox<>(TypeOperation.values());
        cbCaisse = new JComboBox<>(TypeCaisse.values());

        panel.add(new JLabel("Description"));
        panel.add(txtDesc);
        panel.add(new JLabel("Montant"));
        panel.add(txtMontant);

        panel.add(new JLabel("Opération"));
        panel.add(cbOperation);
        panel.add(new JLabel("Caisse"));
        panel.add(cbCaisse);

        JButton btnAdd = UiTheme.primaryButton("Ajouter");
        JButton btnEdit = UiTheme.secondaryButton("Modifier");
        JButton btnDelete = UiTheme.dangerButton("Supprimer");
        JButton btnRefresh = UiTheme.secondaryButton("Actualiser");

        btnAdd.addActionListener(e -> ajouter());
        btnEdit.addActionListener(e -> modifier());
        btnDelete.addActionListener(e -> supprimer());
        btnRefresh.addActionListener(e -> load());

        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnDelete);
        panel.add(btnRefresh);

        return UiTheme.card(panel);
    }

    private JComponent buildTable() {
        model = new DefaultTableModel(
                new Object[]{"ID", "Description", "Montant", "Opération", "Caisse", "Réparation"},
                0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(24);

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                selected = (Transaction) model.getValueAt(row, 0);
                txtDesc.setText(selected.getDescription());
                txtMontant.setText(String.valueOf(selected.getMontant()));
                cbOperation.setSelectedItem(selected.getTypeOperation());
                cbCaisse.setSelectedItem(selected.getTypeCaisse());
            }
        });

        return UiTheme.card(new JScrollPane(table));
    }

    private void load() {
        model.setRowCount(0);
        List<Transaction> list = controller.listerPourReparateur(reparateur.getId());

        for (Transaction t : list) {
            model.addRow(new Object[]{
                    t,
                    t.getDescription(),
                    t.getMontant(),
                    t.getTypeOperation(),
                    t.getTypeCaisse(),
                    t.getReparation() != null ? t.getReparation().getCodeSuivi() : "-"
            });
        }
    }

    private void ajouter() {
        Reparation r = reparationPanel.getSelectedReparation();
        if (r == null) {
            JOptionPane.showMessageDialog(this, "Sélectionne une réparation");
            return;
        }

        controller.creer(
                txtDesc.getText(),
                Double.parseDouble(txtMontant.getText()),
                (TypeOperation) cbOperation.getSelectedItem(),
                (TypeCaisse) cbCaisse.getSelectedItem(),
                reparateur,
                r
        );
        load();
    }

    private void modifier() {
        if (selected == null) return;

        controller.modifier(
                selected.getId(),
                txtDesc.getText(),
                Double.parseDouble(txtMontant.getText()),
                (TypeOperation) cbOperation.getSelectedItem(),
                (TypeCaisse) cbCaisse.getSelectedItem()
        );
        load();
    }

    private void supprimer() {
        if (selected == null) return;
        controller.supprimer(selected.getId());
        load();
    }
}

package ma.gestionreparation.presentation.ui.panels;

import ma.gestionreparation.dao.Client;
import ma.gestionreparation.exception.ValidationException;
import ma.gestionreparation.presentation.controller.ClientController;
import ma.gestionreparation.presentation.ui.util.UiTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClientPanel extends JPanel {

    private final ClientController controller;

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtNom;
    private JTextField txtPrenom;
    private JTextField txtTelephone;

    private JLabel lblStatus;

    public ClientPanel(ClientController controller) {
        this.controller = controller;

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
        left.add(UiTheme.title("Clients"));
        left.add(UiTheme.subtitle("Lister, ajouter et supprimer des clients."));

        header.add(left, BorderLayout.WEST);

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
        model = new DefaultTableModel(new Object[]{"ID", "Nom", "Prénom", "Téléphone"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(24);

        JScrollPane sp = new JScrollPane(table);

        JPanel content = new JPanel(new BorderLayout(8, 8));
        content.setOpaque(false);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);

        JButton btnRefresh = UiTheme.secondaryButton("Rafraîchir");
        btnRefresh.addActionListener(e -> refreshTable());

        JButton btnDelete = UiTheme.secondaryButton("Supprimer sélection");
        btnDelete.addActionListener(e -> deleteSelected());

        actions.add(btnRefresh);
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

        JLabel t = UiTheme.title("Ajouter un client");
        JLabel s = UiTheme.subtitle("Nom, prénom et téléphone obligatoires.");

        txtNom = new JTextField();
        txtPrenom = new JTextField();
        txtTelephone = new JTextField();

        JButton btnAdd = UiTheme.primaryButton("Ajouter");
        btnAdd.addActionListener(e -> addClient());

        lblStatus = new JLabel(" ");
        lblStatus.setForeground(Color.RED);

        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        form.add(t, c);

        c.gridy = 1;
        form.add(s, c);

        c.gridwidth = 1;

        c.gridx = 0; c.gridy = 2; form.add(new JLabel("Nom"), c);
        c.gridx = 1; form.add(txtNom, c);

        c.gridx = 0; c.gridy = 3; form.add(new JLabel("Prénom"), c);
        c.gridx = 1; form.add(txtPrenom, c);

        c.gridx = 0; c.gridy = 4; form.add(new JLabel("Téléphone"), c);
        c.gridx = 1; form.add(txtTelephone, c);

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

        List<Client> clients = controller.lister();
        for (Client c : clients) {
            model.addRow(new Object[]{
                    c.getId(),
                    c.getNom(),
                    c.getPrenom(),
                    c.getTelephone()
            });
        }
    }

    private void addClient() {
        lblStatus.setText(" ");

        try {
            controller.ajouter(
                    txtNom.getText().trim(),
                    txtPrenom.getText().trim(),
                    txtTelephone.getText().trim()
            );

            txtNom.setText("");
            txtPrenom.setText("");
            txtTelephone.setText("");

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
            lblStatus.setText("Veuillez sélectionner un client.");
            return;
        }

        Long id = (Long) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Confirmer la suppression du client ID=" + id + " ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            controller.supprimer(id);
            refreshTable();
        } catch (ValidationException ex) {
            lblStatus.setText(ex.getMessage());
        } catch (Exception ex) {
            lblStatus.setText("Erreur technique. Voir console.");
            ex.printStackTrace();
        }
    }
}

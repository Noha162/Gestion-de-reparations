package ma.gestionreparation.presentation.ui.panels;

import ma.gestionreparation.dao.Appareil;
import ma.gestionreparation.presentation.controller.AppareilController;
import ma.gestionreparation.presentation.ui.util.UiTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AppareilPanel extends JPanel {

    private final AppareilController controller;
    private JTable table;
    private DefaultTableModel model;

    public AppareilPanel(AppareilController controller) {
        this.controller = controller;

        setLayout(new BorderLayout(10, 10));
        setBackground(UiTheme.BG);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildTable(), BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);

        load();
    }

    private JComponent buildTable() {
        model = new DefaultTableModel(
                new Object[]{"ID", "Type", "Marque", "Modèle", "Client"},
                0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(24);

        return UiTheme.card(new JScrollPane(table));
    }

    private JComponent buildButtons() {
        JPanel p = new JPanel(new GridLayout(1, 4, 8, 8));

        JButton btnAdd = UiTheme.primaryButton("Ajouter");
        JButton btnEdit = UiTheme.secondaryButton("Modifier");
        JButton btnDelete = UiTheme.dangerButton("Supprimer");
        JButton btnRefresh = UiTheme.secondaryButton("Actualiser");

        btnRefresh.addActionListener(e -> load());

        p.add(btnAdd);
        p.add(btnEdit);
        p.add(btnDelete);
        p.add(btnRefresh);

        return UiTheme.card(p);
    }

    private void load() {
        model.setRowCount(0);
        List<Appareil> list = controller.lister();

        for (Appareil a : list) {
            model.addRow(new Object[]{
                    a,
                    a.getType(),
                    a.getMarque(),
                    a.getModele(),
                    a.getClient().getNom() + " " + a.getClient().getPrenom()
            });
        }
    }
}

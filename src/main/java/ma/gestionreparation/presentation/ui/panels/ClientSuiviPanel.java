package ma.gestionreparation.presentation.ui.panels;

import ma.gestionreparation.dao.LigneReparation;
import ma.gestionreparation.dao.Reparation;
import ma.gestionreparation.dao.Transaction;
import ma.gestionreparation.presentation.controller.ClientSuiviController;
import ma.gestionreparation.presentation.ui.util.UiTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClientSuiviPanel extends JPanel {

    private final ClientSuiviController controller;

    public ClientSuiviPanel(ClientSuiviController controller, String code) {
        this.controller = controller;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(UiTheme.BG);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        Reparation r = controller.charger(code);

        add(buildHeader(r));
        add(Box.createVerticalStrut(12));
        add(buildAppareils(r));
        add(Box.createVerticalStrut(12));
        add(buildTransactions(r));
    }

    /* ================= HEADER ================= */
    private JComponent buildHeader(Reparation r) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(243, 244, 246));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        panel.add(UiTheme.title("Suivi de réparation"));
        panel.add(Box.createVerticalStrut(6));
        panel.add(new JLabel("Code : " + r.getCodeSuivi()));
        panel.add(new JLabel("Statut : " + r.getStatut()));
        panel.add(new JLabel("Montant total : " + r.getCoutTotal() + " DH"));

        return panel;
    }

    /* ================= APPAREILS ================= */
    private JComponent buildAppareils(Reparation r) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        panel.add(UiTheme.subtitle("Appareils"), BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Appareil", "État", "Coût"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        for (LigneReparation lr : r.getLignes()) {
            model.addRow(new Object[]{
                    lr.getAppareil().getMarque() + " " + lr.getAppareil().getModele(),
                    lr.getEtatAppareil(),
                    lr.getCoutAppareil()
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(26);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    /* ================= TRANSACTIONS ================= */
    private JComponent buildTransactions(Reparation r) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        panel.add(UiTheme.subtitle("Transactions"), BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Description", "Montant", "Type"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        for (Transaction t : r.getTransactions()) {
            model.addRow(new Object[]{
                    t.getDescription(),
                    t.getMontant(),
                    t.getTypeOperation()
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(26);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }
}

package ma.gestionreparation.presentation.ui.panels;

import ma.gestionreparation.dao.Reparation;
import ma.gestionreparation.dao.User;
import ma.gestionreparation.presentation.controller.ReparationController;
import ma.gestionreparation.presentation.ui.util.UiTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReparationPanel extends JPanel {

    private final ReparationController reparationController;
    private final User reparateur;

    private JTable table;
    private DefaultTableModel model;

    private List<Reparation> reparations = new ArrayList<>();
    private Reparation selected;

    private JLabel lblInfo;

    public ReparationPanel(
            ReparationController reparationController,
            User reparateur
    ) {
        this.reparationController = reparationController;
        this.reparateur = reparateur;

        setLayout(new BorderLayout(12, 12));
        setBackground(UiTheme.BG);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);

        loadReparations();
    }

    /* ================= HEADER ================= */
    private JComponent buildHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel left = new JPanel(new GridLayout(2, 1));
        left.setOpaque(false);
        left.add(UiTheme.title("Réparations"));
        left.add(UiTheme.subtitle("Mes réparations"));

        lblInfo = new JLabel("Aucune sélection");
        lblInfo.setFont(lblInfo.getFont().deriveFont(Font.BOLD));

        panel.add(left, BorderLayout.WEST);
        panel.add(lblInfo, BorderLayout.EAST);

        return UiTheme.card(panel);
    }

    /* ================= TABLE ================= */
    private JComponent buildTable() {

        model = new DefaultTableModel(
                new Object[]{"ID", "Client", "Statut", "Date", "Code"},
                0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0 && row < reparations.size()) {
                selected = reparations.get(row);
                lblInfo.setText("Code : " + selected.getCodeSuivi());
            }
        });

        return UiTheme.card(new JScrollPane(table));
    }

    /* ================= DATA ================= */
    private void loadReparations() {
        model.setRowCount(0);
        reparations.clear();
        selected = null;
        lblInfo.setText("Aucune sélection");

        reparations =
                reparationController.listerPourReparateur(reparateur.getId());

        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Reparation r : reparations) {
        	model.addRow(new Object[]{
        		    r.getId(), // ✅ PAS r
        		    r.getClient().getNom() + " " + r.getClient().getPrenom(),
        		    r.getStatut(),
        		    r.getDateCreation().format(f),
        		    r.getCodeSuivi()
        		});

        }
    }

    /* ================= UTIL ================= */
    public Reparation getSelectedReparation() {
        return selected;
    }
}

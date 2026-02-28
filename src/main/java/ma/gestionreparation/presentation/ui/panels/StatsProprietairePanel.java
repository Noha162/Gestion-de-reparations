package ma.gestionreparation.presentation.ui.panels;

import ma.gestionreparation.dao.User;
import ma.gestionreparation.exception.ValidationException;
import ma.gestionreparation.metier.dto.StatsProprietaireDTO;
import ma.gestionreparation.metier.dto.TopReparateurDTO;
import ma.gestionreparation.presentation.controller.ProprietaireController;
import ma.gestionreparation.presentation.ui.util.UiTheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StatsProprietairePanel extends JPanel {

    private final ProprietaireController controller;
    private final User proprietaire;

    private JTextField txtDebut; // format YYYY-MM-DD
    private JTextField txtFin;

    private JLabel lblNbBoutiques;
    private JLabel lblNbReparateurs;
    private JLabel lblNbReparations;
    private JLabel lblCA;
    private JLabel lblProfit;

    private JLabel lblStatus;

    // BONUS
    private JTable topTable;
    private DefaultTableModel topModel;
    private BarChartPanel chartPanel;

    // état courant (pour refresh après reset, etc.)
    private LocalDate currentD1 = null;
    private LocalDate currentD2 = null;

    public StatsProprietairePanel(ProprietaireController controller, User proprietaire) {
        this.controller = controller;
        this.proprietaire = proprietaire;

        setLayout(new BorderLayout(12, 12));
        setBackground(UiTheme.BG);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);

        refresh(null, null); // stats globales par défaut
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
        left.add(UiTheme.title("Statistiques"));
        left.add(UiTheme.subtitle("Synthèse propriétaire (filtrable par période)."));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        txtDebut = new JTextField(10);
        txtFin = new JTextField(10);

        JButton btnAppliquer = UiTheme.primaryButton("Appliquer");
        btnAppliquer.addActionListener(e -> applyDates());

        JButton btnReset = UiTheme.secondaryButton("Reset");
        btnReset.addActionListener(e -> {
            txtDebut.setText("");
            txtFin.setText("");
            refresh(null, null);
        });

        lblStatus = new JLabel(" ");
        lblStatus.setForeground(Color.RED);

        right.add(new JLabel("Début (YYYY-MM-DD):"));
        right.add(txtDebut);
        right.add(new JLabel("Fin (YYYY-MM-DD):"));
        right.add(txtFin);
        right.add(btnAppliquer);
        right.add(btnReset);

        header.add(left, BorderLayout.WEST);

        JPanel box = new JPanel(new BorderLayout());
        box.setOpaque(false);
        box.add(right, BorderLayout.CENTER);
        box.add(lblStatus, BorderLayout.SOUTH);

        header.add(box, BorderLayout.EAST);

        return header;
    }

    private JComponent buildContent() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setOpaque(false);

        // KPI en haut
        root.add(buildKpiGrid(), BorderLayout.NORTH);

        // Bonus en bas : table + chart
        root.add(buildBonusGrid(), BorderLayout.CENTER);

        return root;
    }

    private JComponent buildKpiGrid() {
        JPanel grid = new JPanel(new GridLayout(2, 3, 12, 12));
        grid.setOpaque(false);

        lblNbBoutiques = new JLabel("-");
        lblNbReparateurs = new JLabel("-");
        lblNbReparations = new JLabel("-");
        lblCA = new JLabel("-");
        lblProfit = new JLabel("-");

        grid.add(kpiCard("Boutiques", lblNbBoutiques, new Color(232, 240, 254)));   // bleu pastel
        grid.add(kpiCard("Réparateurs", lblNbReparateurs, new Color(232, 245, 233))); // vert pastel
        grid.add(kpiCard("Réparations", lblNbReparations, new Color(243, 229, 245))); // violet pastel
        grid.add(kpiCard("Chiffre d'affaires", lblCA, new Color(255, 249, 196)));     // jaune pastel
        grid.add(kpiCard("Profit propriétaire", lblProfit, new Color(252, 228, 236))); // rose pastel
        grid.add(new JPanel()); // cellule vide


        return grid;
    }

    private JComponent buildBonusGrid() {
        JPanel grid = new JPanel(new GridLayout(1, 2, 12, 12));
        grid.setOpaque(false);

        // Table Top 5
        topModel = new DefaultTableModel(new Object[]{"Nom", "Prénom", "CA"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        topTable = new JTable(topModel);
        topTable.setRowHeight(24);

        JPanel tableContent = new JPanel(new BorderLayout(8, 8));
        tableContent.setOpaque(false);
        JLabel t1 = UiTheme.title("Top réparateurs");
        JLabel s1 = UiTheme.subtitle("Top 5 par chiffre d'affaires (période sélectionnée).");
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);
        header.add(t1);
        header.add(s1);

        tableContent.add(header, BorderLayout.NORTH);
        tableContent.add(new JScrollPane(topTable), BorderLayout.CENTER);

        // Graphique
        chartPanel = new BarChartPanel();
        JPanel chartContent = new JPanel(new BorderLayout(8, 8));
        chartContent.setOpaque(false);

        JLabel t2 = UiTheme.title("Mini graphique");
        JLabel s2 = UiTheme.subtitle("Répartition CA par réparateur (Top 5).");
        JPanel header2 = new JPanel(new GridLayout(2, 1));
        header2.setOpaque(false);
        header2.add(t2);
        header2.add(s2);

        chartContent.add(header2, BorderLayout.NORTH);
        chartContent.add(chartPanel, BorderLayout.CENTER);

        grid.add(UiTheme.card(tableContent));
        grid.add(UiTheme.card(chartContent));

        return grid;
    }

    private JPanel kpiCard(String title, JLabel value, Color bg) {
        value.setFont(value.getFont().deriveFont(Font.BOLD, 24f));
        value.setForeground(Color.BLACK);

        JLabel t = new JLabel(title);
        t.setForeground(new Color(80, 80, 80));

        JPanel content = new JPanel(new GridLayout(2, 1, 4, 4));
        content.setOpaque(false);
        content.add(t);
        content.add(value);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bg);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)
        ));
        card.add(content, BorderLayout.CENTER);

        return card;
    }


    private void applyDates() {
        lblStatus.setText(" ");

        try {
            LocalDate d1 = parseDate(txtDebut.getText().trim());
            LocalDate d2 = parseDate(txtFin.getText().trim());

            // Optionnel : si les deux dates sont présentes, vérifier ordre
            if (d1 != null && d2 != null && d2.isBefore(d1)) {
                throw new ValidationException("La date de fin doit être >= date début.");
            }

            refresh(d1, d2);
        } catch (ValidationException ex) {
            lblStatus.setText(ex.getMessage());
        }
    }

    private LocalDate parseDate(String s) {
        if (s == null || s.isBlank()) return null;
        try {
            return LocalDate.parse(s);
        } catch (Exception e) {
            throw new ValidationException("Format date invalide (YYYY-MM-DD).");
        }
    }

    private void refresh(LocalDate d1, LocalDate d2) {
        lblStatus.setText(" ");
        currentD1 = d1;
        currentD2 = d2;

        // KPI
        StatsProprietaireDTO dto = controller.stats(proprietaire.getId(), d1, d2);
        lblNbBoutiques.setText(String.valueOf(dto.getNbBoutiques()));
        lblNbReparateurs.setText(String.valueOf(dto.getNbReparateurs()));
        lblNbReparations.setText(String.valueOf(dto.getNbReparations()));
        lblCA.setText(String.format("%.2f", dto.getChiffreAffaires()));
        lblProfit.setText(String.format("%.2f", dto.getProfitProprietaire()));

        // BONUS : Top réparateurs + graphique
        refreshTopAndChart(d1, d2);
    }

    private void refreshTopAndChart(LocalDate d1, LocalDate d2) {
        topModel.setRowCount(0);

        List<TopReparateurDTO> tops;
        try {
            tops = controller.topReparateurs(proprietaire.getId(), d1, d2);
        } catch (Exception e) {
            // ne bloque pas la page stats si le bonus a un souci
            tops = new ArrayList<>();
        }

        for (TopReparateurDTO t : tops) {
            topModel.addRow(new Object[]{
                    t.getNom(),
                    t.getPrenom(),
                    String.format("%.2f", t.getChiffreAffaires())
            });
        }

        // envoyer données au chart
        chartPanel.setData(tops);
    }

    /**
     * Mini bar chart Swing (pas de lib externe)
     */
    private static class BarChartPanel extends JPanel {

        private List<TopReparateurDTO> data = new ArrayList<>();

        public BarChartPanel() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(400, 260));
            setBorder(BorderFactory.createLineBorder(UiTheme.BORDER));
        }

        public void setData(List<TopReparateurDTO> data) {
            this.data = (data == null) ? new ArrayList<>() : new ArrayList<>(data);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // marges
                int left = 40;
                int right = 20;
                int top = 20;
                int bottom = 40;

                // zone de dessin
                int chartW = w - left - right;
                int chartH = h - top - bottom;

                // axes
                g2.setColor(UiTheme.BORDER);
                g2.drawLine(left, top, left, top + chartH);
                g2.drawLine(left, top + chartH, left + chartW, top + chartH);

                if (data == null || data.isEmpty()) {
                    g2.setColor(UiTheme.MUTED);
                    g2.drawString("Aucune donnée (Top réparateurs vide)", left + 10, top + 20);
                    return;
                }

                double max = 0;
                for (TopReparateurDTO d : data) {
                    if (d.getChiffreAffaires() > max) max = d.getChiffreAffaires();
                }
                if (max <= 0) max = 1;

                int n = data.size();
                int gap = 10;
                int barW = (chartW - (gap * (n + 1))) / n;
                if (barW < 10) barW = 10;

                for (int i = 0; i < n; i++) {
                    TopReparateurDTO d = data.get(i);
                    double val = d.getChiffreAffaires();

                    int barH = (int) Math.round((val / max) * (chartH - 10));
                    int x = left + gap + i * (barW + gap);
                    int y = top + chartH - barH;

                    // barre (même couleur que primaire)
                    g2.setColor(new Color(31, 79, 216)); // cohérent avec le thème
                    g2.fillRect(x, y, barW, barH);

                    // valeur au-dessus
                    g2.setColor(UiTheme.TEXT);
                    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 11f));
                    String sVal = String.format("%.0f", val);
                    g2.drawString(sVal, x, y - 4);

                    // label nom (première lettre nom/prénom)
                    String label = shortLabel(d.getNom(), d.getPrenom());
                    g2.setColor(UiTheme.MUTED);
                    g2.drawString(label, x, top + chartH + 16);
                }

            } finally {
                g2.dispose();
            }
        }

        private static String shortLabel(String nom, String prenom) {
            String n = (nom == null || nom.isBlank()) ? "?" : nom.trim();
            String p = (prenom == null || prenom.isBlank()) ? "" : prenom.trim();
            String a = n.substring(0, 1).toUpperCase();
            String b = p.isEmpty() ? "" : p.substring(0, 1).toUpperCase();
            return prenom != null && !prenom.isBlank() ? prenom : "?";

        }
    }
}

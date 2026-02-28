package ma.gestionreparation.presentation.ui.util;

import javax.swing.*;
import java.awt.*;

public class UiTheme {

    public static final Color BG = new Color(245, 247, 250);
    public static final Color CARD = Color.WHITE;
    public static final Color BORDER = new Color(220, 220, 220);
	public static final Color MUTED = null;
	public static final Color TEXT = null;

    /* ================= TITRES ================= */

    public static JLabel title(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 22));
        return l;
    }

    public static JLabel subtitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        l.setForeground(Color.GRAY);
        return l;
    }

    /* ================= CARTES ================= */

    public static JPanel card(JComponent content) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        p.add(content, BorderLayout.CENTER);
        return p;
    }

    /* ================= BOUTONS ================= */

    public static JButton primaryButton(String text) {
        JButton b = new JButton(text);
        styleButton(b, new Color(33, 150, 243), Color.WHITE);
        return b;
    }

    public static JButton secondaryButton(String text) {
        JButton b = new JButton(text);
        styleButton(b, new Color(240, 240, 240), Color.BLACK);
        return b;
    }

    public static JButton dangerButton(String text) {
        JButton b = new JButton(text);
        styleButton(b, new Color(220, 53, 69), Color.WHITE);
        return b;
    }

    private static void styleButton(JButton b, Color bg, Color fg) {
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // ✅ TAILLE UNIQUE POUR TOUS LES BOUTONS
        Dimension size = new Dimension(140, 42);
        b.setPreferredSize(size);
        b.setMinimumSize(size);
        b.setMaximumSize(size);

        b.setBorder(BorderFactory.createLineBorder(bg.darker(), 1));
    }

	public static void applyBaseLookAndFeel() {
		// TODO Auto-generated method stub
		
	}
}

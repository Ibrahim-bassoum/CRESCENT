package util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Classe utilitaire centralisant tous les styles de l'UI.
 * Thème : Luxury Dark (fond sombre, accents or).
 */
public class Theme {

    // ── Palette ──────────────────────────────────────────────
    public static final Color BG_DARK      = new Color(18, 18, 24);
    public static final Color BG_CARD      = new Color(28, 28, 38);
    public static final Color BG_INPUT     = new Color(38, 38, 52);
    public static final Color ACCENT_GOLD  = new Color(212, 175, 55);
    public static final Color ACCENT_LIGHT = new Color(255, 215, 100);
    public static final Color TEXT_PRIMARY = new Color(240, 235, 220);
    public static final Color TEXT_MUTED   = new Color(140, 135, 120);
    public static final Color SUCCESS      = new Color(72, 199, 142);
    public static final Color DANGER       = new Color(220, 80, 80);
    public static final Color TABLE_ROW_ODD  = new Color(32, 32, 44);
    public static final Color TABLE_ROW_EVEN = new Color(26, 26, 36);
    public static final Color TABLE_SELECT   = new Color(80, 65, 20);

    // ── Fonts ─────────────────────────────────────────────────
    public static final Font FONT_TITLE   = new Font("Georgia", Font.BOLD, 26);
    public static final Font FONT_SECTION = new Font("Georgia", Font.BOLD, 16);
    public static final Font FONT_LABEL   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD    = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_INPUT   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BUTTON  = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_TABLE   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_HEADER  = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);

    // ── Buttons ───────────────────────────────────────────────

    public static JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setBackground(ACCENT_GOLD);
        btn.setForeground(new Color(20, 18, 10));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 22, 10, 22));
        btn.setOpaque(true);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(ACCENT_LIGHT);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(ACCENT_GOLD);
            }
        });
        return btn;
    }

    public static JButton dangerButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setBackground(DANGER);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 22, 10, 22));
        btn.setOpaque(true);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(240, 100, 100));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(DANGER);
            }
        });
        return btn;
    }

    public static JButton ghostButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setBackground(BG_INPUT);
        btn.setForeground(TEXT_PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 90), 1),
                new EmptyBorder(9, 20, 9, 20)
        ));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    // ── Text fields ───────────────────────────────────────────

    public static JTextField styledTextField() {
        JTextField tf = new JTextField();
        tf.setFont(FONT_INPUT);
        tf.setBackground(BG_INPUT);
        tf.setForeground(TEXT_PRIMARY);
        tf.setCaretColor(ACCENT_GOLD);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 80), 1),
                new EmptyBorder(8, 12, 8, 12)
        ));
        return tf;
    }

    public static JPasswordField styledPasswordField() {
        JPasswordField pf = new JPasswordField();
        pf.setFont(FONT_INPUT);
        pf.setBackground(BG_INPUT);
        pf.setForeground(TEXT_PRIMARY);
        pf.setCaretColor(ACCENT_GOLD);
        pf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 80), 1),
                new EmptyBorder(8, 12, 8, 12)
        ));
        return pf;
    }

    public static <T> JComboBox<T> styledComboBox() {
        JComboBox<T> cb = new JComboBox<>();
        cb.setFont(FONT_INPUT);
        cb.setBackground(BG_INPUT);
        cb.setForeground(TEXT_PRIMARY);
        cb.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 80), 1));
        return cb;
    }

    // ── Labels ────────────────────────────────────────────────

    public static JLabel titleLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_TITLE);
        lbl.setForeground(ACCENT_GOLD);
        return lbl;
    }

    public static JLabel sectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_SECTION);
        lbl.setForeground(TEXT_PRIMARY);
        return lbl;
    }

    public static JLabel label(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(TEXT_MUTED);
        return lbl;
    }

    // ── Table ─────────────────────────────────────────────────

    public static void styleTable(JTable table) {
        table.setFont(FONT_TABLE);
        table.setBackground(TABLE_ROW_ODD);
        table.setForeground(TEXT_PRIMARY);
        table.setRowHeight(36);
        table.setGridColor(new Color(45, 45, 60));
        table.setSelectionBackground(TABLE_SELECT);
        table.setSelectionForeground(ACCENT_GOLD);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setFillsViewportHeight(true);

        // Header
        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_HEADER);
        header.setBackground(new Color(38, 35, 20));
        header.setForeground(ACCENT_GOLD);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_GOLD));
        header.setReorderingAllowed(false);

        // Alternating rows
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? TABLE_ROW_EVEN : TABLE_ROW_ODD);
                    c.setForeground(TEXT_PRIMARY);
                } else {
                    c.setBackground(TABLE_SELECT);
                    c.setForeground(ACCENT_GOLD);
                }
                setBorder(new EmptyBorder(0, 12, 0, 12));
                return c;
            }
        });
    }

    public static JScrollPane styledScrollPane(Component view) {
        JScrollPane sp = new JScrollPane(view);
        sp.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 70), 1));
        sp.getViewport().setBackground(TABLE_ROW_ODD);
        sp.setBackground(BG_CARD);
        return sp;
    }

    // ── Panels ────────────────────────────────────────────────

    public static JPanel cardPanel() {
        JPanel p = new JPanel();
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 50, 70), 1),
                new EmptyBorder(20, 24, 20, 24)
        ));
        return p;
    }

    public static JPanel darkPanel() {
        JPanel p = new JPanel();
        p.setBackground(BG_DARK);
        return p;
    }

    // ── Global setup ──────────────────────────────────────────

    public static void applyGlobalDefaults() {
        UIManager.put("OptionPane.background", BG_CARD);
        UIManager.put("Panel.background", BG_DARK);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        UIManager.put("Button.background", ACCENT_GOLD);
        UIManager.put("Button.foreground", Color.BLACK);
    }
}

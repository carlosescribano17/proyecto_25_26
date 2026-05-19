package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.plaf.basic.BasicArrowButton;

public final class DarkThemeUtil {

    private static final Color BG_APP = new Color(15, 23, 42);
    private static final Color BG_PANEL = new Color(30, 41, 59);
    private static final Color BG_PANEL_ALT = new Color(51, 65, 85);
    private static final Color BG_INPUT = new Color(22, 32, 52);
    private static final Color FG_MAIN = new Color(226, 232, 240);
    private static final Color FG_MUTED = new Color(148, 163, 184);
    private static final Color ACCENT = new Color(59, 130, 246);
    private static final Color ACCENT_OK = new Color(16, 185, 129);
    private static final Color BORDER = new Color(71, 85, 105);

    private DarkThemeUtil() {
    }

    public static void apply(JFrame frame) {
        frame.getContentPane().setBackground(BG_APP);
        styleTree(frame.getContentPane());
    }

    public static void apply(JDialog dialog) {
        dialog.getContentPane().setBackground(BG_APP);
        styleTree(dialog.getContentPane());
    }

    public static void apply(JPanel panel) {
        panel.setBackground(BG_APP);
        styleTree(panel);
    }

    private static void styleTree(Component c) {
        styleSingle(c);
        if (c instanceof Container) {
            Container container = (Container) c;
            for (Component child : container.getComponents()) {
                styleTree(child);
            }
        }
    }

    private static void styleSingle(Component c) {
        if (c instanceof JPanel) {
            JPanel p = (JPanel) c;
            p.setBackground(BG_PANEL);
        } else if (c instanceof JButton) {
            JButton b = (JButton) c;
            b.setBackground(ACCENT);
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.setBorderPainted(false);
            b.setOpaque(true);
            b.setFont(new Font("Segoe UI", Font.BOLD, 13));
            b.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
            String t = b.getText() == null ? "" : b.getText().toUpperCase();
            if (t.contains("BORRAR") || t.contains("ELIMINAR") || t.contains("SALIR")) {
                b.setBackground(new Color(220, 38, 38));
            } else if (t.contains("PAGAR") || t.contains("CONFIRMAR") || t.contains("ENVIAR")) {
                b.setBackground(ACCENT_OK);
            } else if (t.contains("VOLVER")) {
                b.setBackground(BG_PANEL_ALT);
            }
        } else if (c instanceof JLabel) {
            JLabel l = (JLabel) c;
            l.setForeground(FG_MAIN);
            Font f = l.getFont();
            if (f != null && f.getSize() >= 16) {
                l.setFont(new Font("Segoe UI", Font.BOLD, f.getSize()));
            }
        } else if (c instanceof JTextField) {
            JTextField t = (JTextField) c;
            styleInput(t);
        } else if (c instanceof JPasswordField) {
            JPasswordField p = (JPasswordField) c;
            styleInput(p);
        } else if (c instanceof JTextArea) {
            JTextArea a = (JTextArea) c;
            a.setBackground(BG_INPUT);
            a.setForeground(FG_MAIN);
            a.setCaretColor(FG_MAIN);
            a.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER, 1, true),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
        } else if (c instanceof JComboBox) {
            JComboBox<?> combo = (JComboBox<?>) c;
            combo.setBackground(BG_INPUT);
            combo.setForeground(FG_MAIN);
            combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            combo.setBorder(BorderFactory.createLineBorder(BORDER, 1, true));
            styleArrowButtons(combo);
        } else if (c instanceof JSpinner) {
            JSpinner s = (JSpinner) c;
            s.setBackground(BG_INPUT);
            s.setForeground(FG_MAIN);
            s.setBorder(BorderFactory.createLineBorder(BORDER, 1, true));
            styleArrowButtons(s);
        } else if (c instanceof JTable) {
            JTable table = (JTable) c;
            table.setBackground(BG_INPUT);
            table.setForeground(FG_MAIN);
            table.setSelectionBackground(new Color(30, 58, 138));
            table.setSelectionForeground(Color.WHITE);
            table.setGridColor(BORDER);
            table.setRowHeight(26);
            table.getTableHeader().setBackground(BG_PANEL_ALT);
            table.getTableHeader().setForeground(FG_MAIN);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        } else if (c instanceof JScrollPane) {
            JScrollPane sp = (JScrollPane) c;
            sp.setBorder(BorderFactory.createEmptyBorder());
            sp.getViewport().setBackground(BG_INPUT);
        } else if (c instanceof JViewport) {
            JViewport v = (JViewport) c;
            v.setBackground(BG_INPUT);
        } else if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            jc.setForeground(FG_MUTED);
        }
    }

    private static void styleInput(JComponent c) {
        c.setBackground(BG_INPUT);
        c.setForeground(FG_MAIN);
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        if (c instanceof JTextField) {
            JTextField tf = (JTextField) c;
            tf.setCaretColor(FG_MAIN);
        }
    }

    private static void styleArrowButtons(Container container) {
        for (Component child : container.getComponents()) {
            if (child instanceof BasicArrowButton) {
                BasicArrowButton arrow = (BasicArrowButton) child;
                arrow.setBackground(BG_INPUT);
                arrow.setForeground(FG_MAIN);
                arrow.setBorder(BorderFactory.createEmptyBorder());
                arrow.setOpaque(true);
            } else if (child instanceof AbstractButton) {
                AbstractButton b = (AbstractButton) child;
                b.setBackground(BG_INPUT);
                b.setForeground(FG_MAIN);
                b.setBorder(BorderFactory.createEmptyBorder());
                b.setMargin(new Insets(0, 0, 0, 0));
                b.setFocusable(false);
                b.setOpaque(true);
            }
        }
    }

}

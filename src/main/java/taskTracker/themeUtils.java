package taskTracker;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Manages the visual theme for the Task Tracker application.
 * Provides dark theme colors and applies styling to UI components.
 */
public class themeUtils {

    // Dark theme color palette
    public static final Color BG_DARK = new Color(45, 45, 45);
    public static final Color BG_LIGHTER = new Color(60, 60, 60);
    public static final Color BG_DARKER = new Color(35, 35, 35);
    public static final Color FG_LIGHT = new Color(230, 230, 230);
    public static final Color FG_DIMMED = new Color(200, 200, 200);
    public static final Color ACCENT = new Color(75, 110, 175);
    public static final Color GRID = new Color(70, 70, 70);
    public static final Color SELECTION_BG = new Color(75, 110, 175);
    public static final Color SELECTION_FG = Color.WHITE;

    /**
     * Applies dark theme to the entire application using UIManager.
     * Should be called before creating any UI components.
     */
    public static void applyGlobalTheme() {
        try {
            // Set Nimbus Look and Feel
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }

            // Customize Nimbus colors for dark theme
            UIManager.put("control", BG_DARK);
            UIManager.put("info", BG_DARK);
            UIManager.put("nimbusBase", new Color(18, 30, 49));
            UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
            UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
            UIManager.put("nimbusFocus", new Color(115, 164, 209));
            UIManager.put("nimbusGreen", new Color(176, 179, 50));
            UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
            UIManager.put("nimbusLightBackground", BG_LIGHTER);
            UIManager.put("nimbusOrange", new Color(191, 98, 4));
            UIManager.put("nimbusRed", new Color(169, 46, 34));
            UIManager.put("nimbusSelectedText", SELECTION_FG);
            UIManager.put("nimbusSelectionBackground", SELECTION_BG);
            UIManager.put("text", FG_LIGHT);

            // Table customizations
            UIManager.put("Table.background", BG_LIGHTER);
            UIManager.put("Table.foreground", FG_LIGHT);
            UIManager.put("Table.gridColor", GRID);
            UIManager.put("Table.selectionBackground", SELECTION_BG);
            UIManager.put("Table.selectionForeground", SELECTION_FG);
            UIManager.put("TableHeader.background", BG_DARKER);
            UIManager.put("TableHeader.foreground", FG_LIGHT);

            // Panel and other components
            UIManager.put("Panel.background", BG_DARK);
            UIManager.put("TextField.background", BG_LIGHTER);
            UIManager.put("TextField.foreground", FG_LIGHT);
            UIManager.put("TextField.inactiveBackground", BG_LIGHTER);
            UIManager.put("TextField.inactiveForeground", FG_DIMMED);
            UIManager.put("TextField.caretForeground", FG_LIGHT);
            UIManager.put("Label.foreground", FG_LIGHT);
            UIManager.put("ScrollPane.background", BG_DARK);

        } catch (Exception e) {
            System.err.println("Failed to set dark theme: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Applies dark theme styling to a JPanel.
     */
    public static void stylePanel(JPanel panel) {
        panel.setBackground(BG_DARK);
    }

    /**
     * Applies dark theme styling to a JLabel.
     */
    public static void styleLabel(JLabel label) {
        label.setForeground(FG_LIGHT);
    }

    /**
     * Applies dark theme styling to a JTextField.
     */
    public static void styleTextField(JTextField textField) {
        textField.setBackground(BG_LIGHTER);
        textField.setForeground(FG_LIGHT);
        textField.setCaretColor(FG_LIGHT);
    }

    /**
     * Applies dark theme styling to a JTable.
     */
    public static void styleTable(JTable table) {
        table.setBackground(BG_LIGHTER);
        table.setForeground(FG_LIGHT);
        table.setGridColor(GRID);
        table.setSelectionBackground(SELECTION_BG);
        table.setSelectionForeground(SELECTION_FG);
        table.getTableHeader().setBackground(BG_DARKER);
        table.getTableHeader().setForeground(FG_LIGHT);
    }

    /**
     * Applies dark theme styling to a JScrollPane.
     */
    public static void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.getViewport().setBackground(BG_DARK);
        scrollPane.setBackground(BG_DARK);
    }

    /**
     * Creates a styled DefaultTableCellRenderer for text columns.
     */
    public static DefaultTableCellRenderer createTableCellRenderer() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBackground(BG_LIGHTER);
        renderer.setForeground(FG_LIGHT);
        return renderer;
    }

    /**
     * Creates a styled DefaultTableCellRenderer for icon columns.
     */
    public static DefaultTableCellRenderer createIconCellRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel();
                label.setOpaque(true);
                if (isSelected) {
                    label.setBackground(table.getSelectionBackground());
                } else {
                    label.setBackground(table.getBackground());
                }
                if (value instanceof ImageIcon) {
                    label.setIcon((ImageIcon) value);
                }
                return label;
            }
        };
    }
}

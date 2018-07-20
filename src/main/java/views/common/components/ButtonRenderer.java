package views.common.components;

import literals.Icons;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRenderer extends JButton implements TableCellRenderer {

    private static final long serialVersionUID = -1821483718342159691L;
    private static final ImageIcon lockIcon = Icons.LOCK_ICON;

    public ButtonRenderer() {
        setOpaque(true);
        setIcon(lockIcon);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(UIManager.getColor("Button.background"));
        }
        return this;
    }
}
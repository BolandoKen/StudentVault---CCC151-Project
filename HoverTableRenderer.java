import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class HoverTableRenderer extends DefaultTableCellRenderer {
    private int hoveredRow = -1; // Store hovered row index

    public void setHoveredRow(int row) {
        this.hoveredRow = row;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Default styling
        cell.setForeground(Color.BLACK); // Ensure text stays black
        cell.setBackground(Color.WHITE); // Default row background

        setBorder(null);

        // Apply hover effect (if row is hovered)
        if (row == hoveredRow) {
            cell.setBackground(Color.LIGHT_GRAY); // Light gray hover color
        }

        // Apply selection effect (if row is selected)
        if (isSelected) {
            cell.setBackground(new Color(0x85DCEA));
            cell.setForeground(Color.BLACK); // Keep text readable
        }

        return cell;
    }
}
